# This is my sample project when i learning about microservice
# Description
I use 3 microservices: 
- `order-service` to create orders. This services sends messages to Kafka. It uses the KafkaTemplate.
- `stock-service` to store and check amount product in inventory on the order
- `payment-service` to store and check customer balance on the order

# Technologies
- Spring Boot
- Spring Kafka
- Kafka
- Zookeeper
- PostgreSQL
- confluent cloud

# How To Run
1. Install `postgres` in local machine with port `5432`
2. Open postgres pgadmin, and create 3 databases: `orderdb`, `paymentdb`, `stockdb`
3. Import api collection json file `order-payment-stock-service.postman_collection.json` in postman folder to `postman application`
4. Run `stock-service`, `payment-service` and `order-service`
5. Generate seed data with POST method:
- localhost:8081/products/generate/{number}
- localhost:8082/customers/generate/{number}
- Example: If you wan to create 100 product records, call `localhost:8081/products/generate/100` from postman.
