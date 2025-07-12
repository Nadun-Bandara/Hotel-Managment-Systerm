package repository.custom.impl;

import entity.CustomerEntity;
import org.hibernate.Session;
import repository.custom.CustomerRepository;
import util.HibernateUtil;

public class CustomerRepositoryImpl implements CustomerRepository {

    @Override
    public boolean add(CustomerEntity entity) {
        System.out.println(entity);
        Session session = HibernateUtil.getSession();
        entity.setId(null);
        session.beginTransaction();
        session.save(entity);
        session.getTransaction().commit();
        session.close();
        return true;

    }

    @Override
    public CustomerEntity search(String id) {
        return null;
    }

    @Override
    public boolean update(CustomerEntity entity) {
        return false;
    }

    @Override
    public boolean delete(CustomerEntity entity) {
        return false;
    }
}
