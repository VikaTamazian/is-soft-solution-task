package com.tamazian.solution;

import com.tamazian.solution.util.ConnectionManager;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Finder {

    private static final Finder INSTANCE = new Finder();

    public static Finder getINSTANCE() {
        return INSTANCE;
    }

    public static void findItem() {
        String sql = """
                SELECT p.NAME, (p.PRICE_PER_UNIT * oi.QUANTITY) as sum
                FROM products p
                         JOIN order_items oi on p.ID = oi.PRODUCT_ID
                         JOIN orders o on o.ID = oi.ORDER_ID
                WHERE o.DATE_TIME BETWEEN '2021-01-21 00:00:00' and '2021-01-21 23:59:59'
                GROUP BY p.NAME
                ORDER BY sum DESC
                LIMIT 1
                """;

        try (var connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            var resultSet = statement.executeQuery();
            if (resultSet.next()) {
                var name = resultSet.getString("NAME");
                System.out.println(name);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

}
