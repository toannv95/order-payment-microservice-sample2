package org.toannguyen.controllers;

import org.springframework.web.bind.annotation.*;
import org.toannguyen.models.Product;
import org.toannguyen.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@RestController
@RequestMapping("products")
public class ProductController {
    private ProductRepository repository;

    ProductController(ProductRepository repository){
        this.repository = repository;
    }

    @PostMapping()
    Product newProduct(@RequestBody Product newProduct) {
        return repository.save(newProduct);
    }

    @GetMapping
    public List<Product> getAll() {
        return repository.findAll();
    }

    @GetMapping("{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return repository.findById(id);
    }

    @PutMapping("{id}")
    Product replaceProduct(@RequestBody Product newProduct, @PathVariable Long id) {

        return repository.findById(id)
                .map(product -> {
                    product.setName(newProduct.getName());
                    product.setAvailableItems(newProduct.getAvailableItems());
                    product.setReservedItems(newProduct.getReservedItems());
                    return repository.save(product);
                })
                .orElseGet(() -> {
                    return repository.save(newProduct);
                });
    }

    @DeleteMapping("{id}")
    void deleteProduct(@PathVariable Long id) {
        repository.deleteById(id);
    }
    @PostMapping("generate/{number}")
    void autoGenerate(@PathVariable Long number) {
        Random r = new Random();
        for (int i = 0; i < 50; i++) {

            Product product = new Product();
            product.setName("Product "+i);
            product.setAvailableItems(r.nextInt(1000));
            product.setReservedItems(0);
            repository.save(product);
        }
    };
}
