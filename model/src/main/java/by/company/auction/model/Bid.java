package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("Bids")
public class Bid extends BaseEntity {

    private BigDecimal value;
    private LocalDateTime time;
    private Integer lotId;
    private Integer userId;

    @Override
    public Bid buildFromResultSet(ResultSet resultSet) throws SQLException {

        Bid bid = new Bid();
        bid.setId(resultSet.getInt(1));
        bid.setValue(resultSet.getBigDecimal(2));
        bid.setTime(resultSet.getTimestamp(3).toLocalDateTime());
        bid.setLotId(resultSet.getInt(4));
        bid.setUserId(resultSet.getInt(5));

        return bid;
    }

    @Override
    public String toString() {
        return "Ставка величиной " + value
                + ". Объявлена " + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                + " (id пользователя - " + userId
                + ", id лота - " + lotId + ")";
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

}
