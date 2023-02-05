package org.toannguyen.services;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.springframework.stereotype.Service;
import org.toannguyen.Order;
import org.toannguyen.repositories.OrderRepository;

import java.time.Duration;

@Service
@EnableKafkaStreams
public class OrderService {
    private final Logger LOG = LoggerFactory.getLogger(OrderService.class);

    @Bean
    public NewTopic orders() {
        return TopicBuilder.name("orders")
                .partitions(3)
                .build();
    }

    @Autowired
    OrderRepository repository;

    @Bean
    public KStream<Long, Order> stream(StreamsBuilder builder) {
        try{
            JsonSerde<Order> orderSerde = new JsonSerde<>(Order.class);
            KeyValueBytesStoreSupplier store =
                    Stores.persistentKeyValueStore("orders");
            KStream<Long, Order> stream = builder
                    .stream("payment-orders", Consumed.with(Serdes.Long(), orderSerde));
            stream.join(
                            builder.stream("stock-orders"),
                            this::confirm,
                            JoinWindows.of(Duration.ofSeconds(10)),
                            StreamJoined.with(Serdes.Long(), orderSerde, orderSerde))
                    .peek((k, o) -> {
                        LOG.info("Output: {}", o);
                        repository.save(o);
                    })
                    .to("orders");
            return stream;
        }
        catch (Exception ex){
            LOG.info("Exception: {}", ex.getMessage());
            return null;
        }
    }

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

    @Bean
    public KTable<Long, Order> storeTable(StreamsBuilder builder) {
        KeyValueBytesStoreSupplier store =
                Stores.persistentKeyValueStore("orders");
        JsonSerde<Order> orderSerde = new JsonSerde<>(Order.class);
        KStream<Long, Order> stream = builder
                .stream("orders", Consumed.with(Serdes.Long(), orderSerde));
        return stream.toTable(Materialized.<Long, Order>as(store)
                .withKeySerde(Serdes.Long())
                .withValueSerde(orderSerde));
    }
}
