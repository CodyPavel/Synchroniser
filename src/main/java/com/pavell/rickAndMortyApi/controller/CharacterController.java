package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterController {
    final static Logger LOGGER = Logger.getLogger(CharacterController.class);

    private final CharacterService characterService;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        try {
            pageResponse = characterService.getPage(page);
            LOGGER.info(CharacterController.class.getName() + " getting character page");
        } catch (Exception e) {
            LOGGER.error(CharacterController.class.getName() + " error while getting character page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"{id}"})
    public ResponseEntity<CharacterResponse> getCharacter(@PathVariable Long id) {
        CharacterResponse characterResponse = null;
        try {
            characterResponse = characterService.getCharacterById(id);
            LOGGER.info(CharacterController.class.getName() + " getting character by Id");
        } catch (Exception e) {
            LOGGER.error(CharacterController.class.getName() + " error while getting character by Id ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(characterResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<CharacterResponse>> getCharacters(@PathVariable String[] ids) {
        List<CharacterResponse> characterResponseList = null;
        try {
            characterResponseList = characterService.getCharacterByIds(ids);
            LOGGER.info(CharacterController.class.getName() + " getting characters by Ids");
        } catch (Exception e) {
            LOGGER.error(CharacterController.class.getName() + " error while getting characters by Ids ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(characterResponseList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse>  getFilteredPage(@RequestParam(required = false) Long page,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(required = false) String species,
                                        @RequestParam(required = false) String gender,
                                        @RequestParam(required = false) String type) {

        PageResponse pageResponse = null;
        try {
            pageResponse = characterService.getFilteredPage(page, name, status, species, gender, type);
            LOGGER.info(CharacterController.class.getName() + " getting character filtered character page");
        } catch (Exception e) {
            LOGGER.error(CharacterController.class.getName() + " error while getting filtered character page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }
}
