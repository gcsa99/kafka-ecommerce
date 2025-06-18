package dev.raulens.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import java.util.Map;

public class FraudDetectorService {
    public static void main(String[] args) {
        var fraudService = new FraudDetectorService();
        try (var service = new KafkaService<Order>(FraudDetectorService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", fraudService::parse, Order.class, Map.of())) {
            service.run();
        }
    }


    private void parse(ConsumerRecord<String, Order> record) {
        System.out.println("Processing new order:");
        System.out.println("key: " + record.key() + " value: " + record.value());
        System.out.println("partition: " + record.partition() + " offset: " + record.offset());
        System.out.println("--------------------");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done");
    }

}
