package by.company.auction.dao;

import by.company.auction.model.Lot;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LotDao extends AbstractDao<Lot> {

    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();
    private static LotDao lotDaoInstance;

    private LotDao() {
        super(Lot.class);
    }

    public List<Lot> findLotsByTownId(Integer townId) {
        ArrayList<Lot> lots = new ArrayList<>();
        for (Lot lot : findAll()) {
            if (townId.equals(lot.getTownId())) {
                lots.add(lot);
            }
        }
        return lots;
    }

    public List<Lot> findLotsByCategoryId(Integer categoryId) {
        ArrayList<Lot> lots = new ArrayList<>();
        for (Lot lot : findAll()) {
            if (categoryId.equals(lot.getCategoryId())) {
                lots.add(lot);
            }
        }
        return lots;
    }

    public List<Lot> findLotsByUserId(Integer userId) {

        List<Lot> lots = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT lots.* FROM lots LEFT JOIN bids ON lots.id = bids.lot_id " +
                             "WHERE bids.user_id = ? GROUP BY lots.id")) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    lots.add(Lot.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return lots;
    }

    public List<Lot> findExpiredLotsByUserId(Integer userId) {

        List<Lot> lots = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT lots.* FROM lots LEFT JOIN bids ON lots.id = bids.lot_id " +
                             "WHERE bids.user_id = ? AND lots.closes <= now() GROUP BY lots.id")) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    lots.add(Lot.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return lots;
    }

    public void deleteLotById(Integer lotId) {

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM lots WHERE id = ?; DELETE FROM bids WHERE lot_id = ?")) {

            preparedStatement.setInt(1, lotId);
            preparedStatement.executeQuery();

        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }

    public static LotDao getInstance() {
        if (lotDaoInstance != null) {
            return lotDaoInstance;
        }
        lotDaoInstance = new LotDao();
        return lotDaoInstance;
    }
}
