package ru.maycode.ses.worker.configs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Configuration
public class RedisConfig {

    @Value("${redis.database:-1}")
    Integer redisDatabase;

    @Value("${redis.password:}")
    String password;

    @Value("${redis.host:localhost}")
    String host;

    @Value("${redis.port:6379}")
    Integer port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(host, port);

        if (!password.trim().isEmpty()) {
            redisStandaloneConfiguration.setPassword(password);
        }

        if (redisDatabase > 0) {
            redisStandaloneConfiguration.setDatabase(redisDatabase);
        }

        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public <K, V> RedisTemplate<K, V> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        final RedisTemplate<K, V> redisTemplate = new RedisTemplate<>();

        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(makeDefaultObjectMapper()));
        redisTemplate.afterPropertiesSet();

        return redisTemplate;
    }

    @Bean
    public <K, V> HyperLogLogOperations<K, V> hyperLogLogOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForHyperLogLog();
    }

    @Bean
    public <K, HK, V> HashOperations<K, HK, V> hashOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForHash();
    }

    @Bean
    public <K, V> ZSetOperations<K, V> zSetOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    @Bean
    public <K, V> ClusterOperations<K, V> clusterOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForCluster();
    }

    @Bean
    public <K, V> GeoOperations<K, V> geoOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForGeo();
    }

    @Bean
    public <K, V> ListOperations<K, V> listOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForList();
    }

    @Bean
    public <K, V> SetOperations<K, V> setOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForSet();
    }

    @Bean
    public <K, HK, V> StreamOperations<K, HK, V> streamOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForStream();
    }

    @Bean
    public <K, V> ValueOperations<K, V> valueOperations(RedisTemplate<K, V> redisTemplate) {
        return redisTemplate.opsForValue();
    }

    private static ObjectMapper makeDefaultObjectMapper() {

        ObjectMapper mapper = new ObjectMapper();

        // https://github.com/FasterXML/jackson-modules-java8
        mapper.registerModule(new JavaTimeModule());

        mapper.activateDefaultTyping(
                mapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        return mapper;
    }
}
