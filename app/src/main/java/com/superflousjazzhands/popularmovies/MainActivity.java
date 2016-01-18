package com.superflousjazzhands.popularmovies;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.superflousjazzhands.popularmovies.fragments.DetailFragment;
import com.superflousjazzhands.popularmovies.fragments.PostersFragment;
import com.superflousjazzhands.popularmovies.fragments.SettingsFragment;
import com.superflousjazzhands.popularmovies.model.Movie;

public class MainActivity extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener,
    PostersFragment.Delegate {

    Boolean isDualPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        View detailView = findViewById(R.id.rating_tv);
        isDualPane = detailView != null &&
                detailView.getVisibility() == View.VISIBLE;

        getFragmentManager().addOnBackStackChangedListener(this);
        shouldDisplayHomeUp();

        //TODO: call SyncService here, do the following in its call back:



        if (findViewById(R.id.fragment_container) != null) {

            if (savedInstanceState != null) {
                return;
            }

            getFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new PostersFragment(), null)
                    .addToBackStack(null)
                    .commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_settings:
                displayMenuFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void displayMenuFragment(){
        SettingsFragment settingsFragment = new SettingsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, settingsFragment);
        transaction.addToBackStack(null).commit();
    }

    @Override
    public void onBackStackChanged() {
        shouldDisplayHomeUp();
    }

    public void shouldDisplayHomeUp(){
        //Enable Up button only  if there are entries in the back stack
        boolean canGoBack = getFragmentManager().getBackStackEntryCount()>0;
        getSupportActionBar().setDisplayHomeAsUpEnabled(canGoBack);
    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        getFragmentManager().popBackStack();
        return true;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    /*
    * PostersFragement.java delegate
    * */

    @Override
    public void onItemTapped(PostersFragment postersFragment, Movie movie){

        if (isDualPane){
            getFragmentManager().beginTransaction()
                    .replace(R.id.detail_container, new DetailFragment())
                    .commit();
        } else {
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new DetailFragment())
                    .commit();
        }
    }
}
