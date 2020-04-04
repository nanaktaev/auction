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
    private Integer companyId;
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

    @Override
    public Lot buildFromResultSet(ResultSet resultSet) throws SQLException {

        Lot lot = new Lot();
        lot.setId(resultSet.getInt(1));
        lot.setTitle(resultSet.getString(2));
        lot.setDescription(resultSet.getString(3));
        lot.setPrice(resultSet.getBigDecimal(4));
        lot.setPriceStart(resultSet.getBigDecimal(5));
        lot.setStep(resultSet.getBigDecimal(6));
        lot.setOpened(resultSet.getTimestamp(7).toLocalDateTime());
        lot.setCloses(resultSet.getTimestamp(8).toLocalDateTime());
        lot.setCategoryId(resultSet.getInt(9));
        lot.setCompanyId(resultSet.getInt(10));
        lot.setTownId(resultSet.getInt(11));

        return lot;
    }

    @Override
    public String toString() {
        return "Лот №" + super.getId() +
                " - " + title +
                ":\n" + description +
                "\nЦена - " + price +
                " (начальная - " + priceStart +
                "), мин. повышение - " + step +
                "\nОкончание торгов - " + closes.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                ", начало торгов - " + opened.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\nid категории - " + categoryId +
                ", id компании - " + companyId +
                ", id города - " + townId +
                ".\n";
    }

    public Integer getId() {
        return super.getId();
    }

    public void setId(Integer id) {
        super.setId(id);
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

    public int getCompanyId() {
        return companyId;
    }

    public void setCompanyId(int companyId) {
        this.companyId = companyId;
    }

    public Integer getTownId() {
        return townId;
    }

    public void setTownId(Integer townId) {
        this.townId = townId;
    }

}
