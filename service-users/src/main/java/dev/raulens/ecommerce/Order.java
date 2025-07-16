package dev.raulens.ecommerce;

import java.math.BigDecimal;

public class Order {
    private final String UserId, orderId;
    private final BigDecimal amount;
    private final String email;

    public Order(String userId, String orderId, BigDecimal amount, String email) {
        this.UserId = userId;
        this.orderId = orderId;
        this.amount = amount;
        this.email = email;
    }

    public String getUserId() {
        return UserId;
    }

    public String getEmail() {
        return email;
    }
}
