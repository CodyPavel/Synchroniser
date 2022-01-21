package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.dto.responseDTO.character.ResponsePageCharacterDTO;
import com.pavell.rickAndMortyApi.dto.responseDTO.episode.ResponsePageEpisodeDTO;
import com.pavell.rickAndMortyApi.service.CharacterService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/character")
@RequiredArgsConstructor
public class CharacterController {

    private final CharacterService characterService;

    @GetMapping
    public ResponsePageCharacterDTO getPage(@RequestParam(required = false) Long page){
        return characterService.getPage(page);
    }


}
