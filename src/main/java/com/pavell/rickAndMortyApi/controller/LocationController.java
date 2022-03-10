package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.impl.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {
    private final LocationService locationService;
    private PageResponse pageResponse;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) throws Exception {
        PageResponse pageResponse = null;
        pageResponse = locationService.getPage(page);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable Long id) {
        LocationResponse locationResponse = null;
        locationResponse = locationService.getLocationById(id);

        return new ResponseEntity<>(locationResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<LocationResponse>> getLocations(@PathVariable String[] ids) {
        List<LocationResponse> locationResponseList = null;
        locationResponseList = locationService.getLocationsByIds(ids);

        return new ResponseEntity<>(locationResponseList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse> getFilteredPage(@RequestParam(required = false) String type,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String dimension,
                                                        @RequestParam(required = false) Long page) throws ParseException {
        PageResponse pageResponse = null;
        pageResponse = locationService.getFilteredPage(type, name, dimension, page);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
