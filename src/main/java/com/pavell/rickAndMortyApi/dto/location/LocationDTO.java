
package com.pavell.rickAndMortyApi.dto.location;

import com.pavell.rickAndMortyApi.utils.TimeDateUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.annotation.Generated;

import static com.pavell.rickAndMortyApi.utils.TimeDateUtils.parseDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class LocationDTO {

    private LocalDateTime created;
    private String dimension;
    private String name;
    private String type;
    private String url;

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
