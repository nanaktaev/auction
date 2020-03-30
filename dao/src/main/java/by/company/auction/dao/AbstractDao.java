package by.company.auction.dao;

import by.company.auction.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class AbstractDao<T extends BaseEntity> {

    private final Map<Integer, T> entitiesMap;
    private final Class<T> tClass;

    AbstractDao(Class<T> tClass) {
        this.entitiesMap = FileAccessor.getEntitiesMap(tClass);
        this.tClass = tClass;
    }

    private int generateNewId() {
        int maxId = 0;
        for (int id : entitiesMap.keySet()) {
            if (id > maxId) {
                maxId = id;
            }
        }
        return maxId + 1;
    }

    public T create(T entity) {
        entity.setId(generateNewId());
        entitiesMap.put(entity.getId(), entity);

        FileAccessor.saveEntitiesMap(entitiesMap, tClass);
        return entity;
    }

    @SuppressWarnings("WeakerAccess")
    public T findById(Integer id) {
        return entitiesMap.get(id);
    }

    public T update(T entity) {
        if (entity.getId() == null) {
            throw new IllegalStateException("Объект по данному id не найден.");
        }
        entitiesMap.put(entity.getId(), entity);

        FileAccessor.saveEntitiesMap(entitiesMap, tClass);
        return entity;
    }

    public List<T> findByIds(List<Integer> ids) {
        List<T> entities = new ArrayList<>();
        for (Integer id : ids) {
            entities.add(findById(id));
        }
        return entities;
    }

    public void delete(Integer id) {
        entitiesMap.remove(id);
        FileAccessor.saveEntitiesMap(entitiesMap, tClass);
    }

    @SuppressWarnings("WeakerAccess")
    public List<T> findAll() {
        return new ArrayList<T>(entitiesMap.values());
    }

}
