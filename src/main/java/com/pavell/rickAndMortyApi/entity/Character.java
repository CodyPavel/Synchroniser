package com.pavell.rickAndMortyApi.entity;

import com.pavell.rickAndMortyApi.entity.enums.CharacterStatus;
import com.pavell.rickAndMortyApi.entity.enums.Gender;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private CharacterStatus status;

    private String species;

    private Gender gender;

    @ManyToOne()
    @JoinColumn(name = "origin_id", referencedColumnName = "id")
    private Location origin;

    @ManyToOne()
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    private Location location;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(
            name = "episode_character",
            joinColumns = @JoinColumn(name = "episode_id"),
            inverseJoinColumns = @JoinColumn(name = "character_id")
    )
    private List<Episode> episode;

    private String image;

    private String type;

    private LocalDateTime created;


}
