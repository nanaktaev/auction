package by.company.auction.model;

import by.company.auction.annotaitions.TableName;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@TableName("Lots")
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

    public Lot(Integer id, String title, String description, BigDecimal price, BigDecimal priceStart, BigDecimal step, LocalDateTime opened, LocalDateTime closes, Integer categoryId, Integer vendorId, Integer townId) {
        this.setId(id);
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
    }

    public Lot(String title, String description, BigDecimal price, BigDecimal priceStart, BigDecimal step, LocalDateTime opened, LocalDateTime closes, Integer categoryId, Integer vendorId, Integer townId) {
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
    public Lot buildFromResultSet(ResultSet resultSet) {
        try {
            int id = resultSet.getInt(1);
            String title = resultSet.getString(2);
            String description = resultSet.getString(3);
            BigDecimal price = resultSet.getBigDecimal(4);
            BigDecimal priceStart = resultSet.getBigDecimal(5);
            BigDecimal step = resultSet.getBigDecimal(6);
            LocalDateTime opened = resultSet.getTimestamp(7).toLocalDateTime();
            LocalDateTime closes = resultSet.getTimestamp(8).toLocalDateTime();
            int categoryId = resultSet.getInt(9);
            int companyId = resultSet.getInt(10);
            int townId = resultSet.getInt(11);

            return new Lot(id, title, description, price, priceStart, step, opened, closes, categoryId, companyId, townId);

        } catch (
                SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return null;
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
                ".";
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

}
