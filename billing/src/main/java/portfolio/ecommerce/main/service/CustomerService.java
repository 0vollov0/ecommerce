package portfolio.ecommerce.main.service;

import org.springframework.beans.factory.annotation.Autowired;
import portfolio.ecommerce.main.dto.CreateCustomerDTO;
import portfolio.ecommerce.main.entity.Customer;
import portfolio.ecommerce.main.repository.CustomerRepository;

public class CustomerService {
    @Autowired
    private CustomerRepository customerRepository;

    public Customer create(CreateCustomerDTO dto) {
        Customer customer = toEntity(dto);
        return customerRepository.save(customer);
    }

    private Customer toEntity(CreateCustomerDTO dto) {
        return Customer.builder().name(dto.getName()).build();
    }
}
