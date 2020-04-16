package by.company.auction.services;

import by.company.auction.dao.LotDao;
import by.company.auction.model.Lot;
import by.company.auction.validators.LotValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;

import static by.company.auction.common.security.AuthenticationConfig.authentication;

public class LotService extends AbstractService<Lot, LotDao> {

    private static LotService lotServiceInstance;
    private final Logger LOGGER = LogManager.getLogger(LotService.class);

    public List<Lot> findLotsByTownId(Integer townId) {

        LOGGER.debug("findLotsByTownId() townId = {}", townId);
        return dao.findLotsByTownId(townId);

    }

    public List<Lot> findLotsByCategoryId(Integer categoryId) {

        LOGGER.debug("findLotsByCategoryId() categoryId = {}", categoryId);
        return dao.findLotsByCategoryId(categoryId);

    }

    @SuppressWarnings("WeakerAccess")
    public List<Lot> findLotsByUserId(Integer userId) {

        LOGGER.debug("findLotsByUserId() userId = {}", userId);
        return dao.findLotsByUserId(userId);

    }

    List<Lot> findExpiredLotsByUserId(Integer userId) {

        LOGGER.debug("findExpiredLotsByUserId() userId = {}", userId);
        return dao.findExpiredLotsByUserId(userId);

    }

    public Lot createLot(Lot lot) {

        LOGGER.debug("createLot() lot = {}", lot);

        Integer companyId = authentication.getUserCompanyId();

        LotValidator.getInstance().validate(lot);

        lot.setCompanyId(companyId);
        lot.setOpened(LocalDateTime.now());
        lot.setPrice(lot.getPriceStart());

        return create(lot);
    }

    public static LotService getInstance() {
        if (lotServiceInstance != null) {
            return lotServiceInstance;
        }
        lotServiceInstance = new LotService();
        lotServiceInstance.setDao(LotDao.getInstance());
        return lotServiceInstance;
    }
}
