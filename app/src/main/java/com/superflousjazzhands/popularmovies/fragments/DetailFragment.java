package com.superflousjazzhands.popularmovies.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.model.Movie;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class DetailFragment extends Fragment {

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185//";

    private Movie movie;

    ImageView posterImage;
    TextView titleTextView;
    TextView synopsisTextView;
    TextView dateTextView;
    RatingBar ratingBar;

    public DetailFragment(){};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        movie = (Movie) getArguments().getSerializable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.detail_fragment, viewGroup, false);

        posterImage = (ImageView) view.findViewById(R.id.poster_detail_iv);
        titleTextView = (TextView) view.findViewById(R.id.original_title_tv);
        synopsisTextView = (TextView) view.findViewById(R.id.synopsis_tv);
        dateTextView = (TextView) view.findViewById(R.id.release_date_tv);
        ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        String urlString = BASE_URL + movie.getPosterUrl();

        Picasso.with(getActivity())
                .load(urlString)
                .placeholder(R.mipmap.ic_launcher)
                .into(posterImage);
        titleTextView.setText(movie.getTitle());
        synopsisTextView.setText(movie.getSynopsis());
        dateTextView.setText(movie.getReleaseDate());
        ratingBar.setRating(movie.getUserRating().floatValue());

        titleTextView.setText(movie.getTitle());
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

    }
}
