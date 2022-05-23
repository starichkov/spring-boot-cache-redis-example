package org.starichkov.java.spring.redis.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.cache.support.NoOpCache;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.starichkov.java.spring.redis.domain.entity.Book;
import org.starichkov.java.spring.redis.domain.entity.Magazine;
import org.starichkov.java.spring.redis.service.Constants;

import java.time.Duration;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * @author Vadim Starichkov
 * @since 05.08.2020 13:28
 */
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    private final ObjectMapper objectMapper;
    private final RedisConnectionFactory redisConnectionFactory;

    @Autowired
    public RedisCacheConfig(ObjectMapper objectMapper, RedisConnectionFactory redisConnectionFactory) {
        this.objectMapper = objectMapper;
        this.redisConnectionFactory = redisConnectionFactory;
    }

    @Override
    @Bean("cacheResolver")
    public CacheResolver cacheResolver() {
        return new MultipleCacheResolver(cacheManagerBooks(), cacheManagerMagazines());
    }

    private CacheManager cacheManagerBooks() {
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

    private CacheManager cacheManagerMagazines() {
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

    @Slf4j
    private static final class MultipleCacheResolver implements CacheResolver {

        private final CacheManager booksManager;
        private final CacheManager magazinesManager;
        private final Collection<Cache> noOpCache;

        private MultipleCacheResolver(CacheManager booksManager, CacheManager magazinesManager) {
            this.booksManager = booksManager;
            this.magazinesManager = magazinesManager;
            this.noOpCache = Collections.singletonList(new NoOpCache("no_op_cache"));
        }

        @Override
        public Collection<? extends Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
            Set<String> cacheNames = context.getOperation().getCacheNames();
            if (cacheNames.isEmpty()) {
                log.error("No cache names specified.");
                return noOpCache;
            }

            if (cacheNames.contains(Constants.CACHE_MAGAZINES_ID)) {
                return Collections.singletonList(magazinesManager.getCache(Constants.CACHE_MAGAZINES_ID));
            } else if (cacheNames.contains(Constants.CACHE_BOOKS_ID)) {
                return Collections.singletonList(booksManager.getCache(Constants.CACHE_BOOKS_ID));
            } else if (cacheNames.contains(Constants.CACHE_BOOKS_ISBN)) {
                return Collections.singletonList(booksManager.getCache(Constants.CACHE_BOOKS_ISBN));
            }

            log.error("Unknown cache names specified: {}", String.join(", ", cacheNames));
            return noOpCache;
        }
    }
}
