package by.company.auction.dao;

import by.company.auction.model.Bid;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDao extends AbstractDao<Bid> {

    private static BidDao bidDaoInstance;
    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();

    private BidDao() {
    }

    @Override
    protected Class<Bid> getEntityClass() {
        return Bid.class;
    }

    @Override
    public Bid create(Bid bid) {

        Integer bidId = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "INSERT INTO bids (value, time, lot_id, user_id) VALUES (?, ?, ?, ?) RETURNING id")) {
            preparedStatement.setBigDecimal(1, bid.getValue());
            preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(bid.getTime()));
            preparedStatement.setInt(3, bid.getLotId());
            preparedStatement.setInt(4, bid.getUserId());
            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                bidId = resultSet.getInt(1);
            }
            resultSet.close();

        } catch (SQLException e) {
            throw new IllegalStateException();
        }
        return findById(bidId);
    }

    @Override
    public Bid update(Bid bid) {

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "UPDATE bids SET value = ?, time = ?, lot_id = ?, user_id = ? WHERE (id = ?)")) {
            preparedStatement.setBigDecimal(1, bid.getValue());
            preparedStatement.setTimestamp(2, java.sql.Timestamp.valueOf(bid.getTime()));
            preparedStatement.setInt(3, bid.getLotId());
            preparedStatement.setInt(4, bid.getUserId());
            preparedStatement.setInt(5, bid.getId());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException();
        }
        return bid;
    }

    public Bid findTopBidByLotId(Integer lotId) {

        Bid topBid = null;

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM bids WHERE value = (SELECT MAX(value) FROM bids WHERE lot_id = ?)")) {
            preparedStatement.setInt(1, lotId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                topBid = Bid.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet);
            }
            resultSet.close();

        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return topBid;
    }

    public List<Bid> findBidsByLotId(Integer lotId) {

        List<Bid> bids = new ArrayList<>();

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM bids WHERE lot_id = ?")) {
            preparedStatement.setInt(1, lotId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bids.add(Bid.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return bids;
    }

    public List<Bid> findBidsByUserId(Integer userId) {

        List<Bid> bids = new ArrayList<>();

        try (PreparedStatement preparedStatement = connectionProvider.getConnection().prepareStatement(
                "SELECT * FROM bids WHERE user_id = ?")) {
            preparedStatement.setInt(1, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                bids.add(Bid.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
            }
            resultSet.close();

        } catch (Exception e) {
            throw new IllegalStateException();
        }
        return bids;
    }

    public static BidDao getInstance() {
        if (bidDaoInstance != null) {
            return bidDaoInstance;
        }
        bidDaoInstance = new BidDao();
        return bidDaoInstance;
    }
}
