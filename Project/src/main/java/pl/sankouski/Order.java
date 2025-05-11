package pl.sankouski;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private String id;
    private BigDecimal value;
    public List<String> promotions = new ArrayList<>();

    public Order() {
    }

    public Order(String id, BigDecimal value, List<String> promotions) {
        this.id = id;
        this.value = value;
        this.promotions = promotions;
    }

    public String getId() {
        return id;
    }

    public Order setId(String id) {
        this.id = id;
        return this;
    }

    public BigDecimal getValue() {
        return value;
    }

    public Order setValue(BigDecimal value) {
        this.value = value;
        return this;
    }

    public List<String> getPromotions() {
        return promotions;
    }

    public Order setPromotions(List<String> promotions) {
        this.promotions = promotions;
        return this;
    }
}
