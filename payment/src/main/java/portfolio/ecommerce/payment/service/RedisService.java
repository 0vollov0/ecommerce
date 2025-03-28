package portfolio.ecommerce.payment.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

//    public void saveData(String key, String value, long timeout) {
//        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
//    }

    public String getData(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }

//    public Boolean lockData(String key, String value, long timeout) {
//        return redisTemplate.opsForValue().setIfAbsent(key, value, timeout, TimeUnit.MINUTES);
//    }
}