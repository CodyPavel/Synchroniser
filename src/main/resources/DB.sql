drop table if exists episode;
create table episode(
                        id INT primary key,
                        air_date VARCHAR (300),
                        characters text,
                        created VARCHAR (300),
                        episode VARCHAR (300),
                        name VARCHAR (300),
                        url VARCHAR (300)
);

drop table if exists episode_characters;
create table episode_characters(
                                   id serial,
                                   characters VARCHAR(300),
                                   episode_id int
);
