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

    public Bid(BigDecimal value, LocalDateTime time, Integer lotId, Integer userId) {
        this.value = value;
        this.time = time;
        this.lotId = lotId;
        this.userId = userId;
    }

    public Bid() {
    }

    public Bid(Integer lotId, BigDecimal value) {
        this.value = value;
        this.lotId = lotId;
    }

    public Bid(Integer id, BigDecimal value, LocalDateTime time, Integer lotId, Integer userId) {
        this.setId(id);
        this.value = value;
        this.time = time;
        this.lotId = lotId;
        this.userId = userId;
    }

    @Override
    public Bid buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            BigDecimal value = resultSet.getBigDecimal(2);
            LocalDateTime time = resultSet.getTimestamp(3).toLocalDateTime();
            int lotId = resultSet.getInt(4);
            int userId = resultSet.getInt(5);

            return new Bid(id, value, time, lotId, userId);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "Ставка величиной " + value
                + ". Объявлена " + time.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
                + " (id пользователя - " + userId
                + ", id лота - " + lotId + ")";
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
