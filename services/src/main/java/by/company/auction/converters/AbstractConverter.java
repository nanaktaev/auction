package by.company.auction.converters;

import by.company.auction.dto.BaseDto;
import by.company.auction.model.BaseEntity;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractConverter<T extends BaseEntity, D extends BaseDto> {

    public abstract D convertToDto(T entity);

    public abstract T convertToEntity(D dto);

    public List<D> convertListToDto(List<T> entities) {

        return entities.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
