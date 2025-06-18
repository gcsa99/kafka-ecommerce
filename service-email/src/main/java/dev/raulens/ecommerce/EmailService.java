package dev.raulens.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.Map;

public class EmailService {
    public static void main(String[] args) {
        var emailService = new EmailService();
        try (var service = new KafkaService<Email>(EmailService.class.getSimpleName(), "ECOMMERCE_SEND_EMAIL", emailService::parse, Email.class, Map.of())) {
            service.run();
        }
    }

    private void parse(ConsumerRecord<String, Email> record) {
        System.out.println("Sending email:");
        System.out.println("key: " + record.key() + " value: " + record.value());
        System.out.println("partition: " + record.partition() + " offset: " + record.offset());
        System.out.println("--------------------");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Email sent");
    }
}
