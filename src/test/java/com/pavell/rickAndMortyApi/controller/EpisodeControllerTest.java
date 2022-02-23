package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.EpisodeResponse;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.impl.EpisodeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static com.pavell.rickAndMortyApi.utils.Constants.SLASH;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class EpisodeControllerTest {

    private static final String URI_EPISODE_API = "/api/episode";

    @InjectMocks
    private EpisodeController episodeController;

    @Mock
    private EpisodeService episodeService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(episodeController)
                .build();

    }

    @Test
    public void shouldGetPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_EPISODE_API)
                .params(params);

        PageResponse pageResponse = new PageResponse();

        InfoResponse infoResponse = new InfoResponse();
        pageResponse.setInfo(infoResponse);
        EpisodeResponse episodeResponse = createEpisodeResponse();
        List<EpisodeResponse> resultsList = new ArrayList<EpisodeResponse>();
        resultsList.add(episodeResponse);
        pageResponse.setResults(resultsList);
        when(episodeService.getPage(eq(1L))).thenReturn(pageResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));


        verify(episodeService, times(1)).getPage(eq(1L));
    }

    @Test
    public void shouldGetEpisode() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_EPISODE_API + SLASH + "1");

        EpisodeResponse episodeResponse = createEpisodeResponse();
        when(episodeService.getEpisodeById(eq(1L))).thenReturn(episodeResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));


        verify(episodeService, times(1)).getEpisodeById(eq(1L));
    }

    @Test
    public void shouldGetEpisodes() throws Exception {

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_EPISODE_API + SLASH + "multiple" + SLASH + "1,2");
        List<EpisodeResponse> resultsList = new ArrayList<EpisodeResponse>();
        EpisodeResponse episodeResponse = createEpisodeResponse();
        resultsList.add(episodeResponse);
        when(episodeService.getEpisodesByIds(any(String[].class))).thenReturn(resultsList);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));


        verify(episodeService, times(1)).getEpisodesByIds(any(String[].class));
    }

    @Test
    public void shouldGetFilteredPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("name", "name");
        params.add("episode", "episode");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_EPISODE_API + SLASH)
                .params(params);

        PageResponse pageResponse = new PageResponse();

        InfoResponse infoResponse = new InfoResponse();
        pageResponse.setInfo(infoResponse);
        EpisodeResponse episodeResponse = createEpisodeResponse();
        List<EpisodeResponse> resultsList = new ArrayList<EpisodeResponse>();
        resultsList.add(episodeResponse);
        pageResponse.setResults(resultsList);
        when(episodeService.getFilteredPage(eq("episode"), eq("name"), eq(1L))).thenReturn(pageResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));

        verify(episodeService, times(1)).getFilteredPage(eq("episode"), eq("name"), eq(1L));
    }


    private EpisodeResponse createEpisodeResponse() {

        EpisodeResponse episodeResponse = new EpisodeResponse();
        episodeResponse.setUrl("test/url");

        episodeResponse.setName("Name");

        return episodeResponse;
    }

}