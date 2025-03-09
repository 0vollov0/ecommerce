package portfolio.ecommerce.worker.contoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import portfolio.ecommerce.worker.controller.SseController;
import portfolio.ecommerce.worker.service.SseService;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SseControllerTest {

    private MockMvc mockMvc;
    private SseService sseService;

    @BeforeEach
    void setUp() {
        sseService = mock(SseService.class);
        SseController sseController = new SseController(sseService);
        mockMvc = MockMvcBuilders.standaloneSetup(sseController).build();
    }

    @Test
    void testConnect_Success() throws Exception {
        Long customerId = 1L;
        when(sseService.connect(customerId)).thenReturn(ResponseEntity.ok("Connected to SSE"));

        mockMvc.perform(get("/sse").param("customerId", customerId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Connected to SSE"));
    }

    @Test
    void testConnect_CustomerNotFound() throws Exception {
        Long customerId = 1L;
        when(sseService.connect(customerId)).thenReturn(ResponseEntity.badRequest().body("Customer not found"));

        mockMvc.perform(get("/sse").param("customerId", customerId.toString()))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Customer not found"));
    }

    @Test
    void testConnect_AlreadyConnected() throws Exception {
        Long customerId = 1L;
        when(sseService.connect(customerId)).thenReturn(ResponseEntity.status(409).body("Already connected"));

        mockMvc.perform(get("/sse").param("customerId", customerId.toString()))
                .andExpect(status().isConflict())
                .andExpect(content().string("Already connected"));
    }
}
