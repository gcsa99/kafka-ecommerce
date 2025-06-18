package dev.raulens.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.io.Closeable;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;
import java.util.regex.Pattern;

public class KafkaService<T> implements Closeable {
    private final Map<String, String> properties;
    private final KafkaConsumer<String, T> consumer;
    private final ConsumerFunction parse;
    private final Class<T> type;
    KafkaService(String group, Pattern topic, ConsumerFunction parse, Class<T> type, Map<String, String> properties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<String, T>((Map<String, Object>) getProperties(group, type, properties));
        this.type = type;
        this.properties = properties;
        consumer.subscribe(topic);
    }

    public KafkaService(String group, String topic, ConsumerFunction parse, Class<T> type, Map<String, String> properties) {
        this.parse = parse;
        this.consumer = new KafkaConsumer<String, T>((Map<String, Object>) getProperties(group, type, properties));
        this.type = type;
        this.properties = properties;
        consumer.subscribe(List.of(topic));
    }

    @Override
    public void close() {
        consumer.close();
    }

    public void run() {
        while (true) {
            var records = consumer.poll(Duration.ofMillis(100));
            if (!records.isEmpty()) {
                System.out.println("Found " + records.count() + " records");
                for (var record : records) {
                    parse.consume(record);
                }
            }
        }
    }

    private Object getProperties(String group, Class<T> type, Map<String, String> overrideProperties) {
        var properties = new Properties();
        properties.setProperty(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.24.146.36:9092");
        properties.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, GsonDeserializer.class.getName());
        properties.setProperty(ConsumerConfig.GROUP_ID_CONFIG, group);
        properties.setProperty(ConsumerConfig.CLIENT_ID_CONFIG, UUID.randomUUID().toString());
        properties.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1");
        properties.setProperty(GsonDeserializer.TYPE_CONFIG, type.getName());
        properties.putAll(overrideProperties);
        return properties;
    }
}
