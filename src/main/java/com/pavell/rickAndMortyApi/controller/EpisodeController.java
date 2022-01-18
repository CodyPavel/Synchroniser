package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.dto.episode.EpisodeDTO;
import com.pavell.rickAndMortyApi.dto.episode.PageEpisode;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episode")
@RequiredArgsConstructor
public class EpisodeController {

    final private EpisodeService episodeService;

    private ModelMapper modelMapper = new ModelMapper();

    @GetMapping
    public ResponseEntity<PageEpisode> getPage(@RequestParam(required = false) Long page) {
        PageEpisode pageEpisode = episodeService.getPage(page);
        return new ResponseEntity<>(pageEpisode, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<EpisodeDTO> getEpisode(@PathVariable Long id) {
        EpisodeDTO episodeDTO = episodeService.getEpisodeById(id);
        return new ResponseEntity<>(episodeDTO, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public ResponseEntity<List<EpisodeDTO>> getEpisodes(@PathVariable String[] ids) {
        List<EpisodeDTO> episodeDTOList = episodeService.getEpisodesByIds(ids);
        return new ResponseEntity<>(episodeDTOList, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<PageEpisode> getFilteredPage(@RequestParam(required = false) String air_date,
                                                       @RequestParam(required = false) String name,
                                                       @RequestParam(required = false) Long page) {
        PageEpisode pageEpisode = episodeService.getFilteredPage(air_date, name, page);
        return new ResponseEntity<>(pageEpisode, HttpHeaders.EMPTY, HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<Episode>> list() {
        List<Episode> episodeList = episodeService.list();
        return new ResponseEntity<>(episodeList, HttpHeaders.EMPTY, HttpStatus.OK);
    }
}
