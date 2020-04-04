package by.company.auction.services;

import by.company.auction.dao.TownDao;
import by.company.auction.model.Town;

public class TownService extends AbstractService<Town, TownDao> {

    private static TownService townServiceInstance;

    private TownService() {
    }

    public Town findTownByName(String name) {
        return dao.findTownByName(name);
    }

    public static TownService getInstance() {
        if (townServiceInstance != null) {
            return townServiceInstance;
        }
        townServiceInstance = new TownService();
        townServiceInstance.setDao(TownDao.getInstance());
        return townServiceInstance;
    }
}
