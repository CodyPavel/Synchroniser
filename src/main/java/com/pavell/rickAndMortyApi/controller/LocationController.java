package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {

    private final LocationService locationService;

    @GetMapping
    public PageResponse getPage(@RequestParam(required = false) Long page) {
        return locationService.getPage(page);
    }

    @GetMapping("{id}")
    public LocationResponse getLocation(@PathVariable Long id) {
        return locationService.getLocationById(id);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public List<LocationResponse> getLocations(@PathVariable String[] ids) {
        return locationService.getLocationsByIds(ids);
    }

    @GetMapping("/")
    public PageResponse getFilteredPage(@RequestParam(required = false) String type,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String dimension,
                                        @RequestParam(required = false) Long page) throws ParseException {
        return locationService.getFilteredPage(type, name,dimension, page);
    }

}
