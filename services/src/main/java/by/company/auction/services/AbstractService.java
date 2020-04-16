package by.company.auction.services;

import by.company.auction.dao.AbstractDao;
import by.company.auction.model.BaseEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class AbstractService<T extends BaseEntity, DAO extends AbstractDao<T>> {

    DAO dao;
    private final Logger LOGGER = LogManager.getLogger(AbstractService.class);

    void setDao(DAO dao) {
        this.dao = dao;
    }

    public T findById(Integer id) {

        LOGGER.debug("findById() dao = {}, id = {}", dao.getClass().getSimpleName(), id);
        return dao.findById(id);

    }

    public List<T> findAll() {

        LOGGER.debug("findAll() dao = {}", dao.getClass().getSimpleName());
        return dao.findAll();

    }

    public T create(T entity) {

        LOGGER.debug("create() entity = {}", entity);
        return dao.create(entity);

    }

    public void delete(Integer id) {

        LOGGER.debug("delete() dao = {}, id = {}", dao.getClass().getSimpleName(), id);
        dao.delete(id);

    }

    public T update(T entity) {

        LOGGER.debug("update() entity = {}", entity);
        return dao.update(entity);

    }

    boolean exists(Integer id) {

        LOGGER.debug("exists() dao = {}, id = {}", dao.getClass().getSimpleName(), id);
        return findById(id) != null;

    }

}
