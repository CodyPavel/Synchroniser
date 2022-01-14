drop table if exists episode;
create table episode(
                        id INT primary key,
                        air_date VARCHAR (300),
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


drop table if exists location;
create table location(
                         id INT primary key,
                         created VARCHAR(300),
                         dimension VARCHAR(300),
                         name VARCHAR(300),
                         type VARCHAR(300),
                         url VARCHAR(300)
);
drop table if exists location_residents;
create table location_residents(
                                   id serial,
                                   residents VARCHAR(300),
                                   location_id int
)













 
 