
package com.pavell.rickAndMortyApi.dto.responseDTO.location;

import javax.annotation.Generated;
import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponsePageLocationDTO {

    private ResponseInfoDTO info;
    private List<ResponseLocationDTO> results;

    public ResponseInfoDTO getInfo() {
        return info;
    }

    public void setInfo(ResponseInfoDTO info) {
        this.info = info;
    }

    public List<ResponseLocationDTO> getResults() {
        return results;
    }

    public void setResults(List<ResponseLocationDTO> results) {
        this.results = results;
    }

}
