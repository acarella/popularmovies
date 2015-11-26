package com.superflousjazzhands.popularmovies.fragments;

/**
 * Created by antoniocarella on 11/17/15.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.adapters.PosterAdapter;
import com.superflousjazzhands.popularmovies.model.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

/**
 * Created by antoniocarella on 11/17/15.
 */
public class PostersFragment extends Fragment {
    private final String TAG = PostersFragment.class.getSimpleName();
    private List<Movie> mMovies;
    private GridView mGridView;

    // add your api key here
    private final String API_KEY = getResources().getString(R.string.movies_api_key);
    private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie?" +
            "api_key=" + API_KEY ;

    public static String urlString;

    public PostersFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup,
                             Bundle savedInstanceState) {
        View view = layoutInflater.inflate(R.layout.posters_fragement, viewGroup, false);
        mGridView = (GridView) view.findViewById(R.id.posters_gv);
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = mMovies.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("movie", movie);
                DetailFragment detailFragment = new DetailFragment();
                detailFragment.setArguments(bundle);
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, detailFragment);
                fragmentTransaction.addToBackStack("detail_view").commit();
            }
        });
        return view;
    }

    @Override
    public void onStart(){
        super.onStart();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        if (sharedPreferences.getString(getResources().getString(R.string.pref_syncConnectionType), "").equals("1")) {
            urlString = BASE_URL + "&sort_by=popularity.desc";
        } else {
            urlString = BASE_URL + "&sort_by=vote_count.desc";
        }
        FetchPosterPathsTask fetchDataTask = new FetchPosterPathsTask();
        fetchDataTask.execute();
    }

    public class FetchPosterPathsTask extends AsyncTask<URL, Integer, String> {
        private final String TAG = FetchPosterPathsTask.class.getSimpleName();

        @Override
        protected String doInBackground(URL... params) {
            String jsonString = "";
            try {
                jsonString = downloadUrl(urlString);
            } catch (IOException e) {
                Log.e(TAG, e.toString());
            }

            return jsonString;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            try {
                mMovies = Arrays.asList(parseMoviesFromJsonString(result));
                PosterAdapter adapter = new PosterAdapter(getActivity(), mMovies);
                mGridView.setAdapter(adapter);
            } catch (org.json.JSONException e){
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
                Log.e(TAG, "Error: " + e);
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

        private Movie[] parseMoviesFromJsonString(String jsonString) throws JSONException {

            final String RESUlTS_LIST = "results";
            final String TITLE_KEY = "original_title";
            final String URL_KEY = "poster_path";
            final String SYNOPSIS_KEY = "overview";
            final String USERRATING_KEY = "vote_average";
            final String RELEASEDATE_KEY = "release_date";

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray topMoviesArray = jsonObject.getJSONArray(RESUlTS_LIST);
            int numOfMovies = topMoviesArray.length();
            Movie[] movies = new Movie[numOfMovies];
            for (int i = 0; i < numOfMovies; i++){
                JSONObject movieObject = topMoviesArray.getJSONObject(i);
                String title = movieObject.getString(TITLE_KEY);
                String posterUrl = movieObject.getString(URL_KEY);
                String synopsis = movieObject.getString(SYNOPSIS_KEY);
                Double userRating = movieObject.getDouble(USERRATING_KEY);
                String releaseDate = movieObject.getString(RELEASEDATE_KEY);

                Movie movie = new Movie(title, posterUrl, synopsis, userRating, releaseDate);
                movies[i] = movie;
            }
            return movies;
        }
    }
}
