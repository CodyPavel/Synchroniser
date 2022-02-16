package com.pavell.rickAndMortyApi.utils;

import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class Constants {

    public static final String CHARACTER_URL = "http://localhost:8080/api/character";
    public static final String LOCATION_URL = "http://localhost:8080/api/location";
    public static final String EPISODE_URL = "http://localhost:8080/api/episode";

    public static final String RESOURCE_CHARACTER_URL = "https://rickandmortyapi.com/api/character";
    public static final String RESOURCE_LOCATION_URL = "https://rickandmortyapi.com/api/location";
    public static final String RESOURCE_EPISODE_URL = "https://rickandmortyapi.com/api/episode";

    public static final String REQUEST_PARAM_PAGE_DELIMITER = "?page=";
    public static final String SLASH = "/";

    public static final int SIZE = 20;

}
