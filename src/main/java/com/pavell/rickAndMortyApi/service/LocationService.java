package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.dto.location.LocationDTO;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.dto.location.PageLocation;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import com.pavell.rickAndMortyApi.utils.Constants;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static com.pavell.rickAndMortyApi.utils.Constants.LOCATION_URL;
import static com.pavell.rickAndMortyApi.utils.Constants.RESOURCE_LOCATION_URL;

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

    public void loadData(RestTemplate restTemplate) {
        PageLocation pageLocation = restTemplate.getForObject(RESOURCE_LOCATION_URL, PageLocation.class);

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
            List<LocationDTO> results = pageLocationElement.getResults();
            results.forEach(result -> {
                Location location = modelMapper.map(result, Location.class);
                locations.add(location);
            });
        });
        save(locations);
    }
}
