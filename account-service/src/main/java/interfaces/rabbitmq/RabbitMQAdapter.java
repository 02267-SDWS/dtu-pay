package interfaces.rabbitmq;


import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import infrastructure.bank.Transaction;
import interfaces.rest.RootApplication;
import services.MapperService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

@ApplicationScoped
public class RabbitMQAdapter {
/*
    private static final String EXCHANGE_NAME = "message-hub";
    private final static Logger LOGGER = Logger.getLogger(RootApplication.class.getName());

    @Inject
    IPaymentService service;

    private final static String routingKeyRead = "payment.*";
    private final static String routingKeyWrite = "payment.service";

    public void initConnection() throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        String queueNamePayments = channel.queueDeclare().getQueue();
        channel.queueBind(queueNamePayments, EXCHANGE_NAME, routingKeyRead);

        Gson gson = new Gson();
        MapperService mapper = new MapperService();

        DeliverCallback paymentsCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] splitMessage = message.split(" ");
            String messageType = splitMessage[0];

            LOGGER.log(Level.INFO, "RABBITMQ: Message type: " + messageType);
            LOGGER.log(Level.INFO, "RABBITMQ: Raw message: " + message);

            if (messageType.equals("payment")) {
                String payload = splitMessage[1];
                TransactionDTO dto = gson.fromJson(payload, TransactionDTO.class);
                try {
                    service.createTransaction(dto.getCreditor(), dto.getDebtor(), dto.getAmount().intValue());
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, "PaymentSuccessful".getBytes(StandardCharsets.UTF_8));
                } catch (TransactionException | MerchantException | CustomerException e) {
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, e.getMessage().getBytes(StandardCharsets.UTF_8));
                }
            } else if (messageType.equals("refund")) {
                String payload = splitMessage[1];
                TransactionDTO dto = gson.fromJson(payload, TransactionDTO.class);
                try {
                    service.refund(dto.getCreditor(), dto.getDebtor(), dto.getAmount().intValue());
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, "RefundSuccessful".getBytes(StandardCharsets.UTF_8));
                } catch (TransactionException | MerchantException | CustomerException e) {
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, e.getMessage().getBytes(StandardCharsets.UTF_8));
                }
            } else if (messageType.equals("getLatestTransaction")) {
                String userId = splitMessage[1];
                try {
                    Transaction transaction = service.getLatestTransaction(userId);
                    TransactionDTO dto = mapper.map(transaction, TransactionDTO.class);
                    String json = gson.toJson(dto);
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, json.getBytes(StandardCharsets.UTF_8));
                } catch (CustomerException | AccountException e) {
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, e.getMessage().getBytes(StandardCharsets.UTF_8));
                }
            } else if (messageType.equals("getTransactions")) {
                String userId = splitMessage[1];
                try {
                    List<TransactionDTO> transactions = service.getTransactions(userId);
                    String json = gson.toJson(transactions);
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, json.getBytes(StandardCharsets.UTF_8));
                } catch (CustomerException | AccountException e) {
                    channel.basicPublish(EXCHANGE_NAME, routingKeyWrite, null, e.getMessage().getBytes(StandardCharsets.UTF_8));
                }
            } else {
                LOGGER.log(Level.INFO, "RABBITMQ: Message was ignored.");
            }
        };

        channel.basicConsume(queueNamePayments, true, paymentsCallback, consumerTag -> {
        });
    }

 */

}