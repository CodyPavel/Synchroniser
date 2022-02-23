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

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        try {
            pageResponse = locationService.getPage(page);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable Long id) {

        LocationResponse locationResponse = null;
        try {
            locationResponse = locationService.getLocationById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(locationResponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<LocationResponse>> getLocations(@PathVariable String[] ids) {
        List<LocationResponse> locationResponseList = null;
        try {
            locationResponseList = locationService.getLocationsByIds(ids);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(locationResponseList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse> getFilteredPage(@RequestParam(required = false) String type,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String dimension,
                                                        @RequestParam(required = false) Long page) throws ParseException {
        PageResponse pageResponse = null;
        try {
            pageResponse = locationService.getFilteredPage(type, name, dimension, page);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
