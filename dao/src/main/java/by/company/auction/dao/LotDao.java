package by.company.auction.dao;

import by.company.auction.model.Lot;

import java.util.ArrayList;
import java.util.List;

public class LotDao extends AbstractDao<Lot> {

    private static LotDao lotDaoInstance;

    private LotDao() {
    }

    public List<Lot> findLotsByTownId(Integer townId) {
        ArrayList<Lot> lots = new ArrayList<>();
        for (Lot lot : findAll()) {
            if (townId.equals(lot.getTownId())) lots.add(lot);
        }
        return lots;
    }

    public List<Lot> findLotsByCategoryId(Integer categoryId) {
        ArrayList<Lot> lots = new ArrayList<>();
        for (Lot lot : findAll()) {
            if (categoryId.equals(lot.getCategoryId())) lots.add(lot);
        }
        return lots;
    }

    public List<Lot> findLotsByUserId(Integer userId) {
        ArrayList<Lot> lots = new ArrayList<>();
        for (Lot lot : findAll()) {
            if (lot.getUserIds().contains(userId)) lots.add(lot);
        }
        return lots;
    }

    public List<Lot> findExpiredLotsByUserId(Integer userId) {
        ArrayList<Lot> lots = new ArrayList<>();
        for (Lot lot : findAll()) {
            if (lot.getUserIds().contains(userId) && lot.isExpired()) lots.add(lot);
        }
        return lots;
    }

    public static LotDao getInstance() {
        if (lotDaoInstance != null) return lotDaoInstance;
        lotDaoInstance = new LotDao();
        return lotDaoInstance;
    }
}
