package com.superflousjazzhands.popularmovies.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class Movie implements Serializable {
    String title;
    String posterUrl;
    String synopsis;
    Double userRating;
    String releaseDate;
    String runtime;
    String serverId;
    List<Trailer> trailersList;

    public Movie(String title, String posterUrl, String synopsis, Double userRating,
                 String releaseDate, String runtime, String serverId, List<Trailer> trailersList) {
        this.title = title;
        this.posterUrl = posterUrl;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.serverId = serverId;
        this.trailersList = trailersList;
    }

    public Movie(String title, String posterUrl, String synopsis, Double userRating,
                       String releaseDate, String serverId) {
        List<Trailer> trailersList = new ArrayList<Trailer>();

        this.title = title;
        this.posterUrl = posterUrl;
        this.synopsis = synopsis;
        this.userRating = userRating;
        this.releaseDate = releaseDate;
        this.runtime = "";
        this.serverId = serverId;
        this.trailersList = trailersList;
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

    public String getRuntime() { return runtime; }

    public void setRuntime(String runtime) { this.runtime = runtime; }

    public String getServerId() { return serverId; }

    public List<Trailer> getTrailersList() {
        return trailersList;
    }

    public void setTrailersList(List<Trailer> trailersList) {
        this.trailersList = trailersList;
    }
}
