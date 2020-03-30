package by.company.auction.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Lot extends BaseEntity {
    private String title;
    private String description;
    private BigDecimal price;
    private BigDecimal priceStart;
    private BigDecimal step;
    private LocalDateTime opened;
    private LocalDateTime closes;

    private Integer categoryId;
    private Integer vendorId;
    private Integer townId;
    private List<Integer> bidIds;
    private List<Integer> userIds;

    public boolean isExpired() {
        return LocalDateTime.now().isAfter(closes);
    }

    public boolean isBurning() {
        return !isExpired() && LocalDateTime.now().plusMinutes(3).isAfter(closes);
    }

    public boolean isBidValueEnough(BigDecimal value) {
        return value.compareTo(price.add(step)) >= 0;
    }

    public Lot() {
    }

    public Lot(String title, String description, BigDecimal price, BigDecimal priceStart, BigDecimal step, LocalDateTime opened, LocalDateTime closes, Integer categoryId, Integer vendorId, Integer townId, List<Integer> bidIds, List<Integer> userIds) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.priceStart = priceStart;
        this.step = step;
        this.opened = opened;
        this.closes = closes;
        this.categoryId = categoryId;
        this.vendorId = vendorId;
        this.townId = townId;
        this.bidIds = bidIds;
        this.userIds = userIds;
    }

    public Lot(String title, String description, BigDecimal priceStart, BigDecimal step, LocalDateTime closes, Integer categoryId, Integer townId) {
        this.title = title;
        this.description = description;
        this.priceStart = priceStart;
        this.step = step;
        this.closes = closes;
        this.categoryId = categoryId;
        this.townId = townId;
    }

    @Override
    public String toString() {
        return "Лот №" + super.toString() +
                " - " + title +
                ":\n" + description +
                "\nЦена - " + price +
                " (начальная - " + priceStart +
                "), мин. повышение - " + step +
                "\nОкончание торгов - " + closes.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                ", начало торгов - " + opened.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\nid категории - " + categoryId +
                ", id продавца - " + vendorId +
                ", id города - " + townId +
                "\nid ставок - " + bidIds +
                "\nid пользователей - " + userIds + "\n";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPriceStart() {
        return priceStart;
    }

    public void setPriceStart(BigDecimal priceStart) {
        this.priceStart = priceStart;
    }

    public BigDecimal getStep() {
        return step;
    }

    public void setStep(BigDecimal step) {
        this.step = step;
    }

    public LocalDateTime getOpened() {
        return opened;
    }

    public void setOpened(LocalDateTime opened) {
        this.opened = opened;
    }

    public LocalDateTime getCloses() {
        return closes;
    }

    public void setCloses(LocalDateTime closes) {
        this.closes = closes;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public Integer getTownId() {
        return townId;
    }

    public void setTownId(Integer townId) {
        this.townId = townId;
    }

    public List<Integer> getBidIds() {
        return bidIds;
    }

    public void setBidIds(List<Integer> bidIds) {
        this.bidIds = bidIds;
    }

    public List<Integer> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<Integer> userIds) {
        this.userIds = userIds;
    }
}
