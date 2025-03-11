package portfolio.ecommerce.order.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import portfolio.ecommerce.order.CachingRequestWrapper;
import portfolio.ecommerce.order.dto.OrderDto;
import portfolio.ecommerce.order.service.RedisService;
import portfolio.ecommerce.order.service.UtilService;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class OrderInterceptor implements HandlerInterceptor {
    private static final Logger log = LogManager.getLogger(OrderInterceptor.class);
    private final RedisService redisService;
    private final UtilService utilService;

    public OrderInterceptor(RedisService redisService, UtilService utilService) {
        this.redisService = redisService;
        this.utilService = utilService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();
        String method = request.getMethod();
        if (requestURI.equals("/orders") && "POST".equals(method)) {
            CachingRequestWrapper cachingRequestWrapper = new CachingRequestWrapper(request);
            OrderDto dto = utilService.getBodyAsDto(cachingRequestWrapper, OrderDto.class);
            String uniqueKey = utilService.generateHash(String.valueOf(dto.getCustomer_id() + dto.getProduct_id() + dto.getQuantity()));
            Boolean acquired = redisService.lockData(uniqueKey, "processed", 1);
            if (Boolean.FALSE.equals(acquired)) {
                response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                response.getWriter().write("Duplicate order request");
                return false;
            }else {
                request.setAttribute("cachedRequest", cachingRequestWrapper);
                return true;
            }
        }

        return true; // true: 요청 진행, false: 요청 중단
    }
}
