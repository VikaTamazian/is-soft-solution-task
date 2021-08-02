package com.tamazian.solution;

import com.tamazian.entity.Order;
import com.tamazian.entity.OrderItems;
import com.tamazian.entity.Product;


public class Runner {
    public static void main(String[] args) {
        var order = Order.getINSTANCE();
        var product = Product.getINSTANCE();
        var orderItems = OrderItems.getINSTANCE();
        var finder = Finder.getINSTANCE();

//        order.loadOrders();
//        product.loadProducts();
//        orderItems.loadOrderItems();

        finder.findItem();

    }


}
