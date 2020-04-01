package by.company.auction.dao;

import by.company.auction.model.Bid;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BidDao extends AbstractDao<Bid> {

    private static BidDao bidDaoInstance;
    private static final ConnectionProvider connectionProvider = ConnectionProvider.getInstance();

    private BidDao() {
        super(Bid.class);
    }

    public List<Bid> findBidsByLotId(Integer lotId) {

        List<Bid> bids = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM bids WHERE lot_id = ?")) {

            preparedStatement.setInt(1, lotId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    bids.add(Bid.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }

        return bids;
    }

    public List<Bid> findBidsByUserId(Integer userId) {

        List<Bid> bids = new ArrayList<>();

        try (Connection connection = connectionProvider.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM bids WHERE user_id = ?")) {

            preparedStatement.setInt(1, userId);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                while (resultSet.next()) {
                    bids.add(Bid.class.getDeclaredConstructor().newInstance().buildFromResultSet(resultSet));
                }

            } catch (Exception exception) {
                System.out.println(exception.getMessage());
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
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
