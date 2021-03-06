package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.impl.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        pageResponse = characterService.getPage(page);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"{id}"})
    public ResponseEntity<CharacterResponse> getCharacter(@PathVariable Long id) {
        CharacterResponse characterResponse = null;
        characterResponse = characterService.getCharacterById(id);

        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<CharacterResponse>> getCharacters(@PathVariable String[] ids) {
        List<CharacterResponse> characterResponseList = null;
        characterResponseList = characterService.getCharacterByIds(ids);

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
        pageResponse = characterService.getFilteredPage(page, name, status, species, gender, type);

        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/common/planet")
    public ResponseEntity<LocationResponse> getCommonPlanet() {
        LocationResponse locationResponseList = null;
        locationResponseList = characterService.getCommonPlanet();

        return new ResponseEntity<>(locationResponseList, HttpStatus.OK);
    }

    @GetMapping("/count/special/character")
    public ResponseEntity<Long> getCountSpecialCharacter() {
        Long countSpecialCharacter = null;
        countSpecialCharacter = characterService.getCountSpecialCharacter();

        return new ResponseEntity<>(countSpecialCharacter, HttpStatus.OK);
    }
}
