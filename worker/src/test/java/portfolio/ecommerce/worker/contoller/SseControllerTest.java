package portfolio.ecommerce.worker.contoller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import portfolio.ecommerce.worker.controller.SseController;
import portfolio.ecommerce.worker.service.SseService;

import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;



@ExtendWith(SpringExtension.class)
public class SseControllerTest {
    private MockMvc mockMvc;

    @Mock
    private SseService sseService;

    @InjectMocks
    private SseController sseController;  // Notification 컨트롤러에 모의 객체 주입

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(sseController).build();
    }

    @Test
    void testSseConnection() throws Exception {
        ConcurrentHashMap<Long, SseEmitter> mockClients = new ConcurrentHashMap<>();
        when(sseService.getClients()).thenReturn(mockClients);

        Long customerId = 1L;

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.get("/sse")
                        .param("customerId", customerId.toString()))
                .andExpect(status().isOk()) // SSE 연결 시 정상 응답 확인
                .andReturn();

        assertThat(mockClients.containsKey(customerId)).isTrue();
        assertThat(mockClients.get(customerId)).isNotNull();
    }
}
