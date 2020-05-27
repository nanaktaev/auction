package by.company.auction.services;

import by.company.auction.common.exceptions.NoSuchEntityException;
import by.company.auction.common.exceptions.NotYetPopulatedException;
import by.company.auction.converters.AbstractConverter;
import by.company.auction.dto.BaseDto;
import by.company.auction.model.BaseEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Transactional
public abstract class AbstractService<
        T extends BaseEntity,
        D extends BaseDto,
        R extends JpaRepository<T, Integer>,
        C extends AbstractConverter<T, D>> {

    final R repository;
    final C converter;

    protected AbstractService(R repository, C converter) {
        this.repository = repository;
        this.converter = converter;
    }

    public D findById(Integer id) {

        log.debug("findById() id = {}", id);

        T entity = repository.findById(id).orElseThrow(() -> new NoSuchEntityException("Nothing has been found by this id."));

        return converter.convertToDto(entity);
    }

    public List<D> findAll() {

        log.debug("findAll()");

        List<T> entities = repository.findAll();

        if (entities.isEmpty()) {
            throw new NotYetPopulatedException("There is nothing here yet.");
        }

        return converter.convertListToDto(entities);
    }

    public D create(D dto) {

        dto.setId(null);

        log.debug("create() dto = {}", dto);

        T entity = repository.save(converter.convertToEntity(dto));

        return converter.convertToDto(entity);
    }

    public void delete(Integer id) {

        log.debug("delete() id = {}", id);

        repository.deleteById(id);
    }

    public D update(D dto) {

        log.debug("update() dto = {}", dto);

        T entity = repository.save(converter.convertToEntity(dto));

        return converter.convertToDto(entity);
    }

    public boolean exists(Integer id) {
        return repository.findById(id).isPresent();
    }
}
