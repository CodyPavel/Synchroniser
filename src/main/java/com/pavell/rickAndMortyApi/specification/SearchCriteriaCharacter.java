package com.pavell.rickAndMortyApi.specification;

import com.pavell.rickAndMortyApi.entity.enums.CharacterStatus;
import com.pavell.rickAndMortyApi.entity.enums.Gender;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class SearchCriteriaCharacter {

    String name;
    CharacterStatus status;
    String species;
    Gender gender;
    String type;

}
