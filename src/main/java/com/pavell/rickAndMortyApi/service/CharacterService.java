package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.cache.LocationCache;
import com.pavell.rickAndMortyApi.dto.character.CharacterDTO;
import com.pavell.rickAndMortyApi.dto.character.PageCharacter;
import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.enums.CharacterStatus;
import com.pavell.rickAndMortyApi.enums.Gender;
import com.pavell.rickAndMortyApi.repo.CharacterRepo;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.specification.SearchCriteriaCharacter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.pavell.rickAndMortyApi.specification.CharacterSpecification.findByCriteria;
import static com.pavell.rickAndMortyApi.utils.Constants.*;
import static com.pavell.rickAndMortyApi.utils.InfoUtils.createInfoResponse;
import static com.pavell.rickAndMortyApi.utils.ParamsBuilder.isSinglePage;
import static com.pavell.rickAndMortyApi.utils.ParamsBuilder.setRequestParamsToPrevAndNext;

@Service
public class CharacterService {


    private ModelMapper modelMapper = new ModelMapper();

    private CharacterRepo characterRepo;

    private LocationRepo locationRepo;

    private EpisodeRepo episodeRepo;

    private RestTemplate restTemplate;

    private LocationCache locationCache;

    public CharacterService(CharacterRepo characterRepo,
                            LocationRepo locationRepo,
                            EpisodeRepo episodeRepo,
                            RestTemplate restTemplate,
                            LocationCache locationCache) {
        this.locationRepo = locationRepo;
        this.characterRepo = characterRepo;
        this.episodeRepo = episodeRepo;
        this.restTemplate = restTemplate;
        this.locationCache = locationCache;
    }

    public Character save(Character character) {
        return characterRepo.save(character);
    }

    public List<Character> saveAll(List<Character> character) {
        List<Character> characterList = new ArrayList<>();
        characterRepo.saveAll(character).forEach(characterList::add);
        return characterList;
    }

    public PageResponse getPage(Long page) {
        if (page == null) page = 1L;
        Page<Character> characterPage = characterRepo.findAll(PageRequest.of(page.intValue() - 1, SIZE));

        PageResponse pageResponse = parseToPageResponse(characterPage);
        InfoResponse info = createInfoResponse(characterPage);
        setPrevAndNextToInfo(info, characterPage, page);
        pageResponse.setInfo(info);

        return pageResponse;
    }

    public CharacterResponse getCharacterById(Long id) {
        Optional<Character> optionalCharacter = characterRepo.findById(id);
        if (optionalCharacter.isPresent()) {
            return modelMapper.map(optionalCharacter.get(), CharacterResponse.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found with id: " + id);
        }

    }

    public List<CharacterResponse> getCharacterByIds(String[] ids) {
        return Arrays.stream(ids)
                .map(id -> characterRepo.findById(Long.valueOf(id)).orElseGet(null))
                .filter(Objects::nonNull)
                .map(character -> modelMapper.map(character, CharacterResponse.class))
                .collect(Collectors.toList());
    }

    public PageResponse getFilteredPage(Long page, String name, String status, String species, String gender, String type) {
        if (page == null) page = 1L;
        Specification<Character> specification =
                findByCriteria(new SearchCriteriaCharacter(name,
                        status == null ? null : CharacterStatus.valueOf(status.toUpperCase()),
                        species,
                        gender == null ? null : Gender.valueOf(gender.toUpperCase()),
                        type));

        Page<Character> pageEntity = characterRepo.findAll(specification, PageRequest.of(page == null ? 0 : (int) (page - 1), SIZE));
        PageResponse pageResponse = parseToPageResponse(pageEntity);

        InfoResponse info = createInfoResponse(pageEntity);
        Map<String, String> paramsMap = createParamsMap(page, name, status, species, gender, type);

        setPrevAndNextToInfoFiltered(info, pageEntity, page);
        setRequestParamsToPrevAndNext(info, paramsMap);

        pageResponse.setInfo(info);

        return pageResponse;
    }

    private void setPrevAndNextToInfoFiltered(InfoResponse info, Page<Character> pageEntity, Long page) {
        String next;
        String prev;
        if (page == null || pageEntity.getTotalPages() == page) {
            next = null;
        } else {
            next = CHARACTER_URL;
        }
        if (page == null || page == 2) {
            prev = CHARACTER_URL;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = CHARACTER_URL;
        }

        info.setNext(next);
        info.setPrev(prev);

        isSinglePage(pageEntity.getTotalPages(), info);
        isMoreThenAllPagesFiltered(pageEntity, info, page);
    }

    private void isMoreThenAllPagesFiltered(Page<Character> pageEntity, InfoResponse info, Long page) {
        if (pageEntity.getTotalPages() < page || pageEntity.getTotalPages() == page) {
            info.setNext(null);
        }
        if (pageEntity.getTotalPages() == page || (pageEntity.getTotalPages() > page && page != 1)) {
            info.setPrev(CHARACTER_URL);
        } else if ((pageEntity.getTotalPages() + 1) == page) {
            info.setPrev(CHARACTER_URL);
        } else if (page == 2) {
            info.setPrev(CHARACTER_URL);
        } else {
            info.setPrev(null);
        }
    }

    private void setPrevAndNextToInfo(InfoResponse info, Page<Character> characterPage, Long page) {
        String next;
        String prev;
        if (page == null || characterPage.getTotalPages() == page) {
            next = null;
        } else {
            next = CHARACTER_URL + REQUEST_PARAM_PAGE_DELIMITER + (page + 1);
        }
        if (page == null || page == 2) {
            prev = CHARACTER_URL;
        } else if (page == 1) {
            prev = null;
        } else {
            prev = CHARACTER_URL + REQUEST_PARAM_PAGE_DELIMITER + (page - 1);
        }

        info.setNext(next);
        info.setPrev(prev);

        isSinglePage(characterPage.getTotalPages(), info);
        isMoreThenAllPages(characterPage, info, page);
    }

    private void isMoreThenAllPages(Page<Character> characterPage, InfoResponse info, Long page) {
        if (characterPage.getTotalPages() < page || characterPage.getTotalPages() == page) {
            info.setNext(null);
        }
        if (characterPage.getTotalPages() == page || (characterPage.getTotalPages() > page && page != 1)) {
            info.setPrev(CHARACTER_URL + REQUEST_PARAM_PAGE_DELIMITER + (page - 1));
        } else if ((characterPage.getTotalPages() + 1) == page) {
            info.setPrev(CHARACTER_URL + REQUEST_PARAM_PAGE_DELIMITER + (page - 1));
        } else if (page == 2) {
            info.setPrev(CHARACTER_URL);
        } else {
            info.setPrev(null);
        }
    }

    private PageResponse parseToPageResponse(Page<Character> page) {
        List<CharacterResponse> charResponseList = new ArrayList<>();
        page.get().forEach(character -> {
            CharacterResponse characterResponse = modelMapper.map(character, CharacterResponse.class);
            characterResponse.setUrl(CHARACTER_URL + SLASH + character.getId());

            charResponseList.add(characterResponse);
        });

        PageResponse pageResponse = new PageResponse();
        pageResponse.setResults(charResponseList);

        return pageResponse;
    }

    private HashMap<String, String> createParamsMap(Long page, String name, String status, String species, String gender, String type) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("page", page.toString());
        paramsMap.put("name", name);
        paramsMap.put("status", status);
        paramsMap.put("species", species);
        paramsMap.put("gender", gender);
        paramsMap.put("type", type);

        return paramsMap;
    }

    public void loadData() {
        PageCharacter pageCharacter = restTemplate.getForObject(RESOURCE_CHARACTER_URL, PageCharacter.class);

        List<PageCharacter> pageCharacterList = new ArrayList<>();
        while (true) {
            pageCharacterList.add(pageCharacter);
            pageCharacter = restTemplate.getForObject(pageCharacter.getInfo().getNext(), PageCharacter.class);
            if (pageCharacter == null || pageCharacter.getInfo() == null) {
                break;
            } else if (pageCharacter.getInfo().getNext() == null) {
                pageCharacterList.add(pageCharacter);
                break;
            }
        }

        ArrayList<Character> characters = new ArrayList<>();
        pageCharacterList.forEach(pageCharacterElement -> {
            List<CharacterDTO> results = pageCharacterElement.getResults();
            results.forEach(result -> {
                Character character = modelMapper.map(result, Character.class);

                character.setStatus(CharacterStatus.valueOf(result.getStatus().toUpperCase()));
                character.setGender(Gender.valueOf(result.getGender().toUpperCase()));

                Location location = null;
                Location origin = null;
                try {
                    if (!"unknown".equalsIgnoreCase(character.getLocation().getName())) {
                        location = locationCache.getByName(character.getLocation().getName());
                    }
                    if (!"unknown".equalsIgnoreCase(character.getOrigin().getName())) {
                        origin = locationCache.getByName(character.getOrigin().getName());
                    }
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                if (origin == null) {
                    character.setOrigin(null);
                } else {
                    character.setOrigin(origin);
                }

                if (location == null) {
                    character.setLocation(null);
                } else {
                    character.setLocation(location);
                }

                List<Episode> episodeList = new ArrayList<>();
                result.getEpisode().forEach(episode -> {
                    Optional<Episode> optionalEpisode = episodeRepo.findByUrl(episode);
                    optionalEpisode.ifPresent(episodeList::add);
                });
                character.setEpisode(episodeList);

                characters.add(character);
            });
        });
        characterRepo.saveAll(characters);
    }
}
