package org.toannguyen.controllers;

import org.springframework.web.bind.annotation.*;
import org.toannguyen.models.Product;
import org.toannguyen.repositories.ProductRepository;

import java.util.List;
import java.util.Optional;

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
    Product replaceEmployee(@RequestBody Product newProduct, @PathVariable Long id) {

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
    void deleteEmployee(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
