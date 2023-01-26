package org.toannguyen.kafkaconfigs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicCreateConfig {
    @Bean
    public NewTopic orders() {
        return TopicBuilder.name("orders")
                .partitions(3)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic paymentTopic() {
        return TopicBuilder.name("payment-orders")
                .partitions(3)
                .replicas(1)
                .compact()
                .build();
    }

    @Bean
    public NewTopic stockTopic() {
        return TopicBuilder.name("stock-orders")
                .partitions(3)
                .replicas(1)
                .compact()
                .build();
    }
}
