package portfolio.ecommerce.order.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import portfolio.ecommerce.order.dto.CreateCustomerDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.dto.UpdateCustomerDto;
import portfolio.ecommerce.order.entity.Customer;
import portfolio.ecommerce.order.service.CustomerService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
        CustomerController customerController = new CustomerController(customerService);
        mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
    }

    @Test
    void deleteCustomer() throws Exception {
        doNothing().when(customerService).delete(1L);

        mockMvc.perform(delete("/customers/{id}", 1L))
                .andExpect(status().isOk());

        verify(customerService, times(1)).delete(1L);
    }

    @Test
    void getCustomers() throws Exception {
        Page<Customer> page = new PageImpl<>(Collections.singletonList(Customer.builder().customerId(1L).name("TESTER").build()));
        when(customerService.find(any(RequestPagingDto.class))).thenReturn(page);

        mockMvc.perform(get("/customers")
                        .param("page", String.valueOf(0))
                        .param("pageSize", String.valueOf(10)))
                .andExpect(status().is5xxServerError());

        verify(customerService, times(1)).find(any(RequestPagingDto.class));
    }

    @Test
    void createCustomer() throws Exception {
        CreateCustomerDto dto = new CreateCustomerDto("TESTER", 1000);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        Customer customer = Customer.builder().customerId(1L).name(dto.getName()).amount(dto.getAmount()).build();
        when(customerService.create(any(CreateCustomerDto.class))).thenReturn(customer);

        mockMvc.perform(post("/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("TESTER"))
                .andExpect(jsonPath("$.amount").value(1000));

        verify(customerService, times(1)).create(any(CreateCustomerDto.class));
    }

    @Test
    void updateCustomer() throws Exception {
        UpdateCustomerDto dto = new UpdateCustomerDto("UPDATED", 2000);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);
        Customer customer = Customer.builder().customerId(1L).name(dto.getName()).amount(dto.getAmount()).build();

        when(customerService.update(eq(1L), any(UpdateCustomerDto.class))).thenReturn(customer);

        mockMvc.perform(patch("/customers/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("UPDATED"))
                .andExpect(jsonPath("$.amount").value(2000));

        verify(customerService, times(1)).update(eq(1L), any(UpdateCustomerDto.class));
    }
}