package com.superflousjazzhands.popularmovies.Sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;
import android.util.Log;

import com.superflousjazzhands.popularmovies.R;

import java.io.BufferedReader;
import java.net.HttpURLConnection;

/**
 * Created by TheGoodStuff on 1/16/16.
 */
public class PopularMoviesSyncAdaptor extends AbstractThreadedSyncAdapter {
    public final String TAG = PopularMoviesSyncAdaptor.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private final String API_ARGUMENT_AND_KEY = "api_key=" + getContext().getResources().getString(R.string.movies_api_key);


    public PopularMoviesSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
    }

    public void onPerformSync(Account account, Bundle extras,
                                       String authority, ContentProviderClient provider, SyncResult syncResult){

        Log.d("LOG", "Starting sync");

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;


    }

}
