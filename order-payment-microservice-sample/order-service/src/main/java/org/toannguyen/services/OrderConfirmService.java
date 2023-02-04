package org.toannguyen.services;

import org.springframework.stereotype.Service;
import org.toannguyen.Order;

@Service
public class OrderConfirmService {
    public Order confirm(Order orderPayment, Order orderStock) {
        Order o = new Order();
        o.setCustomerId(orderPayment.getCustomerId());
        o.setProductId(orderPayment.getProductId());
        o.setProductCount(orderPayment.getProductCount());
        o.setPrice(orderPayment.getPrice());

        if (orderPayment.getStatus().equals("ACCEPT") &&
                orderStock.getStatus().equals("ACCEPT")) {
            o.setStatus("CONFIRMED");
        } else if (orderPayment.getStatus().equals("REJECT") &&
                orderStock.getStatus().equals("REJECT")) {
            o.setStatus("REJECTED");
        } else if (orderPayment.getStatus().equals("REJECT") ||
                orderStock.getStatus().equals("REJECT")) {
            String source = orderPayment.getStatus().equals("REJECT")
                    ? "PAYMENT" : "STOCK";
            o.setStatus("ROLLBACK");
            o.setSource(source);
        }
        return o;
    }
}
