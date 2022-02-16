package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.CharacterService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
@Slf4j
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        try {
            pageResponse = characterService.getPage(page);
            log.info(CharacterController.class.getName() + " getting character page");
        } catch (Exception e) {
            log.error(CharacterController.class.getName() + " error while getting character page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"{id}"})
    public ResponseEntity<CharacterResponse> getCharacter(@PathVariable Long id) {
        CharacterResponse characterResponse = null;
        try {
            characterResponse = characterService.getCharacterById(id);
            log.info(CharacterController.class.getName() + " getting character by Id");
        } catch (Exception e) {
            log.error(CharacterController.class.getName() + " error while getting character by Id ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<CharacterResponse>> getCharacters(@PathVariable String[] ids) {
        List<CharacterResponse> characterResponseList = null;
        try {
            characterResponseList = characterService.getCharacterByIds(ids);
            log.info(CharacterController.class.getName() + " getting characters by Ids");
        } catch (Exception e) {
            log.error(CharacterController.class.getName() + " error while getting characters by Ids ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(characterResponseList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse> getFilteredPage(@RequestParam(required = false) Long page,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) String status,
                                                        @RequestParam(required = false) String species,
                                                        @RequestParam(required = false) String gender,
                                                        @RequestParam(required = false) String type) {

        PageResponse pageResponse = null;
        try {
            pageResponse = characterService.getFilteredPage(page, name, status, species, gender, type);
            log.info(CharacterController.class.getName() + " getting character filtered character page");
        } catch (Exception e) {
            log.error(CharacterController.class.getName() + " error while getting filtered character page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/common/planet")
    public ResponseEntity<LocationResponse> getCommonPlanet() {
        LocationResponse locationResponseList = null;
        try {
            locationResponseList = characterService.getCommonPlanet();
            log.info(CharacterController.class.getName() + " getting common planet");
        } catch (Exception e) {
            log.error(CharacterController.class.getName() + " error while getting common planet ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(locationResponseList, HttpStatus.OK);
    }

    @GetMapping("/count/special/character")
    public ResponseEntity<Long> getCountSpecialCharacter() {
        Long countSpecialCharacter = null;
        try {
            countSpecialCharacter = characterService.getCountSpecialCharacter();
            log.info(CharacterController.class.getName() + " getting count special character");
        } catch (Exception e) {
            log.error(CharacterController.class.getName() + " error while getting count special character ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(countSpecialCharacter, HttpStatus.OK);
    }

}
