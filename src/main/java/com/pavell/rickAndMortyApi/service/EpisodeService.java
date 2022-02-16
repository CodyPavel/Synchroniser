package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.dto.episode.EpisodeDTO;
import com.pavell.rickAndMortyApi.dto.episode.PageEpisode;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.EpisodeResponse;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.specification.SearchCriteriaEpisode;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

import static com.pavell.rickAndMortyApi.specification.EpisodeSpecification.findByCriteria;
import static com.pavell.rickAndMortyApi.utils.Constants.*;
import static com.pavell.rickAndMortyApi.utils.InfoUtils.createInfoResponse;
import static com.pavell.rickAndMortyApi.utils.ParamsBuilder.isSinglePage;
import static com.pavell.rickAndMortyApi.utils.ParamsBuilder.setRequestParamsToPrevAndNext;

@Service
@Slf4j
public class EpisodeService {


    private ModelMapper modelMapper = new ModelMapper();

    private EpisodeRepo episodeRepo;

    private RestTemplate restTemplate;

    public EpisodeService(EpisodeRepo episodeRepo, RestTemplate restTemplate) {
        this.episodeRepo = episodeRepo;
        this.restTemplate = restTemplate;
    }

    public Episode getById(Long id) {
        log.info(EpisodeService.class.getName() + " got episode with id: " + id);
        return episodeRepo.findById(id).orElseThrow(NoSuchElementException::new);
    }

    public Episode save(Episode episode) {
        log.info(EpisodeService.class.getName() + " saved episode with name: " + episode.getName());
        return episodeRepo.save(episode);
    }

    public void save(List<Episode> episodes) {
        episodeRepo.saveAll(episodes);
        log.info(EpisodeService.class.getName() + " saved all episodes " +
                episodes.stream().map(Episode::getName).collect(Collectors.joining(", ")));
    }

    public PageResponse getPage(Long page) {
        if (page == null) page = 1L;
        Page<Episode> episodePage = episodeRepo.findAll(PageRequest.of(page.intValue() - 1, SIZE));
        log.info(EpisodeService.class.getName() + " got episode page : " + page);


        PageResponse pageResponse = parseToPageResponse(episodePage);

        InfoResponse info = createInfoResponse(episodePage);
        setPrevAndNextToInfo(info, episodePage, page);
        pageResponse.setInfo(info);

        return pageResponse;
    }

    public EpisodeResponse getEpisodeById(Long id) {

        Optional<Episode> optionalEpisode = episodeRepo.findById(id);
        log.info(EpisodeService.class.getName() + " got episode with id : " + id);

        if (optionalEpisode.isPresent()) {
            return modelMapper.map(optionalEpisode.get(), EpisodeResponse.class);
        } else {
            return new EpisodeResponse();
        }
    }

    public List<EpisodeResponse> getEpisodesByIds(String[] ids) {
        List<EpisodeResponse> episodes = new ArrayList<>();

        Arrays.stream(ids).forEach(id -> {
                    Optional<Episode> optionalEpisode = episodeRepo.findById(Long.valueOf(id));
                    optionalEpisode.ifPresent(episode -> episodes.add(modelMapper.map(episode, EpisodeResponse.class)));
                }
        );
        log.info(EpisodeService.class.getName() + " got episodes with ids : " + Arrays.toString(ids));

        return episodes;
    }

    public PageResponse getFilteredPage(String episode, String name, Long page) throws ParseException {
        if (page == null) page = 1L;

        Specification<Episode> specification = findByCriteria(new SearchCriteriaEpisode(episode, name));

        Page<Episode> pageEntity = episodeRepo.findAll(specification, PageRequest.of(page == null ? 0 : (int) (page - 1), SIZE));
        log.info(EpisodeService.class.getName() + " got episode by page: " + page +
                " and search criteria params" +
                " name=" + name +
                " episode=" + episode);

        PageResponse pageEpisode = parseToPageResponse(pageEntity);

        InfoResponse info = createInfoResponse(pageEntity);

        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.put("episode", episode);
        paramsMap.put("name", name);

        setRequestParamsToPrevAndNext(info, paramsMap);
        pageEpisode.setInfo(info);


        return pageEpisode;
    }

    private PageResponse parseToPageResponse(Page<Episode> page) {
        List<EpisodeResponse> resultList = new ArrayList<>();
        page.get().forEach(episode -> resultList.add(modelMapper.map(episode, EpisodeResponse.class)));

        PageResponse pageEpisode = new PageResponse();
        pageEpisode.setResults(resultList);

        return pageEpisode;
    }

    public List<CharacterResponse> getCommonCharacters() {
        //  TODO групировка
        return episodeRepo.findAll().stream()
                .map(Episode::getCharacters)
                .flatMap(Collection::stream)
                .sorted(Comparator.comparing(Character::getId))
                .distinct()
                .limit(5)
                .map(character -> modelMapper.map(character, CharacterResponse.class))
                .collect(Collectors.toList());

    }


    public void loadData() {
        PageEpisode pageEpisode = restTemplate.getForObject(RESOURCE_EPISODE_URL, PageEpisode.class);
        log.info(EpisodeService.class.getName() + " RestTemplate getForObject  with url " + RESOURCE_EPISODE_URL);

        List<PageEpisode> pageEpisodeList = new ArrayList<>();
        while (true) {
            pageEpisodeList.add(pageEpisode);
            pageEpisode = restTemplate.getForObject(pageEpisode.getInfo().getNext(), PageEpisode.class);
            if (Objects.isNull(pageEpisode) ||
                    Objects.isNull(pageEpisode.getInfo()) ||
                    Objects.isNull(pageEpisode.getInfo().getNext())) {
                log.info(EpisodeService.class.getName() + " RestTemplate getForObject  with url null");
            } else {
                log.info(EpisodeService.class.getName() + " RestTemplate getForObject  with url " + pageEpisode.getInfo().getNext());

            }
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
                Optional<Episode> episodeOptional = episodeRepo.findByUrl(episode.getUrl());
                if (episodeOptional.isEmpty()) {
                    episodes.add(episode);
                } else {
                    Episode oldEpisode = episodeOptional.get();
                    updateEpisode(oldEpisode, episode);
                    episodes.add(oldEpisode);
                }
            });
        });

        save(episodes);
    }

    private void updateEpisode(Episode oldEpisode, Episode newEpisode) {
        oldEpisode.setEpisode(newEpisode.getEpisode());
        oldEpisode.setUrl(newEpisode.getUrl());
        oldEpisode.setAir_date(newEpisode.getAir_date());
        oldEpisode.setCharacters(newEpisode.getCharacters());
        oldEpisode.setCreated(newEpisode.getCreated());
        oldEpisode.setName(newEpisode.getName());
    }

    private void setPrevAndNextToInfo(InfoResponse info, Page<Episode> episodePage, Long page) {
        String next = null;
        String prev = null;
        if (page == null || episodePage.getTotalPages() == page) {
            next = null;
        } else {
            next = EPISODE_URL + REQUEST_PARAM_PAGE_DELIMITER + (page + 1);
        }
        if (page == null || page == 2) {
            prev = EPISODE_URL;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = EPISODE_URL + REQUEST_PARAM_PAGE_DELIMITER + (page - 1);
        }

        info.setNext(next);
        info.setPrev(prev);
        isSinglePage(episodePage.getTotalPages(), info);
    }
}
