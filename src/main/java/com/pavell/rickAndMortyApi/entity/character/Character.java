package com.pavell.rickAndMortyApi.entity.character;


import com.pavell.rickAndMortyApi.entity.Location;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Character {

    @Id
    private Long id;

    private String created;

    @ElementCollection
    private List<String> episode;
    private String gender;
    private String image;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id", insertable = false, updatable = false)
    private Location location;
    private String name;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "id")
    private Origin origin;
    private String species;
    private String status;
    private String type;
    private String url;

}
