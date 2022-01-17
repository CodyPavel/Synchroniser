package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.service.EpisodeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/episodes")
@RequiredArgsConstructor
public class EpisodeController {

    private EpisodeService episodeService;

    @GetMapping("/list")
    public List<Episode> list() {
        return episodeService.list();
    }

}
