package service.custom.impl;

import com.google.inject.Inject;
import dto.Customer;
import entity.CustomerEntity;
import org.modelmapper.ModelMapper;
import repository.custom.CustomerRepository;
import service.custom.CustomerService;

public class CustomerServiceImpl implements CustomerService {
    @Inject
    CustomerRepository repository;
    @Override
    public boolean addCustomer(Customer customer) {
        CustomerEntity entity = new ModelMapper().map(customer, CustomerEntity.class);
        return repository.add(entity);
    }

    @Override
    public Customer searchCustomer(String id) {
        CustomerEntity entity=repository.search(id);
        Customer customer=new ModelMapper().map(entity, Customer.class);
        return customer;
    }

    @Override
    public boolean updateCustomer(Customer customer) {
        CustomerEntity entity=new ModelMapper().map(customer,CustomerEntity.class);
        return repository.update(entity);
    }

    @Override
    public boolean deleteCustomer(String id) {
        CustomerEntity entity=repository.search(id);
        return repository.delete(entity);
    }
}
