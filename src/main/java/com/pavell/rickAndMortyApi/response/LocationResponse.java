
package com.pavell.rickAndMortyApi.response;

import com.pavell.rickAndMortyApi.response.common.AbstractResult;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Generated;
import java.time.LocalDateTime;

import static com.pavell.rickAndMortyApi.service.impl.LocationService.LOCATION_URL;
import static com.pavell.rickAndMortyApi.utils.TimeDateUtils.parseDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
@ToString
@Getter
@Setter
public class LocationResponse extends AbstractResult {

    @Value("${slash.delimiter}")
    private String slash;

    private Long id;

    private LocalDateTime created;

    private String dimension;
    private String name;
    private String type;
    private String url;

    public void setId(Long id) {
        this.url = LOCATION_URL + slash + id;
        this.id = id;
    }

    public void setCreated(String created) {
        this.created = parseDateTime(created);
    }

}
