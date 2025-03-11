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
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.dto.RequestPagingDto;
import portfolio.ecommerce.order.entity.Order;
import portfolio.ecommerce.order.response.OrderResponse;
import portfolio.ecommerce.order.service.OrderService;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        OrderController orderController = new OrderController(orderService);
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
    }

    @Test
    void order_Success() throws Exception {
        OrderDto dto = new OrderDto(1L, 1L, 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        OrderResponse response = new OrderResponse(true, "Order placed successfully");

        when(orderService.order(any(OrderDto.class))).thenReturn(response);
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true))
                .andExpect(jsonPath("$.message").value("Order placed successfully"));

        verify(orderService, times(1)).order(any(OrderDto.class));
    }

    @Test
    void order_Failure() throws Exception {
        OrderDto dto = new OrderDto(1L, 1L, 1);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(dto);

        OrderResponse response = new OrderResponse(false, "Not enough stock to order");

        when(orderService.order(any(OrderDto.class))).thenReturn(response);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.result").value(false))
                .andExpect(jsonPath("$.message").value("Not enough stock to order"));

        verify(orderService, times(1)).order(any(OrderDto.class));
    }

    @Test
    void getOrders() throws Exception {
        RequestPagingDto dto = new RequestPagingDto(0, 10);
        Page<Order> page = new PageImpl<>(Collections.singletonList(new Order()));

        when(orderService.find(any(RequestPagingDto.class))).thenReturn(any());

        mockMvc.perform(get("/orders")
                        .param("page", "0")
                        .param("pageSize", "10"))
                .andExpect(status().isOk());

        verify(orderService, times(1)).find(any(RequestPagingDto.class));
    }

    @Test
    void getOrderByIdFound() throws Exception {
        Order order = Order.builder().orderId(1L).build();

        when(orderService.findById(1L)).thenReturn(Optional.of(order));

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isOk());

        verify(orderService, times(1)).findById(1L);
    }

    @Test
    void getOrderByIdNotFound() throws Exception {
        when(orderService.findById(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/orders/{id}", 1L))
                .andExpect(status().isNotFound());

        verify(orderService, times(1)).findById(1L);
    }
}
