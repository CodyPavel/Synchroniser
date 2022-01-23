package com.pavell.rickAndMortyApi.utils;

import com.pavell.rickAndMortyApi.response.common.InfoResponse;
import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;

import javax.persistence.Entity;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@UtilityClass
public final class ParamsBuilder {


    public static void setRequestParamsToPrevAndNext(InfoResponse info, Map<String, String> params) {
        String next;
        String prev;
        AtomicReference<String> allParams = new AtomicReference<>(StringUtils.EMPTY);
        params.forEach((key, value) -> {

            String param = key + "=" + value;
            if (StringUtils.EMPTY.equalsIgnoreCase(allParams.get())&& value != null) {
                    allParams.set("/?" + param);
            }else if (value != null) {
                allParams.set(allParams + "&" + param);
            }
        });

        if (info.getNext() != null) {
            info.setNext(info.getNext() + allParams);
        }

        if (info.getPrev() != null) {
            info.setPrev(info.getPrev() + allParams);
        }
    }

    public static void isSinglePage(int totalPages, InfoResponse info) {
        if (totalPages == 1 || totalPages == 0) {
            info.setNext(null);
            info.setPrev(null);
        }
    }

}
