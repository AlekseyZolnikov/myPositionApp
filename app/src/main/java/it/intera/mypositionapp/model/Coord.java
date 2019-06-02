package it.intera.mypositionapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Coord {

    @SerializedName("lon")
    @Expose
    private float lon;
    @SerializedName("lat")
    @Expose
    private float lat;

    public float getLat() {
        return lat;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public float getLon() {
        return lon;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }
}
