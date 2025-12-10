package walter.duncan.vinylwebshop.entities;

public class StockEntity extends BaseEntity {
    private String condition;
    private double price;

    public String getCondition() {
        return this.condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}