package dev.raulens.ecommerce;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.Closeable;
import java.util.Properties;
import java.util.concurrent.ExecutionException;

class KafkaDispatcher<T> implements Closeable {
    private final KafkaProducer<String, T> producer;

    KafkaDispatcher() {
        this.producer = new KafkaProducer<>(properties());
    }

    private static Properties properties() {
        var properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "172.24.146.36:9092");
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GsonSerializer.class.getName());
        return properties;
    }

    private static Callback getCallback() {
        return (data, err) -> {
            if (err != null) {
                err.printStackTrace();
                return;
            }
            System.out.println("success: " + data.topic() + "::: partition " + data.partition() + "/ offset " + data.offset() + "::: timestamp " + data.timestamp());
        };
    }

    void send(String topic, String key, T value) throws ExecutionException, InterruptedException {
        var record = new ProducerRecord<String, T>(topic, key, value);
        producer.send(record, getCallback()).get();
    }

    @Override
    public void close() {
        producer.close();
    }
}
