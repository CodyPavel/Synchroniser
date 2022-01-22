package com.pavell.rickAndMortyApi.response.common;


import com.pavell.rickAndMortyApi.response.common.AbstractResult;
import com.pavell.rickAndMortyApi.response.common.InfoResponse;

import javax.annotation.Generated;
import java.util.List;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class PageResponse  {

    private InfoResponse info;
    private List<? extends AbstractResult> results;

    public InfoResponse getInfo() {
        return info;
    }

    public void setInfo(InfoResponse info) {
        this.info = info;
    }

    public List<? extends AbstractResult> getResults() {
        return results;
    }

    public void setResults(List<? extends AbstractResult> results) {
        this.results = results;
    }

}
