package services;

import dto.TransactionDTO;
import exceptions.account.AccountException;
import exceptions.customer.CustomerException;
import exceptions.customer.CustomerNotFoundException;
import exceptions.merchant.MerchantException;
import exceptions.merchant.MerchantNotFoundException;
import exceptions.token.TokenNotValidException;
import exceptions.transaction.TransactionException;
import infrastructure.bank.Account;
import infrastructure.bank.BankService;
import infrastructure.bank.Transaction;
import interfaces.rabbitmq.payment.RabbitMQPaymentAdapterFactory;
import interfaces.rabbitmq.token.RabbitMQTokenAdapterFactory;
import services.interfaces.IPaymentService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * @primary-author Troels (s161791)
 * @co-author Daniel (s151641)
 */
@ApplicationScoped
public class PaymentService implements IPaymentService {

    BankService bs;
    MapperService mapper;

    @Inject
    public PaymentService(Bank bank, MapperService mapper) {
        this.bs = bank.getBankServicePort();
        this.mapper = mapper;
    }

    @Override
    public void processPayment(String customerId, String merchantId, int amount, String token)
            throws TokenNotValidException, CustomerNotFoundException, MerchantNotFoundException, TransactionException {
        PaymentEventService eventService = new RabbitMQPaymentAdapterFactory().getService();

        Account customer = null;
        Account merchant = null;

        String desc = "Transaction between Customer (" + merchantId + ")" +
                " and Merchant (" + customerId + ") for amount " + amount +
                " with token " + token;
        TransactionDTO dto = new TransactionDTO(BigDecimal.valueOf(amount), customer.getBalance(), merchantId, customerId, desc, new Date());
        try {
            // Checks if the token is valid
            TokenEventService service = new RabbitMQTokenAdapterFactory().getService();
            if (!service.validateToken(token).equals(token)) {
                throw new TokenNotValidException("The token (" + token + ") is not valid");
            }

            // TODO get with account service
            merchant = bs.getAccount(customerId);
            customer = bs.getAccount(merchantId);


            bs.transferMoneyFromTo(
                    customer.getId(),
                    merchant.getId(),
                    BigDecimal.valueOf(amount),
                    desc);

            try {
                eventService.sendTransactionDone(dto, true);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        } catch (TokenNotValidException e) {
            throw new TokenNotValidException(e.getMessage());
        } catch (Exception e) {
            try {
                eventService.sendTransactionDone(dto, false);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            if (customer == null) {
                throw new CustomerNotFoundException("Customer (" + merchantId + ") is not found!");
            }
            if (merchant == null) {
                throw new MerchantNotFoundException("Merchant (" + customerId + ") is not found!");
            }
            throw new TransactionException(e.getMessage());
        }
    }

    @Override
    public void refund(String customerId, String merchantId, int amount) throws CustomerException, MerchantException, TransactionException, TokenNotValidException {
        processPayment(merchantId, customerId, amount, "");
    }

    @Override
    public List<TransactionDTO> getTransactions(String accountId) throws AccountException {
        try {
            return mapper.mapList(bs.getAccount(accountId).getTransactions(), TransactionDTO.class);
        } catch (Exception e) {
            throw new AccountException("Account (" + accountId + ") is not found!");
        }
    }

    @Override
    public Transaction getLatestTransaction(String accountId) throws AccountException {
        try {
            Comparator<Transaction> comparator = (p1, p2) -> p1.getTime().compare(p2.getTime());
            return bs.getAccount(accountId).getTransactions().stream().max(comparator).get();
        } catch (Exception e) {
            throw new AccountException("Account (" + accountId + ") is not found!");
        }
    }

}