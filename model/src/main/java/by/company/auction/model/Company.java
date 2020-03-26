package by.company.auction.model;

public class Company extends Base {
    private String name;

    private Integer vendorId;

    @Override
    public String toString() {
        return super.toString() + ". " + name;
    }

    public Company(String name, Integer vendorId) {
        this.name = name;
        this.vendorId = vendorId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getVendorId() {
        return vendorId;
    }

    public void setVendorId(Integer vendorId) {
        this.vendorId = vendorId;
    }
}
