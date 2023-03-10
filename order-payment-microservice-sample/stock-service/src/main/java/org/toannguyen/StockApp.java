package org.toannguyen;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerde;
import org.toannguyen.models.Product;
import org.toannguyen.repositories.ProductRepository;

import java.util.Random;

@SpringBootApplication
@EnableKafkaStreams
public class StockApp {

    private static final Logger LOG = LoggerFactory.getLogger(StockApp.class);

    public static void main(String[] args) {
        SpringApplication.run(StockApp.class, args);
    }

    @Autowired
    private KafkaTemplate<Long, Order> template;
    @Autowired
    ProductRepository repository;
    private Random random = new Random();

    @Bean
    public KStream<Long, Order> stream(StreamsBuilder builder) {
        JsonSerde<Order> orderSerde = new JsonSerde<>(Order.class);
        JsonSerde<Product> rsvSerde = new JsonSerde<>(Product.class);
        KStream<Long, Order> stream = builder
                .stream("orders", Consumed.with(Serdes.Long(), orderSerde))
                .peek((k, order) -> LOG.info("New: {}", order));

        KeyValueBytesStoreSupplier stockOrderStoreSupplier =
                Stores.persistentKeyValueStore("stock-orders");

        Aggregator<Long, Order, Product> aggrSrv = (id, order, product) -> {
            try{
                product = repository.findById(order.getProductId()).get();
                switch (order.getStatus()) {
                    case "CONFIRMED" -> product.setReservedItems(product.getReservedItems() - order.getProductCount());
                    case "ROLLBACK" -> {
                        if (!order.getSource().equals("STOCK")) {
                            product.setAvailableItems(product.getAvailableItems() + order.getProductCount());
                            product.setReservedItems(product.getReservedItems() - order.getProductCount());
                        }
                    }
                    case "NEW" -> {
                        if (order.getProductCount() <= product.getAvailableItems()) {
                            product.setAvailableItems(product.getAvailableItems() - order.getProductCount());
                            product.setReservedItems(product.getReservedItems() + order.getProductCount());
                            order.setStatus("ACCEPT");
                        } else {
                            order.setStatus("REJECT");
                        }
                        // Todo: Need to handle exception
                        template.send("stock-orders", order.getId(), order);
                    }
                }
                repository.save(product);
                LOG.info("{}", product);
                return product;
            }
            catch (Exception ex){
                LOG.info("Exception: {}", ex.getMessage());
                return null;
            }
        };

        stream.selectKey((k, v) -> v.getProductId())
                .groupByKey(Grouped.with(Serdes.Long(), orderSerde))
                .aggregate(() -> new Product(), aggrSrv,
                        Materialized.<Long, Product>as(stockOrderStoreSupplier)
                                .withKeySerde(Serdes.Long())
                                .withValueSerde(rsvSerde))
                .toStream()
                .peek((k, trx) -> LOG.info("Commit: {}", trx));

        return stream;
    }
}