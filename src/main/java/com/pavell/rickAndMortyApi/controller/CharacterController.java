package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public PageResponse getPage(@RequestParam(required = false) Long page) {
        return characterService.getPage(page);
    }

    @GetMapping(value = {"{id}"})
    public CharacterResponse getCharacter(@PathVariable Long id) {
        return characterService.getCharacterById(id);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public List<CharacterResponse> getCharacters(@PathVariable String[] ids) {
        return characterService.getCharacterByIds(ids);
    }

    @GetMapping("/")
    public PageResponse getFilteredPage(@RequestParam(required = false) Long page,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(required = false) String species,
                                        @RequestParam(required = false) String gender,
                                        @RequestParam(required = false) String type) {
        return characterService.getFilteredPage(page, name, status, species, gender, type);
    }
}
