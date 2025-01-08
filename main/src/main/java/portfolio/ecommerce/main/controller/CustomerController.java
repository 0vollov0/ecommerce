package portfolio.ecommerce.main.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import portfolio.ecommerce.main.dto.CreateCustomerDTO;
import portfolio.ecommerce.main.entity.Customer;
import portfolio.ecommerce.main.service.CustomerService;

@RequestMapping("/customers")
@RestController
public class CustomerController {
    @Autowired
    private CustomerService customerService;

    @PostMapping
    public Customer create(@Valid CreateCustomerDTO dto) {
        return customerService.create(dto);
    }

    @GetMapping
    public Iterable<Customer> get() {
        return customerService.find();
    }
}
