package it.intera.mypositionapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

class Clouds {
    @SerializedName("all")
    @Expose
    private int all;

    public int getAll() {
        return all;
    }

    public void setAll(int all) {
        this.all = all;
    }
}
