package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.entity.character.Character;
import com.pavell.rickAndMortyApi.repo.CharacterRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CharacterService {


    private CharacterRepo characterRepo;

    public CharacterService(CharacterRepo characterRepo) {
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

}
