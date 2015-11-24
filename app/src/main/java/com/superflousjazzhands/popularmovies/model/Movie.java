package com.superflousjazzhands.popularmovies.model;

import java.io.Serializable;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class Movie implements Serializable {
    String title;
    String posterUrl;
    String synopsis;
    Double userRating;
    String releaseDate;

    public Movie(String title, String posterUrl, String synopsis, Double userRating, String releaseDate) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public Double getUserRating() {
        return userRating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }
}
