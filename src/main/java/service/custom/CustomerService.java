package service.custom;

import dto.Customer;

public interface CustomerService {
    public boolean addCustomer(Customer customer);
    public Customer searchCustomer(String id);
    public boolean updateCustomer(Customer customer);
    public boolean deleteCustomer(String id);
}
