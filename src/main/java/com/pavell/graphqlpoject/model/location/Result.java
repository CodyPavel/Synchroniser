
package com.pavell.graphqlpoject.model.location;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.SerializedName;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class Result {

    @SerializedName("created")
    private String mCreated;
    @SerializedName("dimension")
    private String mDimension;
    @SerializedName("id")
    private Long mId;
    @SerializedName("name")
    private String mName;
    @SerializedName("residents")
    private List<String> mResidents;
    @SerializedName("type")
    private String mType;
    @SerializedName("url")
    private String mUrl;

    public String getCreated() {
        return mCreated;
    }

    public void setCreated(String created) {
        mCreated = created;
    }

    public String getDimension() {
        return mDimension;
    }

    public void setDimension(String dimension) {
        mDimension = dimension;
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

    public List<String> getResidents() {
        return mResidents;
    }

    public void setResidents(List<String> residents) {
        mResidents = residents;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getUrl() {
        return mUrl;
    }

    public void setUrl(String url) {
        mUrl = url;
    }

}
