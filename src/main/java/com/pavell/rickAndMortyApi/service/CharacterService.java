package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.dto.character.CharacterDTO;
import com.pavell.rickAndMortyApi.dto.responseDTO.character.ResponsePageCharacterDTO;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.dto.character.PageCharacter;
import com.pavell.rickAndMortyApi.enums.CharacterStatus;
import com.pavell.rickAndMortyApi.repo.CharacterRepo;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CharacterService {

    private final static String CHARACTER_RESOURCE_URL = "https://rickandmortyapi.com/api/character";


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

    public ResponsePageCharacterDTO getPage(Long page) {
        if (page == null) page = 1L;
//        Page
        return  new ResponsePageCharacterDTO();
    }

    public void loadData(RestTemplate restTemplate) {
        PageCharacter pageCharacter = restTemplate.getForObject(CHARACTER_RESOURCE_URL, PageCharacter.class);

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

                Optional<Location> location = locationRepo.findByName(character.getLocation().getName());
                Optional<Location> origin = locationRepo.findByName(character.getOrigin().getName());

                if (!origin.isPresent()){
                    character.setOrigin(null);
                }
                if (!location.isPresent()){
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
