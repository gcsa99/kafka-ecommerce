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
}
