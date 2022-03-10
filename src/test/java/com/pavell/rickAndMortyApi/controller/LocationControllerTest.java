package com.pavell.rickAndMortyApi.controller;

import com.pavell.rickAndMortyApi.response.LocationResponse;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import com.pavell.rickAndMortyApi.response.common.PageResponse;
import com.pavell.rickAndMortyApi.service.impl.LocationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {

    @Value("${slash.delimiter}")
    private String slash;

    private static final String URI_LOCATION_API = "/api/location";

    @InjectMocks
    private LocationController locationController;

    @Mock
    private LocationService locationService;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController)
                .build();

    }

    @Test
    public void shouldGetPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_LOCATION_API)
                .params(params);

        PageResponse pageResponse = new PageResponse();

        InfoResponse infoResponse = new InfoResponse();
        pageResponse.setInfo(infoResponse);
        LocationResponse locationResponse = createLocationResponse();
        List<LocationResponse> resultsList = new ArrayList<LocationResponse>();
        resultsList.add(locationResponse);
        pageResponse.setResults(resultsList);
        when(locationService.getPage(eq(1L))).thenReturn(pageResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));


        verify(locationService, times(1)).getPage(eq(1L));
    }

    @Test
    public void shouldGetLocation() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_LOCATION_API + slash + "1");

        LocationResponse locationResponse = createLocationResponse();
        when(locationService.getLocationById(eq(1L))).thenReturn(locationResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));

        verify(locationService, times(1)).getLocationById(eq(1L));
    }

    @Test
    public void shouldGetLocations() throws Exception {
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_LOCATION_API + slash + "multiple" + slash + "1,2");


        List<LocationResponse> resultsList = new ArrayList<LocationResponse>();
        LocationResponse locationResponse = createLocationResponse();
        resultsList.add(locationResponse);
        when(locationService.getLocationsByIds(any(String[].class))).thenReturn(resultsList);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));

        verify(locationService, times(1)).getLocationsByIds(any(String[].class));
    }

    @Test
    public void shouldGetFilteredPage() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("page", "1");
        params.add("type", "type");
        params.add("name", "name");
        params.add("dimension", "dimension");

        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URI_LOCATION_API+ slash)
                .params(params);

        PageResponse pageResponse = new PageResponse();

        InfoResponse infoResponse = new InfoResponse();
        pageResponse.setInfo(infoResponse);
        LocationResponse locationResponse = createLocationResponse();
        List<LocationResponse> resultsList = new ArrayList<LocationResponse>();
        resultsList.add(locationResponse);
        pageResponse.setResults(resultsList);
        when(locationService.getFilteredPage(eq("type"), eq("name"), eq("dimension"), eq(1L))).thenReturn(pageResponse);

        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isOk()).andReturn();
        assertTrue(result.getResponse().getContentAsString().contains("\"url\":\"test/url\""));
        assertTrue(result.getResponse().getContentAsString().contains("\"name\":\"Name\""));


        verify(locationService, times(1)).getFilteredPage(eq("type"), eq("name"), eq("dimension"), eq(1L));
    }


    private LocationResponse createLocationResponse() {

        LocationResponse locationResponse = new LocationResponse();
        locationResponse.setUrl("test/url");

        locationResponse.setName("Name");

        return locationResponse;
    }


}