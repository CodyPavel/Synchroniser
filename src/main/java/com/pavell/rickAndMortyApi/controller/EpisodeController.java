package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.EpisodeResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
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
    public PageResponse getPage(@RequestParam(required = false) Long page) {
        return episodeService.getPage(page);
    }

    @GetMapping("{id}")
    public EpisodeResponse getEpisode(@PathVariable Long id) {
        return episodeService.getEpisodeById(id);
    }

    //TODO:Check this annotation @ApiParam(defaultValue = "[]")
    @GetMapping(value = {"/multiple/{ids}"})
    public List<EpisodeResponse> getEpisodes(@PathVariable String[] ids) {
        return episodeService.getEpisodesByIds(ids);
    }

    @GetMapping("/")
    public PageResponse getFilteredPage(@RequestParam(required = false) String air_date,
                                        @RequestParam(required = false) String name,
                                        @RequestParam(required = false) Long page) throws ParseException {
        return episodeService.getFilteredPage(air_date, name, page);
    }

}



