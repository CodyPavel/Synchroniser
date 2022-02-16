
package com.pavell.rickAndMortyApi.response;

import com.pavell.rickAndMortyApi.entity.Character;
import com.pavell.rickAndMortyApi.response.common.AbstractResult;

import javax.annotation.Generated;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.pavell.rickAndMortyApi.utils.TimeDateUtils.parseDateTime;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class EpisodeResponse extends AbstractResult {

    private Long id;
    private Date air_date;

    private List<String> characters;

    private LocalDateTime created;
    private String episode;
    private String name;
    private String url;

    public List<String> getCharacters() {
        return characters;
    }

    public void setCharacters(List<Character> characters) {
        ArrayList arrayList = new ArrayList();
        characters.forEach(character -> arrayList.add(character.getName()));
        this.characters = arrayList;
    }

    public Date getAir_date() {
        return air_date;
    }

    public void setAir_date(Date air_date) throws ParseException {
        this.air_date = air_date;
    }


    public void setAir_date(String air_date) throws ParseException {

        try {
            DateFormat format = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
            Date newAirDate = format.parse(air_date);
            this.air_date = newAirDate;
        } catch (Exception e) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            Date newAirDate = format.parse(air_date);
            this.air_date = newAirDate;
        }

    }


    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public void setCreated(String created) {
        this.created = parseDateTime(created);
    }

    public String getEpisode() {
        return episode;
    }

    public void setEpisode(String episode) {
        this.episode = episode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
