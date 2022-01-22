package com.pavell.rickAndMortyApi.utils;

import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;

@UtilityClass
public final class InfoUtils {

    public static InfoResponse createInfoResponse(Page page) {
        InfoResponse info = new InfoResponse();
        info.setCount(page.getTotalElements());
        info.setPages((long) page.getTotalPages());

        return info;
    }


}
