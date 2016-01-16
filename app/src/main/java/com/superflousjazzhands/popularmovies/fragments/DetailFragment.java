package com.superflousjazzhands.popularmovies.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.adapters.PosterAdapter;
import com.superflousjazzhands.popularmovies.adapters.TrailerAdapter;
import com.superflousjazzhands.popularmovies.model.Movie;
import com.superflousjazzhands.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class DetailFragment extends Fragment {

    private final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185//";

    private Movie mMovie;

    ImageView mPosterImage;
    TextView mTitleTextView;
    TextView mSynopsisTextView;
    TextView mDateTextView;
    TextView mRatingTextView;
    TextView mRunTimeTextView;

    TrailerAdapter mTrailerAdapter;
    RecyclerView mRecyclerView;

    public static String mUrlString;

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
        mRatingTextView = (TextView) view.findViewById(R.id.rating_tv);
        mRunTimeTextView = (TextView) view.findViewById(R.id.runtime_tv);
        //mRecyclerView = (RecyclerView) view.findViewById(R.id.trailers_rv);

//       LinearLayoutManager llm = new LinearLayoutManager(getActivity());
//       llm.setOrientation(LinearLayoutManager.VERTICAL);
//        mRecyclerView.setHasFixedSize(true);
//        mRecyclerView.setLayoutManager(llm);
//
//        mTrailerAdapter = new TrailerAdapter(mMovie.getTrailersList());
//        mRecyclerView.setAdapter(mTrailerAdapter);

        String urlString = BASE_POSTER_URL + mMovie.getPosterUrl();

        Picasso.with(getActivity())
                .load(urlString)
                .placeholder(R.mipmap.ic_launcher)
                .into(mPosterImage);
        mTitleTextView.setText(mMovie.getTitle());
        mSynopsisTextView.setText(mMovie.getSynopsis());
        mDateTextView.setText(mMovie.getReleaseDate().substring(0, 4));
        String userRating = String.format("%.1f", mMovie.getUserRating());
        mRatingTextView.setText("" + userRating + "/10");
        mTitleTextView.setText(mMovie.getTitle());
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        mUrlString = "https://api.themoviedb.org/3/movie/" +
                mMovie.getServerId() + "?" +
                "&api_key=" + getResources().getString(R.string.movies_api_key) +
                "&append_to_response=trailers";

        FetchDetailsTask fetchDetailsTask = new FetchDetailsTask();
        fetchDetailsTask.execute();
    }

    public void createTrailerViews(List<Trailer> trailerList){
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.trailer_row, null);
        for (Trailer trailer: trailerList){
            TextView textView = (TextView) view.findViewById(R.id.trailer_name_tv);
            textView.setText(trailer.getName());
            LinearLayout insertPoint = (LinearLayout) view.findViewById(R.id.reviews_insert_LL);
            insertPoint.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                    , ViewGroup.LayoutParams.MATCH_PARENT));
        }
    }
    public class FetchDetailsTask extends AsyncTask<URL, Integer, String> {
        private final String TAG = FetchDetailsTask.class.getSimpleName();

        @Override
        protected String doInBackground(URL... params) {
            String jsonString = "";
            try {
                jsonString = downloadUrl(mUrlString);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

            return jsonString;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                parseRuntimeFromJsonString(result);
                mRunTimeTextView.setText(mMovie.getRuntime());
                createTrailerViews(mMovie.getTrailersList());
            } catch (org.json.JSONException e) {
                Log.e(TAG, e.toString());
            }
        }

        private String downloadUrl(String myurl) throws IOException {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            try {
                URL url = new URL(myurl);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    movieJsonStr = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                    Log.d(TAG, line);
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonStr = null;
                }
                movieJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.v(TAG, "Error: " + e);
                movieJsonStr = null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(TAG, "Error closing stream: " + e);
                    }
                }
            }
            return movieJsonStr;
        }

        private void parseRuntimeFromJsonString(String jsonString) throws JSONException {
            if (jsonString == null) { return; }

            final String RUNTIME = "runtime";
            final String TRAILERS = "trailers";
            final String QUICKTIME = "quicktime";
            final String YOUTUBE = "youtube";

            final String TRAILER_NAME = "name";
            final String TRAILER_SIZE = "size";

            List<Trailer> youtubeTrailersList = new ArrayList<Trailer>();
            List<Trailer> quicktimeTrailersList = new ArrayList<Trailer>();

            JSONObject jsonObject = new JSONObject(jsonString);
            String runtimeString = jsonObject.getString(RUNTIME);
            Log.v(TAG, "Runtime String: " + runtimeString);
            mMovie.setRuntime(runtimeString);

            JSONObject trailers = jsonObject.getJSONObject(TRAILERS);
            JSONArray quicktimeTrailers = trailers.getJSONArray(QUICKTIME);
            JSONArray youtubeTrailers = trailers.getJSONArray(YOUTUBE);

            for (int i = 0; i < quicktimeTrailers.length(); i++){
                JSONObject trailerJSONObject = quicktimeTrailers.getJSONObject(i);
                Trailer trailer = new Trailer(trailerJSONObject.getString(TRAILER_NAME),
                                                trailerJSONObject.getString(TRAILER_SIZE),
                                                QUICKTIME);
                quicktimeTrailersList.add(trailer);
            }

            for (int j = 0; j < youtubeTrailers.length(); j++){
                JSONObject trailerJSONObject = youtubeTrailers.getJSONObject(j);
                Trailer trailer = new Trailer(trailerJSONObject.getString(TRAILER_NAME),
                        trailerJSONObject.getString(TRAILER_SIZE),
                        QUICKTIME);
                youtubeTrailersList.add(trailer);
            }

            List trailersList = new ArrayList<Trailer>();
            trailersList.addAll(quicktimeTrailersList);
            trailersList.addAll(youtubeTrailersList);
            mMovie.setTrailersList(trailersList);
        }
    }
}
