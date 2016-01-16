package com.superflousjazzhands.popularmovies.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.model.Movie;

import java.util.List;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class PosterAdapter extends ArrayAdapter<Movie> {

    private final String BASE_URL = "http://image.tmdb.org/t/p/w185//";
    private LayoutInflater layoutInflater;

    public PosterAdapter(Activity context, List<Movie> posterPaths){
        super(context, 0, posterPaths);
        layoutInflater = LayoutInflater.from(context);
    }

    @Override public View getView(final int position, View convertView, final ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.poster_image, parent, false);
        }
        String url = BASE_URL + getItem(position).getPosterUrl();

        ImageView imageView = (ImageView) view.findViewById(R.id.poster_image_siv);

        Picasso.with(getContext())
                .load(url)
                .into(imageView);

        return view;
    }

}
