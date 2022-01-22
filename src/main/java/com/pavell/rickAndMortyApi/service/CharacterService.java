package com.pavell.rickAndMortyApi.service;

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
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.response.CharacterResponse;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.pavell.rickAndMortyApi.utils.Constants.*;
import static com.pavell.rickAndMortyApi.utils.InfoUtils.createInfoResponse;

@Service
public class CharacterService {


    private ModelMapper modelMapper = new ModelMapper();

    private CharacterRepo characterRepo;

    private LocationRepo locationRepo;

    private EpisodeRepo episodeRepo;

    public CharacterService(CharacterRepo characterRepo, LocationRepo locationRepo, EpisodeRepo episodeRepo) {
        this.locationRepo = locationRepo;
        this.characterRepo = characterRepo;
        this.episodeRepo = episodeRepo;
    }

    public Iterable<Character> list() {
        return characterRepo.findAll();
    }

    public Character save(Character character) {
        return characterRepo.save(character);
    }

    public void save(List<Character> character) {
        characterRepo.saveAll(character);
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

        isSinglePage(characterPage, info);
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

    private void isSinglePage(Page<Character> characterPage, InfoResponse info) {
        if (characterPage.getTotalPages() == 1 || characterPage.getTotalPages() == 0) {
            info.setNext(null);
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

    public void loadData(RestTemplate restTemplate) {
        PageCharacter pageCharacter = restTemplate.getForObject(RESOURCE_CHARACTER_URL, PageCharacter.class);

        List<PageCharacter> pageCharacterList = new ArrayList<>();
        while (true) {
            pageCharacterList.add(pageCharacter);
            pageCharacter = restTemplate.getForObject(pageCharacter.getInfo().getNext(), PageCharacter.class);
            if (pageCharacter.getInfo().getNext() == null) {
                pageCharacterList.add(pageCharacter);
                break;
            }
        }


        pageCharacterList.forEach(pageCharacterElement -> {
            List<CharacterDTO> results = pageCharacterElement.getResults();
            results.forEach(result -> {
                Character character = modelMapper.map(result, Character.class);

                character.setStatus(CharacterStatus.valueOf(result.getStatus().toUpperCase()));
                character.setGender(Gender.valueOf(result.getGender().toUpperCase()));

                Optional<Location> location = locationRepo.findByName(character.getLocation().getName());
                Optional<Location> origin = locationRepo.findByName(character.getOrigin().getName());

                if (!origin.isPresent()) {
                    character.setOrigin(null);
                }
                if (!location.isPresent()) {
                    character.setLocation(null);
                }
                origin.ifPresent(character::setOrigin);
                location.ifPresent(character::setLocation);

                List<Episode> episodeList = new ArrayList<>();
                result.getEpisode().forEach(episode -> {
                    Optional<Episode> optionalEpisode = episodeRepo.findByUrl(episode);
                    optionalEpisode.ifPresent(episodeList::add);
                });
                character.setEpisode(episodeList);


                save(character);
            });
        });
    }
}
