package portfolio.ecommerce.order.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import portfolio.ecommerce.order.dto.CreateCustomerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateCustomerDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.repository.CustomerRepository;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        customerService = new CustomerService(customerRepository);
    }

    @Test
    void createCustomer() {
        CreateCustomerDto dto = new CreateCustomerDto("TEST_CUSTOMER", 1000);
        Customer customer = Customer.builder().name(dto.getName()).amount(dto.getAmount()).build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer savedCustomer = customerService.create(dto);

        assertNotNull(savedCustomer);
        assertEquals(dto.getName(), savedCustomer.getName());
        assertEquals(dto.getAmount(), savedCustomer.getAmount());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void findCustomerById() {
        Customer customer = Customer.builder().customerId(1L).name("TEST_CUSTOMER").amount(1000).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Optional<Customer> foundCustomer = customerService.findById(1L);

        assertTrue(foundCustomer.isPresent());
        assertEquals("TEST_CUSTOMER", foundCustomer.get().getName());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void findCustomers() {
        RequestPagingDto dto = new RequestPagingDto(0, 10);
        Page<Customer> page = new PageImpl<>(Collections.singletonList(new Customer()));

        when(customerRepository.findAllByDeleted(eq(false), any(PageRequest.class))).thenReturn(page);

        Page<Customer> result = customerService.find(dto);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        verify(customerRepository, times(1)).findAllByDeleted(eq(false), any(PageRequest.class));
    }

    @Test
    void updateCustomer() {
        UpdateCustomerDto dto = new UpdateCustomerDto("TEST_CUSTOMER_2", 2000);
        Customer customer = Customer.builder().customerId(1L).name("TEST_CUSTOMER").amount(1000).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        Customer updatedCustomer = customerService.update(1L, dto);

        assertNotNull(updatedCustomer);
        assertEquals("TEST_CUSTOMER_2", updatedCustomer.getName());
        assertEquals(2000, updatedCustomer.getAmount());
        verify(customerRepository, times(1)).findById(1L);
    }

    @Test
    void deleteCustomer() {
        Customer customer = Customer.builder().customerId(1L).name("TEST_CUSTOMER").amount(1000).deleted(false).build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        customerService.delete(1L);

        assertTrue(customer.isDeleted());
        verify(customerRepository, times(1)).findById(1L);
    }
}