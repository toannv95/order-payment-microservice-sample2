package org.toannguyen.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.toannguyen.Order;
import org.toannguyen.models.Customer;
import org.toannguyen.repositories.CustomerRepository;

@Service
@Slf4j
public class OrderManageService {

    @Autowired
    private CustomerRepository repository;

    @Autowired
    private KafkaTemplate<Long, Order> template;

    private static final String SOURCE = "payment";

    public void reserve(Order order) {
        Customer customer = repository.findById(order.getCustomerId()).get();
        log.info("Found: {}", customer);
        if (order.getPrice() < customer.getAmountAvailable()) {
            order.setStatus("ACCEPT");
            customer.setAmountReserved(customer.getAmountReserved() + order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() - order.getPrice());
        } else {
            order.setStatus("REJECT");
        }
        order.setSource(SOURCE);
        repository.save(customer);
        template.send("payment-orders", order.getId(), order);
        log.info("Sent: {}", order);
    }

    public void confirm(Order order) {
        Customer customer = repository.findById(order.getCustomerId()).get();
        log.info("Found: {}", customer);
        if (order.getStatus().equals("CONFIRMED")) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            repository.save(customer);
        } else if (order.getStatus().equals("ROLLBACK") && !order.getSource().equals(SOURCE)) {
            customer.setAmountReserved(customer.getAmountReserved() - order.getPrice());
            customer.setAmountAvailable(customer.getAmountAvailable() + order.getPrice());
            repository.save(customer);
        }

    }
}
