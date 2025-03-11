package portfolio.ecommerce.order.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import portfolio.ecommerce.order.CachingRequestWrapper;
import portfolio.ecommerce.order.dto.OrderDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class UtilService {
    private static final ObjectMapper objectMapper = new ObjectMapper();
    public String generateHash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException("Hashing error", e);
        }
    }

    public <T> T getBodyAsDto(CachingRequestWrapper request, Class<T> dtoClass) throws IOException {
        return objectMapper.readValue(request.getCachedBody(), dtoClass);
    }
}
