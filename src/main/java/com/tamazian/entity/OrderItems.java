package com.tamazian.entity;

import com.tamazian.solution.util.ConnectionManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;

import static java.lang.Integer.parseInt;

public class OrderItems {
    private static final OrderItems INSTANCE = new OrderItems();

    public static OrderItems getINSTANCE() {
        return INSTANCE;
    }

    public static void loadOrderItems() {
        String filePath = "src/main/resources/order_items.csv";

        int batchSize = 20;

        Connection connection = null;


        try {
            connection = ConnectionManager.get();
            connection.setAutoCommit(false);

            String sql = """
                    INSERT INTO order_items(ORDER_ID,PRODUCT_ID,QUANTITY) VALUES(?,?,?)
                    """;

            var statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));

            String lineText = null;
            int count = 0;

            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String orderId = data[0];
                String productId = data[1];
                String quantity = data[2];

                statement.setString(1, orderId);
                statement.setString(2, productId);
                statement.setInt(3, parseInt(quantity));
                statement.addBatch();
                if (count % batchSize == 0) {
                    statement.executeBatch();
                }
            }
            lineReader.close();
            statement.executeBatch();
            connection.commit();
            connection.close();
            System.out.println("Data has been inserted successfully.");

        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

}
