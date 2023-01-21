package org.toannguyen.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.toannguyen.models.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
