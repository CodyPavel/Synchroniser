
package com.pavell.rickAndMortyApi.dto.location;

import java.util.List;
import javax.annotation.Generated;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PageLocation {

    private Info info;
    private List<LocationDTO> results;

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public List<LocationDTO> getResults() {
        return results;
    }

    public void setResults(List<LocationDTO> results) {
        this.results = results;
    }

}
