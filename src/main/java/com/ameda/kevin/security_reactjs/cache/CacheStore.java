package com.ameda.kevin.security_reactjs.cache;/*
*
@author ameda
@project security-reactjs
@
*
*/

/*
*  Abstract Cache which is implemented as a @Bean in CacheConfig
* */

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class CacheStore <K,V>{

    private final Cache<K,V> cache;

    public CacheStore(int expireDuration, TimeUnit timeUnit) {
        this.cache = CacheBuilder.newBuilder()
                .expireAfterWrite(expireDuration,timeUnit)
                .concurrencyLevel(Runtime.getRuntime().availableProcessors())
                .build();
    }
 /*
 *  @get returns value if present in the cache else it returns null
 * */
    public V get(@NotNull K key){
        log.info("retrieving from cache with key {}",key.toString());
        return cache.getIfPresent(key);
    }

    public void put(@NotNull K key, @NotNull V value){
        log.info("storing record in cache for key {}",key.toString());
        cache.put(key,value);
    }

    public void evict(@NotNull K key){
        log.info("removing from cache with key {}",key.toString());
        cache.invalidate(key);
    }
}
