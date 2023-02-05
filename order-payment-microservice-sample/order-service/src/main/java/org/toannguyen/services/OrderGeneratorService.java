package org.toannguyen.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.toannguyen.Order;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderGeneratorService {
    private static final Logger LOG = LoggerFactory.getLogger(OrderGeneratorService.class);
    @Autowired
    private KafkaTemplate<Long, Order> template;

    @Async
    public void generate(Long number) {
        Random RAND = new Random();
        AtomicLong id = new AtomicLong();

        for (int i = 0; i <= number; i++) {
            int x = RAND.nextInt(5) + 1;
            Order o = new Order();
            o.setCustomerId(RAND.nextLong(100) + 1);
            o.setProductId(RAND.nextLong(100) + 1);
            o.setStatus("NEW");
            o.setPrice(100 * x);
            o.setProductCount(x);
            template.send("orders", o.getId(), o);
            LOG.info("Sent: {}", o);
        }
    }
}
