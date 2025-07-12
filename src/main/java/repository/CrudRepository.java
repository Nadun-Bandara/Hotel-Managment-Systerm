package repository;

import entity.CustomerEntity;

public interface CrudRepository<C, S> {

    boolean add(CustomerEntity entity);

    CustomerEntity search(String id);

    boolean update(CustomerEntity entity);

    boolean delete(CustomerEntity entity);
}
