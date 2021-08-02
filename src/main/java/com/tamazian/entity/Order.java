package com.tamazian.entity;

import com.tamazian.solution.util.ConnectionManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Order {

    private static final Order INSTANCE = new Order();

    public static Order getINSTANCE() {
        return INSTANCE;
    }

    public static void loadOrders() {
        String filePath = "src/main/resources/orders.csv";

        int batchSize = 20;

        Connection connection = null;

        try {
            connection = ConnectionManager.get();
            connection.setAutoCommit(false);

            String sql = """
                    INSERT INTO orders(ID,DATE_TIME) VALUES(?,?)
                    """;

            var statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));

            String lineText = null;
            int count = 0;

            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String id = data[0];
                String dataTime = data[1];

                LocalDateTime localDateTime = LocalDateTime.parse(dataTime);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String timestamp = localDateTime.format(formatter);

                statement.setString(1, id);
                statement.setTimestamp(2, Timestamp.valueOf(timestamp));
                statement.addBatch();
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("Data has been loaded successfully.");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}
