package com.pavell.graphqlpoject.controller;

import com.pavell.graphqlpoject.entity.Episode;
import com.pavell.graphqlpoject.service.EpisodeService;
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
