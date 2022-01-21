
package com.pavell.rickAndMortyApi.dto.responseDTO.episode;

import javax.annotation.Generated;
import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponsePageEpisodeDTO {

    private ResponseInfoDTO info;
    private List<ResponseEpisodeDTO> results;

    public ResponseInfoDTO getInfo() {
        return info;
    }

    public void setInfo(ResponseInfoDTO info) {
        this.info = info;
    }

    public List<ResponseEpisodeDTO> getResults() {
        return results;
    }

    public void setResults(List<ResponseEpisodeDTO> results) {
        this.results = results;
    }
}
