package com.superflousjazzhands.popularmovies.adapters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.squareup.picasso.Picasso;
import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.model.Movie;
import com.superflousjazzhands.popularmovies.views.SquaredImageView;

import java.util.List;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class PosterAdapter extends ArrayAdapter<Movie> {

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185//";

    public PosterAdapter(Activity context, List<Movie> posterPaths){
        super(context, 0, posterPaths);
    }

    @Override public View getView(final int position, View convertView, final ViewGroup parent) {
        SquaredImageView view = (SquaredImageView) convertView;
        if (view == null) {
            view = new SquaredImageView(getContext());
        }
        String url = BASE_URL + getItem(position).getPosterUrl();

        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.ic_cloud_black_36dp)
                .into(view);

        return view;
    }

}
