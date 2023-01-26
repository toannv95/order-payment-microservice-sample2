package org.toannguyen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.toannguyen.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
