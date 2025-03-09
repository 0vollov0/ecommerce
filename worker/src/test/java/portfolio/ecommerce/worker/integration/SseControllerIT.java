package portfolio.ecommerce.worker.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import portfolio.ecommerce.worker.repository.CustomerRepository;
import portfolio.ecommerce.worker.service.SseService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/data/customer_data.sql")  // 테스트용 데이터 추가
@Sql(scripts = "/data/cleanup.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // 테스트 후 데이터 삭제
class SseControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SseService sseService;

    @Autowired
    private CustomerRepository customerRepository;

    private final Long existingCustomerId = 1L;

    @BeforeEach
    void setUp() {
        assertTrue(customerRepository.existsById(existingCustomerId), "테스트 고객이 존재해야 합니다.");
    }

    @Test
    void testConnect_Success() throws Exception {
        mockMvc.perform(get("/sse")
                        .param("customerId", existingCustomerId.toString()))
                .andExpect(status().isOk())
                .andExpect(content().string("Connected to SSE"));

        SseEmitter emitter = sseService.getEmitter(existingCustomerId);
        assertNotNull(emitter, "SSE Emitter 정상적으로 생성되어야 합니다.");
    }

    @Test
    void testConnect_CustomerNotFound() throws Exception {
        long nonExistingCustomerId = 999L;
        mockMvc.perform(get("/sse")
                        .param("customerId", Long.toString(nonExistingCustomerId)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Customer not found"));
    }

    @Test
    void testConnect_AlreadyConnected() throws Exception {
        // 첫 번째 연결
        mockMvc.perform(get("/sse")
                        .param("customerId", existingCustomerId.toString()))
                .andExpect(status().isConflict());

        // 두 번째 연결 시도 (이미 연결된 상태)
        mockMvc.perform(get("/sse")
                        .param("customerId", existingCustomerId.toString()))
                .andExpect(status().isConflict())
                .andExpect(content().string("Already connected"));
    }
}