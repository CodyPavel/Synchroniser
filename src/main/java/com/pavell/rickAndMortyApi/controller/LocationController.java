package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.LocationService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/location")
public class LocationController {
    final static Logger LOGGER = Logger.getLogger(LocationController.class);
    private final LocationService locationService;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        try {
            pageResponse = locationService.getPage(page);
            LOGGER.info(LocationController.class.getName() + " getting location page");
        } catch (Exception e) {
            LOGGER.error(LocationController.class.getName() + " error while getting location page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);

    }

    @GetMapping("{id}")
    public ResponseEntity<LocationResponse> getLocation(@PathVariable Long id) {

        LocationResponse locationResponse = null;
        try {
            locationResponse = locationService.getLocationById(id);
            LOGGER.info(LocationController.class.getName() + " getting location by ID");
        } catch (Exception e) {
            LOGGER.error(LocationController.class.getName() + " error while getting location by ID  ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(locationResponse, HttpStatus.OK);

    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<LocationResponse>> getLocations(@PathVariable String[] ids) {
        List<LocationResponse> locationResponseList = null;
        try {
            locationResponseList = locationService.getLocationsByIds(ids);
            LOGGER.info(LocationController.class.getName() + " getting locations by Ids");
        } catch (Exception e) {
            LOGGER.error(LocationController.class.getName() + " error while getting locations by Ids ", e);
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
            LOGGER.info(LocationController.class.getName() + " getting location filtered page");
        } catch (Exception e) {
            LOGGER.error(LocationController.class.getName() + " error while getting location filtered page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

}
