package com.example.videogamesshop.cache;

import com.example.videogamesshop.dto.game.GameCatalogResponse;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

@Component
public class GameCacheService {

    private final Map<GameQueryKey, Page<GameCatalogResponse>> cache;

    public GameCacheService() {
        Map<GameQueryKey, Page<GameCatalogResponse>> map = new LinkedHashMap<>(16, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<GameQueryKey,
                    Page<GameCatalogResponse>> eldest) {
                return size() > 5;
            }
        };
        this.cache = Collections.synchronizedMap(map);
    }

    public Page<GameCatalogResponse> get(GameQueryKey key) {
        return cache.get(key);
    }

    public void put(GameQueryKey key, Page<GameCatalogResponse> value) {
        cache.put(key, value);
    }

    public void clear() {
        cache.clear();
    }
}