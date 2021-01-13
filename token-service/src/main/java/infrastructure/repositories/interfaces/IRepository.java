package infrastructure.repositories.interfaces;

import exceptions.CustomerAlreadyRegisteredException;
import exceptions.CustomerNotFoundException;
import java.util.List;
public interface IRepository<T> {
    void add(T obj) throws CustomerAlreadyRegisteredException;
}