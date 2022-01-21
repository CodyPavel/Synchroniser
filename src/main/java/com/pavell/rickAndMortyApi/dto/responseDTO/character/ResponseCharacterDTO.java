
package com.pavell.rickAndMortyApi.dto.responseDTO.character;

import javax.annotation.Generated;
import java.time.LocalDateTime;
import java.util.List;

import static com.pavell.rickAndMortyApi.utils.TimeDateUtils.parseDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponseCharacterDTO {

    private LocalDateTime created;
    private List<String> episode;
    private String gender;
    private String image;
    private ResponseLocationDTO location;
    private String name;
    private ResponseOriginDTO origin;
    private String species;
    private String status;
    private String type;
    private String url;

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = parseDateTime(created);
    }

    public List<String> getEpisode() {
        return episode;
    }

    public void setEpisode(List<String> episode) {
        this.episode = episode;
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

    public ResponseLocationDTO getLocation() {
        return location;
    }

    public void setLocation(ResponseLocationDTO location) {
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ResponseOriginDTO getOrigin() {
        return origin;
    }

    public void setOrigin(ResponseOriginDTO origin) {
        this.origin = origin;
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

    public void setStatus(String status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
