package dev.raulens.ecommerce;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class CreateUserService {

    private final Connection connection;

    CreateUserService() throws SQLException {
        String url = "jdbc:sqlite:target/users_database.db";
        connection = DriverManager.getConnection(url);
        try {
            connection.createStatement().execute("CREATE TABLE Users( uuid varchar(200) primary key, email varchar(200))");
        }catch(SQLException ex){
            System.out.println("Table already exists");
            ex.printStackTrace();
        }
    }
    public static void main(String[] args) throws SQLException {
        var createUserService = new CreateUserService();
        try (var service = new KafkaService<>(CreateUserService.class.getSimpleName(), "ECOMMERCE_NEW_ORDER", createUserService::parse, Order.class, Map.of())) {
            service.run();
        }
    }
    private final KafkaDispatcher<Order> orderDispatcher = new KafkaDispatcher<>();

    private void parse(ConsumerRecord<String, Order> record) throws SQLException {
        System.out.println("--------------------");
        System.out.println("Processing new order, checking for new user:");
        System.out.println("key: " + record.key());
        System.out.println("value: " + record.value());
        System.out.println("partition: " + record.partition());
        System.out.println("offset: " + record.offset());
        var order = record.value();
        if (isNewUser(order.getEmail())) {
            insertNewUser(order.getUserId() ,order.getEmail());

        }else{
            System.out.println("User already exists");
        }
        System.out.println("--------------------");

    }

    private void insertNewUser(String uuid, String email) throws SQLException {
        System.out.println("Creating new user");
        System.out.println("uuid " + uuid);
        System.out.println("email " + email);
        var insert = connection.prepareStatement("INSERT INTO Users (uuid, email) VALUES (?,?) ");
        insert.setString(1, uuid);
        insert.setString(2, email);
        insert.execute();
        System.out.println("User created uuid");

    }

    private boolean isNewUser(String email) throws SQLException {
        System.out.println("Checking if user exists");
        var exists = connection.prepareStatement("SELECT uuid FROM Users WHERE email = ? limit 1");
        exists.setString(1, email);
        var results = exists.executeQuery();
        System.out.println("Results: " + results );
        return !results.next();
    }
}
