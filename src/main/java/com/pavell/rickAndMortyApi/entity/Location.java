package com.pavell.rickAndMortyApi.entity;

import lombok.Data;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Location {

    @Id
    @Column(name="id")
    private Long id;

    private String created;
    private String dimension;

    @Column(name="name")
    private String name;
    @Fetch(value = FetchMode.SUBSELECT)
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> residents;
    private String type;
    private String url;

}
