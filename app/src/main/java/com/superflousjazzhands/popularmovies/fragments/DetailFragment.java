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

    private Movie mMovie;

    ImageView mPosterImage;
    TextView mTitleTextView;
    TextView mSynopsisTextView;
    TextView mDateTextView;
    RatingBar mRatingBar;

    public DetailFragment(){};

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        mMovie = (Movie) getArguments().getSerializable("movie");
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.detail_fragment, viewGroup, false);

        mPosterImage = (ImageView) view.findViewById(R.id.poster_detail_iv);
        mTitleTextView = (TextView) view.findViewById(R.id.original_title_tv);
        mSynopsisTextView = (TextView) view.findViewById(R.id.synopsis_tv);
        mDateTextView = (TextView) view.findViewById(R.id.release_date_tv);
        mRatingBar = (RatingBar) view.findViewById(R.id.ratingBar);

        String urlString = BASE_URL + mMovie.getPosterUrl();

        Picasso.with(getActivity())
                .load(urlString)
                .placeholder(R.mipmap.ic_launcher)
                .into(mPosterImage);
        mTitleTextView.setText(mMovie.getTitle());
        mSynopsisTextView.setText(mMovie.getSynopsis());
        mDateTextView.setText(mMovie.getReleaseDate());
        mRatingBar.setRating(mMovie.getUserRating().floatValue());


        mTitleTextView.setText(mMovie.getTitle());
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

    }
}
