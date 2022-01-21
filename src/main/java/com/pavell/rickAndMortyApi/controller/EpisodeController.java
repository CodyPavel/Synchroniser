package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.dto.responseDTO.episode.ResponseEpisodeDTO;
import com.pavell.rickAndMortyApi.dto.responseDTO.episode.ResponsePageEpisodeDTO;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    public ResponseEntity<ResponsePageEpisodeDTO> getPage(@RequestParam(required = false) Long page) {
        ResponsePageEpisodeDTO responsePageEpisodeDTO = episodeService.getPage(page);
        return new ResponseEntity<>(responsePageEpisodeDTO, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseEpisodeDTO> getEpisode(@PathVariable Long id) {
        ResponseEpisodeDTO responseEpisodeDTO = episodeService.getEpisodeById(id);
        return new ResponseEntity<>(responseEpisodeDTO, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<ResponseEpisodeDTO>> getEpisodes(@PathVariable String[] ids) {
        List<ResponseEpisodeDTO> episodeDTOList = episodeService.getEpisodesByIds(ids);
        return new ResponseEntity<>(episodeDTOList, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<ResponsePageEpisodeDTO> getFilteredPage(@RequestParam(required = false) String air_date,
                                                                  @RequestParam(required = false) String name,
                                                                  @RequestParam(required = false) Long page) throws ParseException {
        ResponsePageEpisodeDTO pageEpisode = episodeService.getFilteredPage(air_date, name, page);
        return new ResponseEntity<>(pageEpisode, HttpHeaders.EMPTY, HttpStatus.OK);
    }

}



