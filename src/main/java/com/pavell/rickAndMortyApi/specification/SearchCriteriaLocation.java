package com.pavell.rickAndMortyApi.specification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SearchCriteriaLocation {
    String type;
    String name;
    String dimension;

}
