package by.company.auction.dao;

import by.company.auction.common.exceptions.DataAccessException;
import by.company.auction.model.Lot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class LotDao extends AbstractDao<Lot> {

    private final ConnectionProvider CONNECTION_PROVIDER = ConnectionProvider.getInstance();
    private final Logger LOGGER = LogManager.getLogger(LotDao.class);

    @Override
    protected Class<Lot> getEntityClass() {
        return Lot.class;
    }

    @Override
    public Lot create(Lot lot) {

        Integer lotId = null;

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "INSERT INTO lots (title, description, price, price_start, step," +
                        " opened, closes, category_id, company_id, town_id)" +
                        " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id")) {
            preparedStatement.setString(1, lot.getTitle());
            preparedStatement.setString(2, lot.getDescription());
            preparedStatement.setBigDecimal(3, lot.getPrice());
            preparedStatement.setBigDecimal(4, lot.getPriceStart());
            preparedStatement.setBigDecimal(5, lot.getStep());
            preparedStatement.setTimestamp(6, java.sql.Timestamp.valueOf(lot.getOpened()));
            preparedStatement.setTimestamp(7, java.sql.Timestamp.valueOf(lot.getCloses()));
            preparedStatement.setInt(8, lot.getCategoryId());
            preparedStatement.setInt(9, lot.getCompanyId());
            preparedStatement.setInt(10, lot.getTownId());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                lotId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return findById(lotId);
    }

    @Override
    public Lot update(Lot lot) {

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "UPDATE lots SET title = ?, description = ?, price =?, price_start = ?, step = ?," +
                        " opened = ?, closes = ?, category_id = ?, company_id = ?, town_id = ? WHERE (id = ?)")) {
            preparedStatement.setString(1, lot.getTitle());
            preparedStatement.setString(2, lot.getDescription());
            preparedStatement.setBigDecimal(3, lot.getPrice());
            preparedStatement.setBigDecimal(4, lot.getPriceStart());
            preparedStatement.setBigDecimal(5, lot.getStep());
            preparedStatement.setTimestamp(6, java.sql.Timestamp.valueOf(lot.getOpened()));
            preparedStatement.setTimestamp(7, java.sql.Timestamp.valueOf(lot.getCloses()));
            preparedStatement.setInt(8, lot.getCategoryId());
            preparedStatement.setInt(9, lot.getCompanyId());
            preparedStatement.setInt(10, lot.getTownId());
            preparedStatement.setInt(11, lot.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return lot;
    }

    public List<Lot> findLotsByTownId(Integer townId) {

        List<Lot> lots = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM lots WHERE town_id = ?")) {
            preparedStatement.setInt(1, townId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lots.add(Lot.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return lots;
    }

    public List<Lot> findLotsByCategoryId(Integer categoryId) {

        List<Lot> lots = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT * FROM lots WHERE category_id = ?")) {
            preparedStatement.setInt(1, categoryId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lots.add(Lot.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return lots;
    }

    public List<Lot> findLotsByUserId(Integer userId) {

        List<Lot> lots = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT lots.* FROM lots LEFT JOIN bids ON lots.id = bids.lot_id " +
                        "WHERE bids.user_id = ? GROUP BY lots.id")) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lots.add(Lot.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return lots;
    }

    public List<Lot> findExpiredLotsByUserId(Integer userId) {

        List<Lot> lots = new ArrayList<>();

        try (PreparedStatement preparedStatement = CONNECTION_PROVIDER.getConnection().prepareStatement(
                "SELECT lots.* FROM lots LEFT JOIN bids ON lots.id = bids.lot_id " +
                        "WHERE bids.user_id = ? AND lots.closes < NOW() GROUP BY lots.id")) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                lots.add(Lot.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            LOGGER.error(e);
            throw new DataAccessException();
        }
        return lots;
    }

    private static LotDao lotDaoInstance;

    public static LotDao getInstance() {
        if (lotDaoInstance != null) {
            return lotDaoInstance;
        }
        lotDaoInstance = new LotDao();
        return lotDaoInstance;
    }
}
