package org.starichkov.java.spring.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.starichkov.java.spring.redis.domain.entity.Book;
import org.starichkov.java.spring.redis.domain.entity.Magazine;
import org.starichkov.java.spring.redis.service.Constants;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

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

    @Bean(Constants.CACHE_MGR_BOOKS)
    @Primary
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
                .initialCacheNames(Set.of(Constants.CACHE_BOOKS_ID, Constants.CACHE_BOOKS_ISBN))
                .build();
    }

    @Bean(Constants.CACHE_MGR_MAGAZINES)
    public CacheManager redisCacheManagerMagazines() {
        Jackson2JsonRedisSerializer<Magazine> jacksonSerializer = new Jackson2JsonRedisSerializer<>(Magazine.class);
        jacksonSerializer.setObjectMapper(objectMapper);

        RedisSerializationContext.SerializationPair<Magazine> serializationPair =
                RedisSerializationContext.SerializationPair.fromSerializer(jacksonSerializer);

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofDays(1))
                .serializeValuesWith(serializationPair);

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .initialCacheNames(Set.of(Constants.CACHE_MAGAZINES_ID))
                .build();
    }

    /*
    @Bean
    public CacheResolver cacheResolver() {
        return new MultipleCacheResolver(redisCacheManager(), redisCacheManagerMagazines());
    }

    private static class MultipleCacheResolver implements CacheResolver {

        private final CacheManager booksManager;
        private final CacheManager magazinesManager;

        public MultipleCacheResolver(CacheManager booksManager, CacheManager magazinesManager) {
            this.booksManager = booksManager;
            this.magazinesManager = magazinesManager;
        }

        @Override
        public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
            Collection<Cache> caches = new ArrayList<>();
            return caches;
        }
    }
    */
}
