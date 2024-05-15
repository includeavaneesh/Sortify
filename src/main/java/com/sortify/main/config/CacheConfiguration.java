package com.sortify.main.config;

import com.github.benmanes.caffeine.cache.Caffeine;
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

    @Bean
    @Primary
    public CacheManager cacheManager () {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager("user_cache");
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(1)
                .expireAfterAccess(5, TimeUnit.SECONDS)
                .maximumSize(2));
        return cacheManager;
    }
}
