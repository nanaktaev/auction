package by.company.auction.services;

import by.company.auction.dao.LotDao;
import by.company.auction.model.Category;
import by.company.auction.model.Lot;
import by.company.auction.model.Town;
import by.company.auction.model.User;
import by.company.auction.validators.LotValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.company.auction.secuirty.AuthenticatonConfig.authentication;

public class LotService extends AbstractService<Lot, LotDao> {

    private static LotService lotServiceInstance;

    private LotService() {
    }

    public List<Lot> findLotsByTownId(Integer townId) {
        return dao.findLotsByTownId(townId);
    }

    public List<Lot> findLotsByCategoryId(Integer categoryId) {
        return dao.findLotsByCategoryId(categoryId);
    }

    public List<Lot> findLotsByUserId(Integer userId) {
        return dao.findLotsByUserId(userId);
    }

    List<Lot> findExpiredLotsByUserId(Integer userId) {
        return dao.findExpiredLotsByUserId(userId);
    }

    public Lot createLot(Lot lot) {
        Integer userId = authentication.getUserId();

        LotValidator.validate(lot);

        lot.setVendorId(userId);
        lot.setOpened(LocalDateTime.now());
        lot.setPrice(lot.getPriceStart());
        create(lot);

        return lot;
    }

    public void deleteLot(Integer lotId) {
        dao.deleteLotById(lotId);
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
