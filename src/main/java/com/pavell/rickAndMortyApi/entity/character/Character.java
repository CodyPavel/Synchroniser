package com.pavell.rickAndMortyApi.entity.character;

import com.pavell.rickAndMortyApi.entity.Location;
import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Character {

    @Id
    private Long id;

    private String created;
    @Fetch(value = FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> episode;

    private String gender;

    private String image;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(
            name = "character_location",
            joinColumns = @JoinColumn(
                    name = "character_id",
                    referencedColumnName = "id"
            ),
            inverseJoinColumns = @JoinColumn(
                    name = "location_id",
                    referencedColumnName = "id"
            )
    )
    private Location location;

    private String name;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "id")
    private Origin origin;

    private String species;

    private String status;

    private String type;

    private String url;

}
