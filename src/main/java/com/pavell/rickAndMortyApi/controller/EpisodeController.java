package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.dto.episode.PageEpisode;
import com.pavell.rickAndMortyApi.dto.episode.EpisodeDTO;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/episode")
public class EpisodeController {

    private EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeRepo) {
        this.episodeService = episodeRepo;
    }

    @GetMapping
    public PageEpisode getPage(@RequestParam(required = false) Long page) {
        if (page == null) {
            return episodeService.getFirstPage();
        }
        return episodeService.getPage(page);
    }

    @GetMapping("{id}")
    public EpisodeDTO getEpisode(@PathVariable Long id) {
        return episodeService.getEpisodeById(id);
    }

    @RequestMapping(value = {"{ids}"}, method = RequestMethod.GET)
    public List<EpisodeDTO> getEpisodes(@PathVariable String[]  ids) {
        return episodeService.getEpisodesByIds(ids);
    }


    @GetMapping("/list")
    public List<Episode> list() {
        return episodeService.list();
    }

}
