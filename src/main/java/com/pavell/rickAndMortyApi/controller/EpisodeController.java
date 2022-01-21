package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.dto.responseDTO.episode.ResponseEpisodeDTO;
import com.pavell.rickAndMortyApi.dto.responseDTO.episode.ResponsePageEpisodeDTO;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;

@RestController
@RequestMapping("/api/episode")
@RequiredArgsConstructor
public class EpisodeController {

    private final EpisodeService episodeService;

    @GetMapping
    public ResponsePageEpisodeDTO getPage(@RequestParam(required = false) Long page) {
        return episodeService.getPage(page);
    }

    @GetMapping("{id}")
    public ResponseEpisodeDTO getEpisode(@PathVariable Long id) {
        return episodeService.getEpisodeById(id);
    }

    @GetMapping(value = {"/multiple/{ids}"})
    public List<ResponseEpisodeDTO> getEpisodes(@PathVariable String[] ids) {
        return episodeService.getEpisodesByIds(ids);
    }

    @GetMapping("/")
    public ResponsePageEpisodeDTO getFilteredPage(@RequestParam(required = false) String air_date,
                                                  @RequestParam(required = false) String name,
                                                  @RequestParam(required = false) Long page) throws ParseException {
        ResponsePageEpisodeDTO pageEpisode = episodeService.getFilteredPage(air_date, name, page);
        return pageEpisode;
    }

}



