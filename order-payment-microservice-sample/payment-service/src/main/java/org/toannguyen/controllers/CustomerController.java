package org.toannguyen.controllers;

import com.github.javafaker.Faker;
import org.springframework.web.bind.annotation.*;
import org.toannguyen.models.Customer;
import org.toannguyen.repositories.CustomerRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("customers")
public class CustomerController {
    private CustomerRepository repository;

    CustomerController(CustomerRepository repository){
        this.repository = repository;
    }

    @PostMapping()
    Customer newCustomer(@RequestBody Customer newCustomer) {
        return repository.save(newCustomer);
    }

    @GetMapping
    public List<Customer> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Optional<Customer> getCustomerById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PutMapping("{id}")
    Customer replaceCustomer(@RequestBody Customer newCustomer, @PathVariable Long id) {

        return repository.findById(id)
                .map(customer -> {
                    customer.setFirstName(newCustomer.getFirstName());
                    customer.setLastName(newCustomer.getLastName());
                    customer.setAddress(newCustomer.getAddress());
                    customer.setAmountAvailable(newCustomer.getAmountAvailable());
                    customer.setAmountReserved(newCustomer.getAmountReserved());
                    return repository.save(customer);
                })
                .orElseGet(() -> {
                    return repository.save(newCustomer);
                });
    }

    @DeleteMapping("{id}")
    void deleteCustomer(@PathVariable Long id) {
        repository.deleteById(id);
    }

    @PostMapping("generate/{number}")
    void autoGenerate(@PathVariable Long number) {
        Random r = new Random();
        Faker faker = new Faker();
        for (int i = 1; i <= number; i++) {
            Customer customer = new Customer();
            customer.setLastName(faker.name().lastName());
            customer.setFirstName(faker.name().firstName());
            customer.setAddress(faker.address().fullAddress());
            customer.setAmountAvailable(r.nextInt(1000));
            customer.setAmountReserved(0);
            repository.save(customer);
        }
    }
}
