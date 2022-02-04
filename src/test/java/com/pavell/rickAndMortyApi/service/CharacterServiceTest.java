package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.cache.LocationCache;
import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.enums.CharacterStatus;
import com.pavell.rickAndMortyApi.enums.Gender;
import com.pavell.rickAndMortyApi.repo.CharacterRepo;
import com.pavell.rickAndMortyApi.repo.EpisodeRepo;
import com.pavell.rickAndMortyApi.repo.LocationRepo;
import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
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
        Character character = getCharacterList().get(0);

        when(characterRepo.save(eq(character))).thenReturn(character);

        Character actualCharacter = characterService.save(character);

        assertEquals(character, actualCharacter);
    }

    @Test
    public void shouldSaveAll() {
        List<Character> characterList = getCharacterList();

        when(characterRepo.saveAll(eq(characterList))).thenReturn(characterList);

        List<Character> actualCharacterList = characterService.saveAll(characterList);

        verify(characterRepo, times(1)).saveAll(eq(characterList));
        assertEquals(characterList, actualCharacterList);
    }

    @Test
    public void shouldGetPage() {
        List<Character> content = getCharacterList();
        Pageable pageable = PageRequest.of(3, 100);
        Page<Character> characterPage = new PageImpl<Character>(content, pageable, 100L);

        when(characterRepo.findAll(any(Pageable.class))).thenReturn(characterPage);
        PageResponse pageResponse = characterService.getPage(1L);

        verify(characterRepo, times(1)).findAll(any(Pageable.class));

        assertEquals("http://localhost:8080/api/character?page=2", pageResponse.getInfo().getNext());
        assertEquals(4, pageResponse.getInfo().getPages());
        assertEquals(302, pageResponse.getInfo().getCount());
        assertEquals("http://localhost:8080/api/location/1", ((CharacterResponse) pageResponse.getResults().get(0)).getLocation().getUrl());
        assertEquals("http://localhost:8080/api/character/1", ((CharacterResponse) pageResponse.getResults().get(0)).getUrl());
    }
    

    private List<Character> getCharacterList() {
        List<Character> characterList = new ArrayList<>();
        Character character1 = new Character();
        character1.setId(1L);
        character1.setCreated(LocalDateTime.now().minusDays(1L));

        List<Episode> episodes = new ArrayList<>();
        Episode episode = new Episode();
        episode.setId(1L);
        episodes.add(episode);
        character1.setEpisode(episodes);

        Location location = new Location();
        location.setName("Test name");
        location.setId(1L);
        location.setCreated(LocalDateTime.now().minusDays(1L));
        location.setDimension("Test Dimension");
        location.setType("Test Type");
        character1.setLocation(location);

        Location origin = new Location();
        origin.setName("Test name1");
        origin.setId(11L);
        origin.setCreated(LocalDateTime.now().minusDays(11L));
        origin.setDimension("Test Dimension1");
        origin.setType("Test Type1");
        character1.setOrigin(origin);

        character1.setStatus(CharacterStatus.DEAD);
        character1.setName("Test char name");
        character1.setImage("image/test");
        character1.setSpecies("Test Species");
        character1.setType("Test Type");
        character1.setGender(Gender.MALE);

        characterList.add(character1);

        Character character2 = new Character();
        character2.setId(2L);
        character2.setCreated(LocalDateTime.now().minusDays(2L));

        List<Episode> episodes2 = new ArrayList<>();
        Episode episode2 = new Episode();
        episode2.setId(2L);
        episodes2.add(episode2);
        character2.setEpisode(episodes2);

        Location location2 = new Location();
        location2.setName("Test name2");
        location2.setId(2L);
        location2.setCreated(LocalDateTime.now().minusDays(2L));
        location2.setDimension("Test Dimension2");
        location2.setType("Test Type2");
        character2.setLocation(location2);

        Location origin2 = new Location();
        origin2.setName("Test name22");
        origin2.setId(22L);
        origin2.setCreated(LocalDateTime.now().minusDays(22L));
        origin2.setDimension("Test Dimension22");
        origin2.setType("Test Type22");
        character2.setOrigin(origin2);

        character2.setStatus(CharacterStatus.ALIVE);
        character2.setName("Test char name2");
        character2.setImage("image/test2");
        character2.setSpecies("Test Species2");
        character2.setType("Test Type2");
        character2.setGender(Gender.FEMALE);

        characterList.add(character2);

        return characterList;
    }
}