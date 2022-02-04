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

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CharacterControllerTest {

    private ModelMapper modelMapper = new ModelMapper();

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

//        PageCharacter character = new ObjectMapper().readValue(result.getResponse().getContentAsString(), PageCharacter.class);


        verify(characterService, times(1)).getPage(eq(1L));
//        assertEquals(1, 1);

    }

    private CharacterResponse createCharacterResponse() {

        CharacterResponse characterResponse = new CharacterResponse();
        characterResponse.setUrl("test/url");

        characterResponse.setGender("MALE");

        return characterResponse;
    }


}