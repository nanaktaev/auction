package by.company.auction.dao;

import by.company.auction.model.Town;

public class TownDao extends AbstractDao<Town> {

    private static TownDao townDaoInstance;

    private TownDao() {
    }

    public static TownDao getInstance() {
        if (townDaoInstance != null) {
            return townDaoInstance;
        }
        townDaoInstance = new TownDao();
        return townDaoInstance;
    }

}
