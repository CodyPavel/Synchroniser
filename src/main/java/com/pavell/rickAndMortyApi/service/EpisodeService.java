package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.dto.episode.EpisodeDTO;
import com.pavell.rickAndMortyApi.dto.episode.Info;
import com.pavell.rickAndMortyApi.dto.episode.PageEpisode;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import com.pavell.rickAndMortyApi.specification.EpisodeSpecification;
import com.pavell.rickAndMortyApi.specification.SearchCriteria;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class EpisodeService {

    private final static String EPISODE_RESOURCE_URL ="https://rickandmortyapi.com/api/episode";

    private final static String FIRST_EPISODE_PAGE = "http://localhost:8080/api/episode";
    private final static String EPISODE_PAGE = "http://localhost:8080/api/episode?page=";
    private final static int SIZE = 20;

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
        if (page == null) page = 1L;
        Page<Episode> episodePage = episodeRepo.findAll(PageRequest.of(page.intValue() - 1, SIZE));

        PageEpisode pageEpisode = parseToPageEpisode(episodePage);

        Info info = createInfo(episodePage);
        setPrevAndNextToInfo(info, episodePage, page);
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

    public PageEpisode getFilteredPage(String air_date, String name, Long page) {
        Page<Episode> pageEntity = episodeRepo.findAll(createSpecification(air_date, name), PageRequest.of(page == null ? 0 : (int) (page - 1), SIZE));
        PageEpisode pageEpisode = parseToPageEpisode(pageEntity);

        Info info = createInfo(pageEntity);

        Map<String, String> map = new HashMap<>();
        map.put("air_date", air_date);
        map.put("name", name);

        setPrevAndNextToInfoWithRequestParams(info, pageEntity, page, map);
        pageEpisode.setInfo(info);

        return pageEpisode;
    }

    private PageEpisode parseToPageEpisode(Page<Episode> page) {
        List<EpisodeDTO> resultList = new ArrayList<>();
        page.get().forEach(episode -> resultList.add(modelMapper.map(episode, EpisodeDTO.class)));

        PageEpisode pageEpisode = new PageEpisode();
        pageEpisode.setResults(resultList);

        return pageEpisode;
    }

    public void loadData(RestTemplate restTemplate) {
        PageEpisode pageEpisode = restTemplate.getForObject(EPISODE_RESOURCE_URL, PageEpisode.class);

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

    private Info createInfo(Page page) {
        Info info = new Info();
        info.setCount(page.getTotalElements());
        info.setPages((long) page.getTotalPages());

        return info;
    }

    private void setPrevAndNextToInfo(Info info, Page<Episode> episodePage, Long page) {
        String next = null;
        String prev = null;
        if (page == null || episodePage.getTotalPages() == page) {
            next = null;
        } else {
            next = EPISODE_PAGE + (page + 1);
        }
        if (page == null || page == 2) {
            prev = FIRST_EPISODE_PAGE;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = EPISODE_PAGE + (page - 1);
        }

        info.setNext(next);
        info.setPrev(prev);
        isSinglePage(episodePage, info);
    }

    private void setPrevAndNextToInfoWithRequestParams(Info info, Page<Episode> episodePage, Long page, Map<String, String> params) {
        setPrevAndNextToInfo(info, episodePage, page);
        String next;
        String prev;
        AtomicReference<String> allParams = new AtomicReference<>(StringUtils.EMPTY);
        params.forEach((key, value) -> {
            String param = key + "=" + value;
            if (StringUtils.EMPTY.equalsIgnoreCase(allParams.get())) {
                allParams.set("/?" + param);
            }
            allParams.set(allParams + "&" + param);
        });

        if (info.getNext() != null) {
            info.setNext(info.getNext() + allParams);
        }

        if (info.getPrev() != null) {
            info.setPrev(info.getPrev() + allParams);
        }
        isSinglePage(episodePage, info);
    }

    private void isSinglePage(Page<Episode> episodePage, Info info) {
        if (episodePage.getTotalPages() == 1 || episodePage.getTotalPages() == 0) {
            info.setNext(null);
            info.setPrev(null);
        }
    }

    private Specification<Episode> createSpecification(String air_date, String name) {
        EpisodeSpecification specName = new EpisodeSpecification(new SearchCriteria("name", ";", name));
        EpisodeSpecification specAirDate = new EpisodeSpecification(new SearchCriteria("air_date", ";", air_date));
        Specification<Episode> specification = null;
        if (name != null && air_date != null) {
            specification = specName.and(specAirDate);
        } else if (name != null) {
            specification = specName;
        } else if (air_date != null) {
            specification = specAirDate;
        }
        return specification;
    }
}
