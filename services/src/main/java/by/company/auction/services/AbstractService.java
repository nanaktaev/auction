package by.company.auction.services;

import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.model.BaseEntity;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Log4j2
@Transactional
public abstract class AbstractService<T extends BaseEntity, R extends JpaRepository<T, Integer>> {

    final R repository;

    protected AbstractService(R repository) {
        this.repository = repository;
    }

    public T findById(Integer id) {

        log.debug("findById() dao = {}, id = {}", repository.getClass().getSimpleName(), id);

        return repository.findById(id).orElseThrow(() -> new NoSuchEntityException("Ничего не найдено."));
    }

    public List<T> findAll() {

        log.debug("findAll() dao = {}", repository.getClass().getSimpleName());

        List<T> entities = repository.findAll();

        if (entities.isEmpty()) {
            throw new NotYetPopulatedException("Пока здесь ничего нет.");
        }

        return entities;
    }

    public T create(T entity) {

        log.debug("create() entity = {}", entity);

        return repository.save(entity);
    }

    public void delete(Integer id) {

        log.debug("delete() dao = {}, id = {}", repository.getClass().getSimpleName(), id);

        repository.delete(findById(id));
    }

    public T update(T entity) {

        log.debug("update() entity = {}", entity);

        return repository.save(entity);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean exists(Integer id) {
        return repository.findById(id).isPresent();
    }

}
