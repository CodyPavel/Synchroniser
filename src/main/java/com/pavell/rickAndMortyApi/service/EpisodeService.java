package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.dto.episode.Info;
import com.pavell.rickAndMortyApi.dto.episode.PageEpisode;
import com.pavell.rickAndMortyApi.dto.episode.EpisodeDTO;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class EpisodeService {

    private final static String FIRST_EPISODE_PAGE = "http://localhost:8080/api/episode";
    private final static String EPISODE_PAGE = "http://localhost:8080/api/episode?page=";


    private ModelMapper modelMapper = new ModelMapper();

    private EpisodeRepo episodeRepo;

    public List<Episode> list() {
        List<Episode> episodes = new ArrayList<>();
        for (Episode episode : episodeRepo.findAll()) {
            episodes.add(episode);
        }

        return episodes;
    }

    public Episode getById(Long id) {
        return episodeRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public EpisodeService(EpisodeRepo episodeRepo) {
        this.episodeRepo = episodeRepo;
    }

    public Episode save(Episode episode) {
        return episodeRepo.save(episode);
    }

    public void save(List<Episode> episodes) {
        episodeRepo.saveAll(episodes);
    }

    public PageEpisode getPage(Long page) {
        Pageable pageable = PageRequest.of(page.intValue() - 1, 20);
        Page<Episode> episodePage = episodeRepo.findAll(pageable);

        if (episodePage.getTotalPages() < page) {
            //TODO: return error
            return new PageEpisode();
        }
        PageEpisode pageEpisode = getPageEpisode(episodePage);

        Info info = getInfo(episodePage);
        setPrevAndNextToInfo(info, episodePage, page);

        pageEpisode.setInfo(info);


        return pageEpisode;
    }

    public PageEpisode getFirstPage() {
        Pageable pageable = PageRequest.of(0, 20);
        Page<Episode> page = episodeRepo.findAll(pageable);

        PageEpisode pageEpisode = getPageEpisode(page);
        Info info = getInfo(page);

        info.setNext(EPISODE_PAGE + 2);
        info.setPrev(null);      //null because its first page
        pageEpisode.setInfo(info);

        return pageEpisode;
    }

    public EpisodeDTO getEpisodeById(Long id) {

        Optional<Episode> optionalEpisode = episodeRepo.findById(id);
        if (optionalEpisode.isPresent()) {
            return modelMapper.map(optionalEpisode.get(), EpisodeDTO.class);
        } else {
            //TODO:return exception
            return new EpisodeDTO();
        }
    }

    public List<EpisodeDTO> getEpisodesByIds(String[] ids) {
        List<EpisodeDTO> episodes = new ArrayList<>();

        Arrays.stream(ids).forEach(id -> {
                    Optional<Episode> optionalEpisode = episodeRepo.findById(Long.valueOf(id));
                    optionalEpisode.ifPresent(episode -> episodes.add(modelMapper.map(episode, EpisodeDTO.class)));
                }
        );

        return episodes;
    }

    public void parseAndSaveAll(RestTemplate restTemplate, String url) {
        PageEpisode pageEpisode = restTemplate.getForObject(url, PageEpisode.class);

        List<PageEpisode> pageEpisodeList = new ArrayList<>();
        while (true) {
            pageEpisodeList.add(pageEpisode);
            pageEpisode = restTemplate.getForObject(pageEpisode.getInfo().getNext(), PageEpisode.class);
            if (pageEpisode.getInfo().getNext() == null) {
                pageEpisodeList.add(pageEpisode);
                break;
            }
        }

        ArrayList<Episode> episodes = new ArrayList<Episode>();
        pageEpisodeList.forEach(pageEpisodeElement -> {
            List<EpisodeDTO> results = pageEpisodeElement.getResults();
            results.forEach(result -> {
                Episode episode = modelMapper.map(result, Episode.class);
                episodes.add(episode);
            });
        });

        save(episodes);
    }

    private PageEpisode getPageEpisode(Page<Episode> page) {
        List<EpisodeDTO> resultList = new ArrayList<>();
        page.get().forEach(episode -> resultList.add(modelMapper.map(episode, EpisodeDTO.class)));

        PageEpisode pageEpisode = new PageEpisode();
        pageEpisode.setResults(resultList);

        return pageEpisode;
    }

    private Info getInfo(Page page) {
        Info info = new Info();
        info.setCount(page.getTotalElements());
        info.setPages((long) page.getTotalPages());

        return info;
    }

    private void setPrevAndNextToInfo(Info info, Page<Episode> episodePage, Long page) {
        String next = null;
        String prev = null;
        if (episodePage.getTotalPages() == page) {
            next = null;
        } else {
            next = EPISODE_PAGE + (page + 1);
        }
        if (page == 2) {
            prev = FIRST_EPISODE_PAGE;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = EPISODE_PAGE + (page - 1);
        }
        info.setNext(next);
        info.setPrev(prev);
    }
}
