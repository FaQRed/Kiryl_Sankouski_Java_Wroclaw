package pl.sankouski;

import java.math.BigDecimal;

public class PaymentMethod {
    private String id;
    private BigDecimal discount;
    private BigDecimal limit;

    public PaymentMethod() {
    }

    public PaymentMethod(String id, BigDecimal discount, BigDecimal limit) {
        this.id = id;
        this.discount = discount;
        this.limit = limit;
    }

    public String getId() {
        return id;
    }

    public PaymentMethod setId(String id) {
        this.id = id;
        return this;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public PaymentMethod setDiscount(BigDecimal discount) {
        this.discount = discount;
        return this;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public PaymentMethod setLimit(BigDecimal limit) {
        this.limit = limit;
        return this;
    }
}
