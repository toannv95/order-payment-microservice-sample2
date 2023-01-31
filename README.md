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
1. Go to docker folder, then run command: `docker compose up`

# How to call API
1. Import api collection json file `order-payment-stock-service.postman_collection.json` in postman folder to `postman application`
2. API list:
- `localhost:8081/products/generate/{number}`: Generate seed data for stock-service
  + Example: `localhost:8081/products/generate/100`: product records
- GET: `localhost:8081/products`: Get all products
- GET: `localhost:8081/products/{id}`: Get product with id = `{id}`
- POST: `localhost:8081/products`: Post 1 product record into database
- PUT: `localhost:8081/products/{id}`: Update product record with id = `{id}` on database

- `localhost:8082/customers/generate/{number}`: Generate seed data for payment-service
- GET: `localhost:8082/customers`: Get all products
- GET: `localhost:8082/customers/{id}`: Get customer with id = `{id}`
- POST: `localhost:8082/customers`: Post 1 customer record into database
- PUT: `localhost:8082/customer`: Update customer record with id = `{id}` on database

- `localhost:8080/orders/generate/{number}`: Generate orders
- GET: `localhost:8080/orders`: Get all orders
- POST: `localhost:8080/orders`: Post 1 order record into database
- GET: `localhost:8080/orders/stream`: Get all orders from Kafka stream
