package by.company.auction.dao;

import by.company.auction.model.Town;

public class TownDao extends AbstractDao<Town> {

    private static TownDao townDaoInstance;

    private TownDao() {
        super(Town.class);
    }

    public Town findByName(String name) {
        for (Town town : findAll()) {
            if (name.equals(town.getName())) {
                return town;
            }
        }
        return null;
    }

    public static TownDao getInstance() {
        if (townDaoInstance != null) {
            return townDaoInstance;
        }
        townDaoInstance = new TownDao();
        return townDaoInstance;
    }

}
