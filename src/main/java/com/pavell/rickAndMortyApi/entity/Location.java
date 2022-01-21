package com.pavell.rickAndMortyApi.entity;

import lombok.Data;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
public class Location {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String type;
    private String dimension;

    @OneToMany(  mappedBy = "location", cascade = CascadeType.ALL)
    private List<Character> residents;

    private LocalDateTime created;

}
