package com.sortify.main.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {

    @Value("${caffeine.cache.min-size}")
    private int MIN_SIZE;

    @Value("${caffeine.cache.max-size}")
    private int MAX_SIZE;

    @Value("${caffeine.cache.time-to-live}")
    private int TTL;
    @Bean
    @Primary
    public CacheManager cacheManager () {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("user_cache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(MIN_SIZE)
                .expireAfterAccess(TTL, TimeUnit.MINUTES)
                .maximumSize(MAX_SIZE));
        return cacheManager;
    }
}
