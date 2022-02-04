package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.cache.LocationCache;
import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.repo.CharacterRepo;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CharacterServiceTest {

    @InjectMocks
    private CharacterService characterService;

    @Mock
    private CharacterRepo characterRepo;

    @Mock
    private LocationRepo locationRepo;

    @Mock
    private EpisodeRepo episodeRepo;

    @Mock
    private LocationCache locationCache;

    @Test
    public void shouldSave() {
        Character character = createCharacterList().get(0);

        when(characterRepo.save(eq(character))).thenReturn(character);

        Character actualCharacter = characterService.save(character);

        assertEquals(character, actualCharacter);
    }

    @Test
    public void shouldSaveAll() {
        List<Character> characterList = createCharacterList();

        when(characterRepo.saveAll(eq(characterList))).thenReturn(characterList);

        List<Character> actualCharacterList = characterService.saveAll(characterList);

        verify(characterRepo, times(1)).saveAll(eq(characterList));
        assertEquals(characterList, actualCharacterList);
    }

    private List<Character> createCharacterList() {
        List<Character> characterList = new ArrayList<>();
        Character character1 = new Character();
        character1.setId(1L);


        Character character2 = new Character();

        characterList.add(character1);
        return characterList;
    }
}