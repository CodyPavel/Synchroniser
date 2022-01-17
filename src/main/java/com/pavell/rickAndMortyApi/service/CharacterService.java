package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.entity.character.Character;
import com.pavell.rickAndMortyApi.dto.character.PageCharacter;
import com.pavell.rickAndMortyApi.repo.CharacterRepo;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CharacterService {

    private ModelMapper modelMapper = new ModelMapper();

    private CharacterRepo characterRepo;

    private LocationRepo locationRepo;

    public CharacterService(CharacterRepo characterRepo, LocationRepo locationRepo) {
        this.locationRepo = locationRepo;
        this.characterRepo = characterRepo;
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

    public void parseAndSaveAll(RestTemplate restTemplate, String url) {
        PageCharacter pageCharacter = restTemplate.getForObject(url, PageCharacter.class);

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
            List<com.pavell.rickAndMortyApi.dto.character.Result> results = pageCharacterElement.getResults();
            results.forEach(result -> {
                Character character = modelMapper.map(result, Character.class);
                Optional<Location> location = locationRepo.findByName(character.getLocation().getName());
                if (location.isPresent()) {
                    character.setLocation(location.get());
                } else {
                    Long locationMaxId = locationRepo.getMaxId();
                    character.getLocation().setId(locationMaxId == null ? 1 : locationMaxId + 1);
                }
                save(character);
            });
        });
    }
}
