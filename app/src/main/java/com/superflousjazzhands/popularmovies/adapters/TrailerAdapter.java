package com.superflousjazzhands.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.model.Trailer;

import java.util.List;

/**
 * Created by TheGoodStuff on 12/27/15.
 */
public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    private List<Trailer> trailerList;

    public TrailerAdapter(List<Trailer> trailerList){
        super();
        this.trailerList = trailerList;
    }

    @Override
    public int getItemCount(){
        return trailerList.size();
    }

    @Override
    public TrailerAdapter.TrailerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.trailer_row, parent, false);

        return new TrailerViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.TrailerViewHolder holder, int position) {
        Trailer trailer = trailerList.get(position);
        holder.trailerNameTV.setText(trailer.getName());
    }

    public static class TrailerViewHolder extends RecyclerView.ViewHolder{
        TextView trailerNameTV;

        public TrailerViewHolder(View v){
            super(v);
            trailerNameTV = (TextView) v.findViewById(R.id.trailer_name_tv);
        }
    }
}
