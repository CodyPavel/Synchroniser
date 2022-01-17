package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.dto.location.PageLocation;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class LocationService {

    private ModelMapper modelMapper = new ModelMapper();

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

    public Long getMaxId() {
        return locationRepo.getMaxId();
    }

    public void parseAndSaveAll(RestTemplate restTemplate, String url) {
        PageLocation pageLocation = restTemplate.getForObject(url, PageLocation.class);

        List<PageLocation> pageLocationList = new ArrayList<>();
        while (true) {
            pageLocationList.add(pageLocation);
            pageLocation = restTemplate.getForObject(pageLocation.getInfo().getNext(), PageLocation.class);
            if (pageLocation.getInfo().getNext() == null) {
                pageLocationList.add(pageLocation);
                break;
            }
        }

        ArrayList<Location> locations = new ArrayList<Location>();
        pageLocationList.forEach(pageLocationElement -> {
            List<com.pavell.rickAndMortyApi.dto.location.Result> results = pageLocationElement.getResults();
            results.forEach(result -> {
                Location location = modelMapper.map(result, Location.class);
                locations.add(location);
            });
        });
        save(locations);
    }
}
