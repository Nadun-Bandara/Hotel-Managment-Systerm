package repository.impl;

import dto.Customer;

public interface CustomerRepository {
     default boolean addCustomer(Customer customer){
        return true;
    }

}
