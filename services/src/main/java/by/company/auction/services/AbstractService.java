package by.company.auction.services;

import by.company.auction.dao.AbstractDao;
import by.company.auction.model.Base;

import java.util.List;

public abstract class AbstractService<T extends Base, DAO extends AbstractDao<T>> {

    DAO dao;

    void setDao(DAO dao) {
        this.dao = dao;
    }

    public T findById(Integer id) {
        return dao.findById(id);
    }

    public List<T> findAll() {
        return dao.findAll();
    }

    public T create(T entity) {
        return dao.create(entity);
    }

    public T update(T entity) {
        return dao.update(entity);
    }

    public boolean exists(Integer id) {
        return findById(id) != null;
    }

    public List<T> findByIds(List<Integer> ids) {
        return dao.findByIds(ids);
    }
}
