package com.pavell.graphqlpoject.service;

import com.pavell.graphqlpoject.entity.Location;
import com.pavell.graphqlpoject.repo.LocationRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {
    private LocationRepo locationRepo;

    public LocationService(LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
    }

    public Iterable<Location> list() {
        return locationRepo.findAll();
    }

    public Location save(Location location) {
        return locationRepo.save(location);
    }

    public void save(List<Location> locations) {
        locationRepo.saveAll(locations);
    }

    public void parse(){

    }
}
