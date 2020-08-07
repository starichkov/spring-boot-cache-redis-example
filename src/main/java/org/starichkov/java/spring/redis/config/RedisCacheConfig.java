package org.starichkov.java.spring.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.starichkov.java.spring.redis.domain.entity.Book;

import java.time.Duration;

/**
 * @author Vadim Starichkov
 * @since 05.08.2020 13:28
 */
@Configuration
@EnableCaching
public class RedisCacheConfig {

    private final ObjectMapper objectMapper;
    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisCacheConfig(ObjectMapper objectMapper, RedisConnectionFactory redisConnectionFactory) {
        this.objectMapper = objectMapper;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Bean
    public CacheManager redisCacheManager() {
        Jackson2JsonRedisSerializer<Book> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Book.class);
        jacksonSerializer.setObjectMapper(objectMapper);

        RedisSerializationContext.SerializationPair<Book> serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))
                .serializeValuesWith(serializationPair);

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .build();
    }
}
