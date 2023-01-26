package org.toannguyen.kafkaconfigs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.toannguyen.Order;
import org.toannguyen.services.OrderManageService;
@Slf4j
@Component
public class StockKafkaListener {
    @Autowired
    private OrderManageService orderManageService;

    @KafkaListener(id = "orders", topics = "orders", groupId = "stock")
    public void onEvent(@Payload Order o) {
        log.info("Received: {}" , o);
        if (o.getStatus().equals("NEW"))
            orderManageService.reserve(o);
        else
            orderManageService.confirm(o);
    }
}
