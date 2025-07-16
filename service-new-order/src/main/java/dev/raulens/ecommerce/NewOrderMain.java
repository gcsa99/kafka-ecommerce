package dev.raulens.ecommerce;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class NewOrderMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        try (var orderDispatcher = new KafkaDispatcher<Order>()) {
            try (var emailDispatcher = new KafkaDispatcher<Email>()) {
                for (int i = 0; i < 20; i++) {
                    var userId = UUID.randomUUID().toString();
                    var orderId = UUID.randomUUID().toString();
                    var amount = new BigDecimal((Math.random() * 10000) + 1);
                    var userEmail = Math.random() + "@email.com";
                    var order = new Order(userId, orderId, amount, userEmail);
                    var email = new Email("Order " + orderId, "Order " + orderId + " was created");
                    orderDispatcher.send("ECOMMERCE_NEW_ORDER", userId, order);

                    emailDispatcher.send("ECOMMERCE_SEND_EMAIL", userId, email);

                }
            }
        }
    }
}