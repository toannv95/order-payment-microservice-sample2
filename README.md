# This is my sample project when i learning about microservice
# Description
I use 3 microservices: 
`order-service` to create orders. This services sends messages to Kafka. It uses the KafkaTemplate.
`stock-service` to store and check amount product in inventory on the order
`payment-service` to store and check customer balance on the order

# Technologies
Spring Boot
Spring Kafka
Kafka
Zookeeper
confluent cloud

# How To Run
Run `stock-service`, `payment-service` and `order-service`
You can use postman sample api for this project:
https://drive.google.com/file/d/15MthwjX5nWHSRZlYZhaq-3JhFQxqgOB8/view?usp=sharing
