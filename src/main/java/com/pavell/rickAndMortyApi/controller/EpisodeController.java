package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.EpisodeResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.impl.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        try {
            pageResponse = episodeService.getPage(page);
         } catch (Exception e) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<EpisodeResponse> getEpisode(@PathVariable Long id) {
        EpisodeResponse episodeResponse = null;
        try {
            episodeResponse = episodeService.getEpisodeById(id);
         } catch (Exception e) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(episodeResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<EpisodeResponse>> getEpisodes(@PathVariable String[] ids) {
        List<EpisodeResponse> episodeResponseList = null;
        try {
            episodeResponseList = episodeService.getEpisodesByIds(ids);
         } catch (Exception e) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(episodeResponseList, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageResponse> getFilteredPage(@RequestParam(required = false) String episode,
                                                        @RequestParam(required = false) String name,
                                                        @RequestParam(required = false) Long page) throws ParseException {
        PageResponse pageResponse = null;
        try {
            pageResponse = episodeService.getFilteredPage(episode, name, page);
         } catch (Exception e) {
             return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/common/characters")
    public ResponseEntity<List<CharacterResponse>> getCommonCharacters() {
        List<CharacterResponse> characterResponseList = null;
        try {
            characterResponseList = episodeService.getCommonCharacters();
         } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(characterResponseList, HttpStatus.OK);
    }
}



