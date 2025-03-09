package portfolio.ecommerce.worker.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import portfolio.ecommerce.worker.repository.CustomerRepository;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SseServiceTest {

    private SseService sseService;
    private CustomerRepository customerRepository;

    @BeforeEach
    void setUp() {
        customerRepository = mock(CustomerRepository.class);
        sseService = new SseService(customerRepository);
    }

    @Test
    void testConnect_Success() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        ResponseEntity<String> response = sseService.connect(customerId);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("Connected to SSE", response.getBody());
        assertNotNull(sseService.getEmitter(customerId));
    }

    @Test
    void testConnect_CustomerNotFound() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(false);

        ResponseEntity<String> response = sseService.connect(customerId);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("Customer not found", response.getBody());
        assertNull(sseService.getEmitter(customerId));
    }

    @Test
    void testConnect_AlreadyConnected() {
        Long customerId = 1L;
        when(customerRepository.existsById(customerId)).thenReturn(true);

        sseService.connect(customerId); // First connection
        ResponseEntity<String> response = sseService.connect(customerId); // Second connection

        assertEquals(409, response.getStatusCode().value());
        assertEquals("Already connected", response.getBody());
    }

    @Test
    void testGetEmitter_NotConnected() {
        Long customerId = 1L;
        assertNull(sseService.getEmitter(customerId));
    }
}
