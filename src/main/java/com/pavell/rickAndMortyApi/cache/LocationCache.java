package com.pavell.rickAndMortyApi.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

@Component
public class LocationCache {

    @Value("${cache.max.size}")
    private int maximumSize;

    @Autowired
    private LocationRepo locationRepo;

    private LoadingCache<String, Location> locationCache =
            CacheBuilder.newBuilder()
                    .maximumSize(maximumSize)
                    .build(new CacheLoader<String, Location>() {
                        @Override
                        public Location load(String locationName) throws Exception {
                            Optional<Location> optionalLocation = locationRepo.findByName(locationName);
                            return optionalLocation.orElse(null);
                        }
                    });

    public Location getByName(String name) throws ExecutionException {
        return locationCache.get(name);
    }
}
