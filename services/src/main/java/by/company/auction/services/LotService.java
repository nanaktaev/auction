package by.company.auction.services;

import by.company.auction.dao.LotDao;
import by.company.auction.model.Category;
import by.company.auction.model.Lot;
import by.company.auction.model.Town;
import by.company.auction.model.User;
import validators.LotValidator;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static by.company.auction.secuirty.AuthenticatonContainer.authentication;

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

        LotValidator.validateCreation(lot);

        lot.setVendorId(userId);
        lot.setOpened(LocalDateTime.now());
        lot.setPrice(lot.getPriceStart());
        lot.setUserIds(new ArrayList<>());
        lot.setBidIds(new ArrayList<>());
        create(lot);

        return lot;
    }

    public void deleteLot(Lot lot) {

        CategoryService categoryService = CategoryService.getInstance();
        TownService townService = TownService.getInstance();
        UserService userService = UserService.getInstance();

        List<Integer> bidIds = new ArrayList<>(lot.getBidIds());
        for (Integer bidId : bidIds) {
            BidService.getInstance().deleteBid(bidId);
        }

        for (Integer userId : lot.getUserIds()) {
            User user = userService.findById(userId);
            user.getLotIds().removeIf(lot.getId()::equals);
            userService.update(user);
        }

        Category category = categoryService.findById(lot.getCategoryId());
        category.getLotIds().removeIf(lot.getId()::equals);
        categoryService.update(category);

        Town town = townService.findById(lot.getTownId());
        town.getLotIds().removeIf(lot.getId()::equals);
        townService.update(town);

        dao.delete(lot.getId());
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
