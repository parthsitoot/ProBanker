package com.banking.proBanker.Config;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.val;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig {

    @Bean
    public CacheManager cacheManager(){
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCacheNames(List.of("otpAttempts")); //this defines the cache names
        cacheManager.setCaffeine(caffeineConfig());
        return cacheManager;
    }

    private Caffeine<Object, Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(15, TimeUnit.MINUTES) // cache entries expire after 15 minutes
                .maximumSize(100) // max of 100 entries in cache
                .recordStats(); // for monitoring cache stats;
    }
}
