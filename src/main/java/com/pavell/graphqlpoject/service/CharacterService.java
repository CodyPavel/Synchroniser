package com.pavell.graphqlpoject.service;

import com.pavell.graphqlpoject.entity.character.Character;
import com.pavell.graphqlpoject.repo.CharacterRepo;
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
