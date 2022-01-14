package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/episodes")
public class EpisodeController {
    private EpisodeService episodeService;

    public EpisodeController(EpisodeService episodeService) {
        this.episodeService = episodeService;
    }

    @GetMapping("/list")
    public Iterable<Episode> list() {
        return episodeService.list();
    }

}
