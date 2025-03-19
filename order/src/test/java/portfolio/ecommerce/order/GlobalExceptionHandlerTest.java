package portfolio.ecommerce.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testValidationException() throws Exception {
        // Given: 필수 값이 없는 잘못된 JSON 요청
        Map<String, Object> invalidRequest = new HashMap<>();
        invalidRequest.put("product_id", 1);  // customer_id, quantity 누락

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest))) // DTO 매핑 오류 유발
                .andExpect(status().isBadRequest()) // 400 응답 검증
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Validation Error"))); // 에러 메시지 검증
    }

    @Test
    void testJsonParseException() throws Exception {
        // Given: 잘못된 JSON 요청 (타입 불일치)
        String invalidJson = "{ \"product_id\": \"invalid\", \"customer_id\": \"customer\", \"quantity\": 10 }"; // product_id는 Long인데 문자열로 보냄

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest()); // 400 응답 검증
    }

    @Test
    void testInternalServerError() throws Exception {
        // Given: 일부러 서버 오류를 발생시키는 요청
        mockMvc.perform(post("/orders/force-error")) // 존재하지 않는 엔드포인트
                .andExpect(status().isInternalServerError()) // 500 응답 검증
                .andExpect(content().string(org.hamcrest.Matchers.containsString("INTERNAL SERVER ERROR"))); // 에러 메시지 검증
    }
}