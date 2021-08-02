package com.tamazian.entity;

import com.tamazian.solution.util.ConnectionManager;

import java.io.BufferedReader;
import java.io.FileReader;
import java.sql.Connection;

import static java.lang.Integer.parseInt;

public class Product {
    private static final Product INSTANCE = new Product();

    public static Product getINSTANCE() {
        return INSTANCE;
    }

    public static void loadProducts() {
        String filePath = "src/main/resources/products.csv";

        int batchSize = 20;

        Connection connection = null;


        try {
            connection = ConnectionManager.get();
            connection.setAutoCommit(false);

            String sql = """
                    INSERT INTO products(ID,NAME,PRICE_PER_UNIT) VALUES(?,?,?)
                    """;

            var statement = connection.prepareStatement(sql);

            BufferedReader lineReader = new BufferedReader(new FileReader(filePath));

            String lineText = null;
            int count = 0;

            lineReader.readLine();
            while ((lineText = lineReader.readLine()) != null) {
                String[] data = lineText.split(",");

                String id = data[0];
                String name = data[1];
                String price = data[2];

                statement.setString(1, id);
                statement.setString(2, name);
                statement.setInt(3, parseInt(price));
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
