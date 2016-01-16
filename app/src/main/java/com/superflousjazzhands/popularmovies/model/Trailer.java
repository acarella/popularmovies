package com.superflousjazzhands.popularmovies.model;

import java.io.Serializable;

/**
 * Created by TheGoodStuff on 1/3/16.
 */
public class Trailer implements Serializable{

    String name;
    String size;
    String site;

    public Trailer(String name, String size, String site) {
        this.name = name;
        this.size = size;
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getSite() {
        return site;
    }
}
