
package com.pavell.graphqlpoject.model.episode;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("air_date")
    private String mAirDate;
    @SerializedName("characters")
    private List<String> mCharacters;
    @SerializedName("created")
    private String mCreated;
    @SerializedName("episode")
    private String mEpisode;
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("url")
    private String mUrl;

    public String getAirDate() {
        return mAirDate;
    }

    public void setAirDate(String airDate) {
        mAirDate = airDate;
    }

    public List<String> getCharacters() {
        return mCharacters;
    }

    public void setCharacters(List<String> characters) {
        mCharacters = characters;
    }

    public String getCreated() {
        return mCreated;
    }

    public void setCreated(String created) {
        mCreated = created;
    }

    public String getEpisode() {
        return mEpisode;
    }

    public void setEpisode(String episode) {
        mEpisode = episode;
    }

    public Long getId() {
        return mId;
    }

    public void setId(Long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
