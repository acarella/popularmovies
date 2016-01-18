package com.superflousjazzhands.popularmovies.Network;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.superflousjazzhands.popularmovies.R;
import com.superflousjazzhands.popularmovies.model.Movie;
import com.superflousjazzhands.popularmovies.model.MovieColumns;
import com.superflousjazzhands.popularmovies.model.MovieProvider;
import com.superflousjazzhands.popularmovies.model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TheGoodStuff on 1/18/16.
 */
public class NetworkManager  {

    interface NetworkManagerDelegate{
        void onCallbackComplete();
    }

    private static final String TAG = NetworkManager.class.getSimpleName();
    private NetworkManager instance;
    private static RequestQueue mQueue;
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static SharedPreferences mSharedPreferences;
    private static Context mContext;

    private NetworkManager(){}

    public NetworkManager getNetworkManager(Context context){

        if (instance == null){
            instance = new NetworkManager();
            mContext = context;
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
            mQueue = Volley.newRequestQueue(mContext);
        }

        return instance;
    }

    public void getAllPosters(final NetworkManagerDelegate callback){

        String mUrlString = BASE_URL + "discover/movie?" +
                "api_key=" + mContext.getResources().getString(R.string.movies_api_key);

        if (mSharedPreferences.getString(mContext.getResources().getString(R.string.pref_syncConnectionType), "").equals("1")) {
            mUrlString += "&sort_by=popularity.desc";
        } else {
            mUrlString += "&sort_by=vote_count.desc";
        }

        JsonArrayRequest jsonRequest = new JsonArrayRequest(mUrlString, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    if (response.equals(null)) {
                        return;
                    }

                    final String TITLE_KEY = "original_title";
                    final String URL_KEY = "poster_path";
                    final String SYNOPSIS_KEY = "overview";
                    final String USERRATING_KEY = "vote_average";
                    final String RELEASEDATE_KEY = "release_date";
                    final String SERVER_ID_KEY = "id";

                    JSONArray topMoviesArray = response;
                    int numOfMovies = topMoviesArray.length();
                    String[] idArray = new String [numOfMovies];
                    ArrayList<ContentProviderOperation> batchOperations = new ArrayList<>();
                    for (int i = 0; i < numOfMovies; i++) {
                        JSONObject movieObject = topMoviesArray.getJSONObject(i);
                        ContentProviderOperation.Builder builder = ContentProviderOperation.newInsert(
                                MovieProvider.Movies.CONTENT_URI);
                        builder.withValue(MovieColumns.TITLE, movieObject.getString(TITLE_KEY));
                        builder.withValue(MovieColumns.POSTER_URL, movieObject.getString(URL_KEY));
                        builder.withValue(MovieColumns.SYNOPSIS, movieObject.getString(SYNOPSIS_KEY));
                        builder.withValue(MovieColumns.USER_RATING, movieObject.getDouble(USERRATING_KEY));
                        builder.withValue(MovieColumns.RELEASE_DATE, movieObject.getString(RELEASEDATE_KEY));
                        builder.withValue(MovieColumns.SERVER_ID, movieObject.getString(SERVER_ID_KEY));
                        idArray[i] = movieObject.getString(SERVER_ID_KEY);
                        batchOperations.add(builder.build());
                        try{
                            mContext.getContentResolver().applyBatch(MovieProvider.AUTHORITY, batchOperations);
                        } catch(RemoteException | OperationApplicationException e){
                            Log.e(TAG, "Error applying batch insert", e);
                        }

                    }

                    for (String id : idArray){
                        getAllMovieInfo(id);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        mQueue.add(jsonRequest);
    }

    public void getAllMovieInfo(final String movieId) {
        String mUrlString = BASE_URL + "movie/" +
                movieId + "?" +
                "&api_key=" + mContext.getResources().getString(R.string.movies_api_key) +
                "&append_to_response=trailers";

        JsonObjectRequest jsonRequest = new JsonObjectRequest(mUrlString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            final String RUNTIME = "runtime";
                            final String TRAILERS = "trailers";
                            final String QUICKTIME = "quicktime";
                            final String YOUTUBE = "youtube";

                            final String TRAILER_NAME = "name";
                            final String TRAILER_SIZE = "size";

                            String[] projections = {MovieColumns._ID};
                            String selectionClause = MovieColumns.SERVER_ID + " = ?";
                            String[] selectionArgs = {movieId};

                            Cursor cursor = mContext.getContentResolver().query(MovieProvider.Movies.CONTENT_URI,
                                    projections, selectionClause, selectionArgs, null
                            );


                            //TODO: How best to relate the trailers table to the movies table??


                            List<Trailer> youtubeTrailersList = new ArrayList<Trailer>();
                            List<Trailer> quicktimeTrailersList = new ArrayList<Trailer>();

                            String runtimeString = response.getString(RUNTIME);
                            Log.v(TAG, "Runtime String: " + runtimeString);
                            //movie.setRuntime(runtimeString);

                            JSONObject trailers = response.getJSONObject(TRAILERS);
                            JSONArray quicktimeTrailers = trailers.getJSONArray(QUICKTIME);
                            JSONArray youtubeTrailers = trailers.getJSONArray(YOUTUBE);

                            for (int i = 0; i < quicktimeTrailers.length(); i++) {
                                JSONObject trailerJSONObject = quicktimeTrailers.getJSONObject(i);
                                Trailer trailer = new Trailer(trailerJSONObject.getString(TRAILER_NAME),
                                        trailerJSONObject.getString(TRAILER_SIZE),
                                        QUICKTIME);
                                quicktimeTrailersList.add(trailer);
                            }

                            for (int j = 0; j < youtubeTrailers.length(); j++) {
                                JSONObject trailerJSONObject = youtubeTrailers.getJSONObject(j);
                                Trailer trailer = new Trailer(trailerJSONObject.getString(TRAILER_NAME),
                                        trailerJSONObject.getString(TRAILER_SIZE),
                                        QUICKTIME);
                                youtubeTrailersList.add(trailer);
                            }

                            List trailersList = new ArrayList<Trailer>();
                            trailersList.addAll(quicktimeTrailersList);
                            trailersList.addAll(youtubeTrailersList);
                            //movie.setTrailersList(trailersList);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse (VolleyError error){
                error.printStackTrace();
            }
        }
        );
    }
}
