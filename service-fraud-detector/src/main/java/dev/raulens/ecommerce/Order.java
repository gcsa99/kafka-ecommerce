package dev.raulens.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String UserId, orderId;
    private final BigDecimal amount;

    public Order(String userId, String orderId, BigDecimal amount) {
        this.UserId = userId;
        this.orderId = orderId;
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Order: { User: " + UserId + ", OrderId: " + orderId + ", Amount: " + amount + " }";
    }

    public String getUserId() {
        return UserId;
    }
}
