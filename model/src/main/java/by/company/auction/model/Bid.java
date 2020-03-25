package by.company.auction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Bid extends Base {
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

    public Bid(Integer lotId, BigDecimal value) {
        this.value = value;
        this.lotId = lotId;
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
