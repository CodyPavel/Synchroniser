
package com.pavell.rickAndMortyApi.dto.responseDTO.character;

import javax.annotation.Generated;
import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class ResponsePageCharacterDTO {

    private ResponseInfoDTO info;
    private List<ResponseCharacterDTO> results;

    public ResponseInfoDTO getInfo() {
        return info;
    }

    public void setInfo(ResponseInfoDTO info) {
        this.info = info;
    }

    public List<ResponseCharacterDTO> getResults() {
        return results;
    }

    public void setResults(List<ResponseCharacterDTO> results) {
        this.results = results;
    }

}
