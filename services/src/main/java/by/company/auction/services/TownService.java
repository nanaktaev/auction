package by.company.auction.services;

import by.company.auction.dao.TownDao;
import by.company.auction.model.Town;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TownService extends AbstractService<Town, TownDao> {

    private static TownService townServiceInstance;
    private final Logger LOGGER = LogManager.getLogger(TownService.class);

    public Town findTownByName(String name) {

        LOGGER.debug("findTownByName() name = {}", name);
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
