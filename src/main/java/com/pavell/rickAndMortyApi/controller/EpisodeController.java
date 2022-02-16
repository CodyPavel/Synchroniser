package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.EpisodeResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/episode")
@RequiredArgsConstructor
@Slf4j
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping
    public ResponseEntity<PageResponse> getPage(@RequestParam(required = false) Long page) {
        PageResponse pageResponse = null;
        try {
            pageResponse = episodeService.getPage(page);
            log.info(EpisodeController.class.getName() + " getting episode page");
        } catch (Exception e) {
            log.error(EpisodeController.class.getName() + " error while getting episode page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<EpisodeResponse> getEpisode(@PathVariable Long id) {
        EpisodeResponse episodeResponse = null;
        try {
            episodeResponse = episodeService.getEpisodeById(id);
            log.info(EpisodeController.class.getName() + " getting episode by ID ");
        } catch (Exception e) {
            log.error(EpisodeController.class.getName() + " error while getting episode by ID ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(episodeResponse, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<EpisodeResponse>> getEpisodes(@PathVariable String[] ids) {
        List<EpisodeResponse> episodeResponseList = null;
        try {
            episodeResponseList = episodeService.getEpisodesByIds(ids);
            log.info(EpisodeController.class.getName() + " getting multiple episodes by Ids ");
        } catch (Exception e) {
            log.error(EpisodeController.class.getName() + " getting multiple episodes by Ids ", e);
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
            log.info(EpisodeController.class.getName() + " getting episode filtered page");
        } catch (Exception e) {
            log.error(EpisodeController.class.getName() + " error while getting episode filtered page ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(pageResponse, HttpStatus.OK);
    }

    @GetMapping("/common/characters")
    public ResponseEntity<List<CharacterResponse>> getCommonCharacters() {
        List<CharacterResponse> characterResponseList = null;
        try {
            characterResponseList = episodeService.getCommonCharacters();
            log.info(EpisodeController.class.getName() + " getting common characters from episodes");
        } catch (Exception e) {
            log.error(EpisodeController.class.getName() + " error while getting common characters from episodes ", e);
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(characterResponseList, HttpStatus.OK);
    }
}



