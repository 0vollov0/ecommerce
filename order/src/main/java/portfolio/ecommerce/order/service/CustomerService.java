package portfolio.ecommerce.order.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.dto.CreateCustomerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateCustomerDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.repository.CustomerRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private final CustomerRepository customerRepository;

    public Customer create(CreateCustomerDto dto) {
        return customerRepository.save(Customer.builder().name(dto.getName()).amount(dto.getAmount()).build());
    }

    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    public Page<Customer> find(RequestPagingDto dto) {
        return customerRepository.findAllByDeleted(false, PageRequest.of(dto.getPage(), dto.getPageSize(), Sort.by("createdAt").descending()));
    }

    @Transactional
    public Customer update(Long id, UpdateCustomerDto dto) {
        Customer customer = customerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        customer.setName(dto.getName());
        customer.setAmount(dto.getAmount());
        return customer;
    }

    @Transactional
    public void delete(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(EntityNotFoundException::new);
        customer.setDeleted(true);
    }
}
