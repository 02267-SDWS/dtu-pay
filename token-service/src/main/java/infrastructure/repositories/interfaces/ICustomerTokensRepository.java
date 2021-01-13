package infrastructure.repositories.interfaces;

import domain.CustomerTokens;
import exceptions.CustomerNotFoundException;
import exceptions.TokenNotFoundException;

public interface ICustomerTokensRepository extends IRepository<CustomerTokens> {
    CustomerTokens get(String id) throws CustomerNotFoundException;
    CustomerTokens getCustomerWithTokenId(String tokenId) throws TokenNotFoundException;
    void delete(String id) throws CustomerNotFoundException;
}