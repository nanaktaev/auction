package by.company.auction.dao;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

class FileAccessor {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    static <T> Map<Integer, T> getEntitiesMap(Class<T> tClass) {

        String entityName = tClass.getSimpleName();
        File databaseDirectory = new File("database");
        File entitiesFile = new File("database\\" + entityName + ".json");

        try {
            if (!entitiesFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                databaseDirectory.mkdir();
                //noinspection ResultOfMethodCallIgnored
                entitiesFile.createNewFile();
                objectMapper.writeValue(entitiesFile, new HashMap<Integer, T>());
            }

            return objectMapper.readValue(entitiesFile, TypeFactory.defaultInstance()
                    .constructMapType(Map.class, Integer.class, tClass));

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
        return null;
    }

    static <T> void saveEntitiesMap(Map<Integer, T> entitiesMap, Class<T> tClass) {

        String entityName = tClass.getSimpleName();

        try {
            objectMapper.writeValue(new File("database\\" + entityName + ".json"), entitiesMap);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

}