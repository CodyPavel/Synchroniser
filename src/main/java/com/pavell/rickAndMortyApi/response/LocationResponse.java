
package com.pavell.rickAndMortyApi.response;

import com.pavell.rickAndMortyApi.response.common.AbstractResult;
import com.pavell.rickAndMortyApi.utils.Constants;
import lombok.ToString;

import javax.annotation.Generated;
import java.time.LocalDateTime;

import static com.pavell.rickAndMortyApi.utils.Constants.LOCATION_URL;
import static com.pavell.rickAndMortyApi.utils.Constants.SLASH;
import static com.pavell.rickAndMortyApi.utils.TimeDateUtils.parseDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@ToString
public class LocationResponse extends AbstractResult {

    private Long id;

    private LocalDateTime created;

    private String dimension;
    private String name;
    private String type;
    private String url;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.url = LOCATION_URL + SLASH + id;
        this.id = id;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = parseDateTime(created);
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
