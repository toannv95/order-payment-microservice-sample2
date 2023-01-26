package org.toannguyen.services;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.toannguyen.Order;

import java.util.Random;
import java.util.concurrent.Executor;

@Service
public class OrderGeneratorService {

    private static Random RAND = new Random();
    private Executor executor;
    private KafkaTemplate<Long, Order> template;

    public OrderGeneratorService(Executor executor, KafkaTemplate<Long, Order> template) {
        this.executor = executor;
        this.template = template;
    }

    @Async
    public void generate(Long number) {
        for (int i = 0; i <= number; i++) {
            int x = RAND.nextInt(5) + 1;
            Order o = new Order();
            o.setCustomerId(RAND.nextLong(100) + 1);
            o.setProductId(RAND.nextLong(100));
            o.setStatus("NEW");
            o.setPrice(100 * x);
            o.setProductCount(x);
            template.send("orders", o.getId(), o);
        }
    }
}
