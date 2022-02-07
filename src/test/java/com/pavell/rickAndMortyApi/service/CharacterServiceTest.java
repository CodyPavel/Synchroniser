package com.pavell.rickAndMortyApi.service;

import com.pavell.rickAndMortyApi.cache.LocationCache;
import com.pavell.rickAndMortyApi.dto.character.CharacterDTO;
import com.pavell.rickAndMortyApi.dto.character.Info;
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
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static com.pavell.rickAndMortyApi.utils.Constants.RESOURCE_CHARACTER_URL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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

    @Mock
    private RestTemplate restTemplate;

    private ModelMapper modelMapper = new ModelMapper();

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


    @Test
    public void shouldGetCharacterById() {
        Optional<Character> character = Optional.of(getCharacterList().get(0));
        when(characterRepo.findById(eq(1L))).thenReturn(character);

        CharacterResponse characterResponse = characterService.getCharacterById(1L);

        verify(characterRepo, times(1)).findById(eq(1L));

        assertEquals("http://localhost:8080/api/character/1", characterResponse.getUrl());
        assertEquals("http://localhost:8080/api/episode/1", characterResponse.getEpisode().get(0));
        assertEquals("http://localhost:8080/api/location/1", characterResponse.getLocation().getUrl());
        assertEquals("Test char name", characterResponse.getName());
    }

    @Test
    public void shouldThrowExceptionWhileGettingCharacterById() {
        Optional<Character> character = Optional.empty();
        when(characterRepo.findById(eq(1L))).thenReturn(character);


        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> characterService.getCharacterById(1L));
        verify(characterRepo, times(1)).findById(eq(1L));

        assertEquals("404 NOT_FOUND \"User not found with id: 1\"", exception.getMessage());
    }

    @Test
    public void shouldGetCharacterByIds() {
        Optional<Character> character1 = Optional.of(getCharacterList().get(0));
        Optional<Character> character2 = Optional.of(getCharacterList().get(1));
        when(characterRepo.findById(eq(1L))).thenReturn(character1);
        when(characterRepo.findById(eq(2L))).thenReturn(character2);

        String[] strArray = new String[]{"1", "2"};
        List<CharacterResponse> responseList = characterService.getCharacterByIds(strArray);


        verify(characterRepo, times(1)).findById(eq(1L));
        verify(characterRepo, times(1)).findById(eq(2L));
    }

    @Test
    public void shouldGetFilteredPage() {
        List<Character> content = getCharacterList();
        Pageable pageable = PageRequest.of(3, 100);
        Page<Character> characterPage = new PageImpl<Character>(content, pageable, 100L);

        when(characterRepo.findAll(any(Specification.class), any(Pageable.class))).thenReturn(characterPage);
        PageResponse pageResponse = characterService.getFilteredPage(null, "Name",
                CharacterStatus.ALIVE.toString(), "Species", Gender.MALE.toString(), "Type");


        verify(characterRepo, times(1)).findAll(any(Specification.class), any(Pageable.class));

        assertEquals("http://localhost:8080/api/character/?gender=MALE&species=Species&name=Name&page=1&type=Type&status=ALIVE",
                pageResponse.getInfo().getNext());
        assertEquals(4, pageResponse.getInfo().getPages());
        assertEquals(302, pageResponse.getInfo().getCount());
        assertEquals("http://localhost:8080/api/location/1", ((CharacterResponse) pageResponse.getResults().get(0)).getLocation().getUrl());
        assertEquals("http://localhost:8080/api/character/1", ((CharacterResponse) pageResponse.getResults().get(0)).getUrl());

    }

    @Test
    public void shouldLoadData() throws IOException, ExecutionException {
        Path path = Path.of("/home/user1/IdeaProjects/Synchroniser/src/main/resources/json/character.json");
        String content = Files.readString(path, StandardCharsets.UTF_8);

        PageCharacter pageCharacter = getPageCharacter();

        when(restTemplate.getForObject(eq(RESOURCE_CHARACTER_URL), eq(PageCharacter.class))).thenReturn(pageCharacter);
        when(locationCache.getByName(anyString())).thenReturn(getLocationList().get(0));
        when(characterRepo.saveAll(anyList())).thenReturn(getCharacterList());

        characterService.loadData();

        verify(restTemplate, times(1)).getForObject(eq(RESOURCE_CHARACTER_URL), eq(PageCharacter.class));
        verify(locationCache, times(4)).getByName(anyString());
        verify(characterRepo, times(1)).saveAll(anyList());
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

        character1.setLocation(getLocationList().get(0));
        character1.setOrigin(getLocationList().get(1));

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

        character2.setLocation(getLocationList().get(2));
        character2.setOrigin(getLocationList().get(3));

        character2.setStatus(CharacterStatus.ALIVE);
        character2.setName("Test char name2");
        character2.setImage("image/test2");
        character2.setSpecies("Test Species2");
        character2.setType("Test Type2");
        character2.setGender(Gender.FEMALE);

        characterList.add(character2);

        return characterList;
    }

    private PageCharacter getPageCharacter() {
        PageCharacter pageCharacter = new PageCharacter();

        List<CharacterDTO> results = new ArrayList<>();
        CharacterDTO characterDTO1 = modelMapper.map(getCharacterList().get(0), CharacterDTO.class);
        CharacterDTO characterDTO2 = modelMapper.map(getCharacterList().get(1), CharacterDTO.class);
        results.add(characterDTO1);
        results.add(characterDTO2);

        pageCharacter.setResults(results);

        Info info = new Info();
        info.setNext(null);
        info.setPrev(null);

        pageCharacter.setInfo(info);

        return pageCharacter;
    }

    private List<Location> getLocationList() {
        List<Location> locations = new ArrayList<>();

        Location location1 = new Location();
        location1.setName("Test name");
        location1.setId(1L);
        location1.setCreated(LocalDateTime.now().minusDays(1L));
        location1.setDimension("Test Dimension");
        location1.setType("Test Type");
        locations.add(location1);

        Location location2 = new Location();
        location2.setName("Test name1");
        location2.setId(11L);
        location2.setCreated(LocalDateTime.now().minusDays(11L));
        location2.setDimension("Test Dimension1");
        location2.setType("Test Type1");
        locations.add(location2);

        Location location3 = new Location();
        location3.setName("Test name2");
        location3.setId(2L);
        location3.setCreated(LocalDateTime.now().minusDays(2L));
        location3.setDimension("Test Dimension2");
        location3.setType("Test Type2");
        locations.add(location3);

        Location origin4 = new Location();
        origin4.setName("Test name22");
        origin4.setId(22L);
        origin4.setCreated(LocalDateTime.now().minusDays(22L));
        origin4.setDimension("Test Dimension22");
        origin4.setType("Test Type22");
        locations.add(origin4);

        return locations;
    }
}