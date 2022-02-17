
package com.pavell.rickAndMortyApi.response;

import com.pavell.rickAndMortyApi.entity.Episode;
import com.pavell.rickAndMortyApi.entity.Location;
import com.pavell.rickAndMortyApi.enums.CharacterStatus;
import com.pavell.rickAndMortyApi.response.common.AbstractResult;
import com.pavell.rickAndMortyApi.utils.Constants;
import lombok.ToString;

import javax.annotation.Generated;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.pavell.rickAndMortyApi.utils.Constants.*;
import static com.pavell.rickAndMortyApi.utils.TimeDateUtils.parseDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@ToString
public class CharacterResponse extends AbstractResult {

    private Long id;

    private LocalDateTime created;
    private List<String> episode;
    private String gender;
    private String image;
    private LocationResponse location;
    private String name;
    private LocationResponse origin;
    private String species;
    private String status;
    private String type;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = parseDateTime(created);
    }

    public List<String> getEpisode() {
        return episode;
    }

    public void setEpisode(List<Episode> episodes) {
        ArrayList<String> episodeStrings = new ArrayList<>();
        episodes.forEach(singleEpisode -> {
            String episodeUrl = EPISODE_URL + SLASH + singleEpisode.getId().toString();
            episodeStrings.add(episodeUrl);
        });

        this.episode = episodeStrings;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public LocationResponse getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        if (location == null) {
            this.location = null;
        } else {
            LocationResponse locationResponse = new LocationResponse();
            locationResponse.setName(location.getName());
            locationResponse.setUrl(Constants.LOCATION_URL + SLASH + location.getId());
            locationResponse.setCreated(location.getCreated().toString());
            locationResponse.setDimension(location.getDimension());
            locationResponse.setType(location.getType());
            this.location = locationResponse;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationResponse getOrigin() {
        return origin;
    }

    public void setOrigin(Location origin) {
        if (origin == null) {
            this.origin = null;
        } else {
            LocationResponse locationResponse = new LocationResponse();
            locationResponse.setName(origin.getName());
            locationResponse.setUrl(Constants.LOCATION_URL + SLASH + origin.getId());
            locationResponse.setCreated(origin.getCreated().toString());
            locationResponse.setDimension(origin.getDimension());
            locationResponse.setType(origin.getType());
            this.origin = locationResponse;
        }

    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(CharacterStatus status) {
        this.status = status.toString();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return CHARACTER_URL + SLASH + id;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
