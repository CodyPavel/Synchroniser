package com.pavell.rickAndMortyApi.entity;

import lombok.Data;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
public class Location {

    @Id
    private Long id;

    private String created;
    private String dimension;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> residents;
    private String type;
    private String url;

}
