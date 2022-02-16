package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.CharacterResponse;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.CharacterService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.pavell.rickAndMortyApi.utils.Constants.SLASH;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CharacterControllerTest {

    private static final String URI_CHARACTER_API = "/api/character";

    @InjectMocks
    private CharacterController characterController;

    @Mock
    private CharacterService characterService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(characterController)
                .build();

    }

    @Test
    public void shouldGetPage() throws Exception {

        Stream.of(1, 3, 4, 3, 4, 3, 2, 3, 3, 3, 3, 3)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
                .entrySet()
                .stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(System.out::println);


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_CHARACTER_API)
                .params(params);

        PageResponse pageResponse = new PageResponse();

        InfoResponse infoResponse = new InfoResponse();
        pageResponse.setInfo(infoResponse);
        CharacterResponse characterResponse = createCharacterResponse();
        List<CharacterResponse> resultsList = new ArrayList<CharacterResponse>();
        resultsList.add(characterResponse);
        pageResponse.setResults(resultsList);
        when(characterService.getPage(eq(1L))).thenReturn(pageResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"http://localhost:8080/api/character/null\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"gender\":\"MALE\""));


        verify(characterService, times(1)).getPage(eq(1L));
    }

    @Test
    public void shouldGetCharacter() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_CHARACTER_API + SLASH + "1");

        CharacterResponse characterResponse = createCharacterResponse();
        when(characterService.getCharacterById(eq(1L))).thenReturn(characterResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();

        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"http://localhost:8080/api/character/null\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"gender\":\"MALE\""));


        verify(characterService, times(1)).getCharacterById(eq(1L));
    }

    @Test
    public void shouldGetCharacters() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_CHARACTER_API + SLASH + "multiple" + SLASH + "1,2");


        CharacterResponse characterResponse = createCharacterResponse();
        List<CharacterResponse> resultsList = new ArrayList<CharacterResponse>();
        resultsList.add(characterResponse);

        when(characterService.getCharacterByIds(any(String[].class))).thenReturn(resultsList);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"http://localhost:8080/api/character/null\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"gender\":\"MALE\""));


        verify(characterService, times(1)).getCharacterByIds(any(String[].class));
    }

    @Test
    public void shouldGetFilteredPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("name", "name");
        params.add("status", "status");
        params.add("species", "species");
        params.add("gender", "gender");
        params.add("type", "type");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_CHARACTER_API + SLASH)
                .params(params);

        PageResponse pageResponse = new PageResponse();

        InfoResponse infoResponse = new InfoResponse();
        pageResponse.setInfo(infoResponse);
        CharacterResponse characterResponse = createCharacterResponse();
        List<CharacterResponse> resultsList = new ArrayList<CharacterResponse>();
        resultsList.add(characterResponse);
        pageResponse.setResults(resultsList);
        when(characterService.getFilteredPage(eq(1L), eq("name"), eq("status"), eq("species"), eq("gender"), eq("type"))).thenReturn(pageResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"http://localhost:8080/api/character/null\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"gender\":\"MALE\""));

        verify(characterService, times(1)).getFilteredPage(eq(1L), eq("name"), eq("status"), eq("species"), eq("gender"), eq("type"));
    }

    private CharacterResponse createCharacterResponse() {

        CharacterResponse characterResponse = new CharacterResponse();
        characterResponse.setUrl("test/url");

        characterResponse.setGender("MALE");

        return characterResponse;
    }


}