package com.pavell.rickAndMortyApi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Episode {

    @Id
    Long id;

    private String air_date;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> characters;
    private String created;
    private String episode;
    private String name;
    private String url;

}