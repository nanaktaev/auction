package by.company.auction.services;

import by.company.auction.dao.LotDao;
import by.company.auction.model.Lot;
import by.company.auction.validators.LotValidator;

import java.time.LocalDateTime;
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

    @SuppressWarnings("WeakerAccess")
    public List<Lot> findLotsByUserId(Integer userId) {
        return dao.findLotsByUserId(userId);
    }

    List<Lot> findExpiredLotsByUserId(Integer userId) {
        return dao.findExpiredLotsByUserId(userId);
    }

    public Lot createLot(Lot lot) {
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
