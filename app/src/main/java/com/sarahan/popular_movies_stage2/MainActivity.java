package com.sarahan.popular_movies_stage2;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.sarahan.popular_movies_stage2.Loaders.LoadPopular;
import com.sarahan.popular_movies_stage2.Loaders.LoadVote;
import com.sarahan.popular_movies_stage2.MovieRoom.FavoriteMovies;
import com.sarahan.popular_movies_stage2.MovieRoom.MovieViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    private RecyclerView mRecyclerView;
    private MovieAdapter movieAdapter; //adapter 설정 다시하기.
    private ArrayList<MovieItem> mMovieList = new ArrayList<>();
    private List<FavoriteMovies> mFavoriteMovies = new ArrayList<>();

    private final String MOVIE_LIST_STATE = "recyclerViewState";
    //JSON Key constants
    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String SYNOPSIS_OVERVIEW = "overview";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String RELEASE_DATE = "release_date";
    private static final String POPULARITY = "popularity";
    private static final String MOVIE_ID = "id";

    //constants for loader ID
    private static final int L_POPULAR = 1;
    private static final int L_VOTE_AVERAGE = 2;
    private static final int myFavorites = R.id.my_favorites;
    private static int currentSort;
    private static int loaderId; //keeps track of the loader ID that is currently displayed.

    private MovieViewModel mMovieViewModelMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MAIN ACTIVITY", "main activity starting");
        setContentView(R.layout.activity_main);

        mRecyclerView = findViewById(R.id.rv_movie);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));

        getFavoriteListFromDB();

        if (savedInstanceState != null &&
                (savedInstanceState.getInt(MOVIE_LIST_STATE)) != 0) {

            loaderId = savedInstanceState.getInt(MOVIE_LIST_STATE); //get the loader ID from saved state
            currentSort = savedInstanceState.getInt(CURRENT_SORT); //get the current sort from saved state

            if(loaderId == L_POPULAR && currentSort == R.id.popular){
                Log.d("savedInstanceState", "Popular sort is loaded again");
                getSupportLoaderManager().initLoader(L_POPULAR, null, this);
            }else if(loaderId == L_VOTE_AVERAGE && currentSort == R.id.top_rated) {
                Log.d("savedInstanceState", "Vote average sort is loaded again");
                getSupportLoaderManager().initLoader(L_VOTE_AVERAGE, null, this);
            }else if(currentSort == myFavorites){
                mFavoriteMovies = mMovieViewModelMain.getmMyFavouriteMovies().getValue();
                parseFavoriteListFromDB();
            }
        }else{
           getSupportLoaderManager().initLoader(L_POPULAR, null, this);
           // getPref();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //아예 앱을 종료할때 불려짐.
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("onResume", "on resume running ");

    }

    public void getFavoriteListFromDB(){
        mMovieViewModelMain = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModelMain.getmMyFavouriteMovies().observe(MainActivity.this,
                new Observer<List<FavoriteMovies>>() {
                    @Override
                    public void onChanged(@Nullable List<FavoriteMovies> favoriteMovies) {
                            mFavoriteMovies = favoriteMovies;
                        Log.d("getFavoriteList from DB", "onChanged method " + mFavoriteMovies);
                    }
        });
    }

    public void parseFavoriteListFromDB(){
        for(int i = 0 ; i < mFavoriteMovies.size(); i++){
            FavoriteMovies favoriteMovies = mFavoriteMovies.get(i);
            String imageUrl = favoriteMovies.geteMoviePosterPath();
            String movieName = favoriteMovies.geteMovieTitle();
            String synopsis = favoriteMovies.geteSynopsis();
            String releaseDate = favoriteMovies.geteReleaseDate();
            String voteAverage = favoriteMovies.geteVoteAverage();
            String popularity = favoriteMovies.getePopularity();
            int movieId = favoriteMovies.geteMovieId();
            mMovieList.add(new MovieItem(imageUrl, movieName, synopsis, releaseDate,
                    voteAverage, popularity, movieId));
        }
        movieAdapter = new MovieAdapter(this, mMovieList);
        mRecyclerView.setAdapter(movieAdapter);
    }

    private static final String CURRENT_SORT = "currentSortState";
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(MOVIE_LIST_STATE, loaderId);
        outState.putInt(CURRENT_SORT, currentSort);
        Log.d("onSaveInstanceState", "ON SAVE INSTANCE STATE");

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        loaderId = savedInstanceState.getInt(MOVIE_LIST_STATE);
        currentSort = savedInstanceState.getInt(CURRENT_SORT);
    }

    @Override
    //automatically invoked when activity is created./ load in background 를 실행시킨다.
    public boolean onCreateOptionsMenu(Menu menu){
        Log.d("onCreateOptionsMenu", "Creating options menu");
        getMenuInflater().inflate(R.menu.movie_menu, menu);
        return true; //return true -> shows menu.
    }


    public boolean onOptionsItemSelected(MenuItem item){
        Log.d("onOptionsItemSelected", "Selecting items");
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.top_rated:
                setTitle("TOP RATED MOVIES");

                getSupportLoaderManager().initLoader(L_VOTE_AVERAGE, null, this);

                currentSort = R.id.top_rated;
                //storePref(R.id.top_rated);
                movieAdapter.notifyDataSetChanged();
                return true;
            case R.id.popular:
                setTitle("POPULAR MOVIES");
                getSupportLoaderManager().initLoader(L_POPULAR, null, this);
                currentSort = R.id.popular;
                //storePref(R.id.popular);
                movieAdapter.notifyDataSetChanged();
                return true;
            case R.id.my_favorites:
                setTitle("MY FAVORITE MOVIES");
                mMovieList.clear();
                getFavoriteListFromDB();
                parseFavoriteListFromDB();
                currentSort = myFavorites;
                //storePref(myFavorites);
                Log.d("onOptionsItemSelected", "current sort : my favorite" + myFavorites);
                return true;
        }

        return true;
    }

//    public void storePref(int currentSort){
//        SharedPreferences sp = getSharedPreferences("current_sort_pref", MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putInt("current_sort", currentSort);
//        editor.commit();
//    }
//
//    public void getPref(){
//        SharedPreferences sp = getSharedPreferences("current_sort_pref", MODE_PRIVATE);
//        int prefSortOrder = sp.getInt("current_sort", R.id.popular);
//
//        if(prefSortOrder == R.id.popular){
//            getSupportLoaderManager().initLoader(L_POPULAR, null, this);
//        }else if(prefSortOrder == R.id.top_rated){
//            getSupportLoaderManager().initLoader(L_VOTE_AVERAGE, null, this);
//        }else{
//            mFavoriteMovies = mMovieViewModelMain.getmMyFavouriteMovies().getValue();
//            parseFavoriteListFromDB();
//        }
//    }




    @NonNull
    @Override
    public Loader<String> onCreateLoader(int loaderID, @Nullable Bundle bundle) {
        Log.d("onCreateLoader", "Creating Loaders");

        switch (loaderID){
            case L_POPULAR:
                return new LoadPopular(MainActivity.this);
            case L_VOTE_AVERAGE:
                return new LoadVote(MainActivity.this);
        }

        return null;
    }



    //constants for images
    final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    final String IMAGE_SIZE ="w185";

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String jsonString) {
        Log.d("onLoadFinished", "finishing loaders");
        loaderId = loader.getId();

        mMovieList.clear(); //make sure the mMovieList is empty, or else the recyclerView creates same items.

        if(loaderId == L_POPULAR || loaderId == L_VOTE_AVERAGE) {
            parseJsonStrings(jsonString);
        }
        movieAdapter = new MovieAdapter(MainActivity.this, mMovieList);
        mRecyclerView.setAdapter(movieAdapter);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }

    public void parseJsonStrings(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray resultsArray = jsonObject.getJSONArray(RESULTS);
            for (int i = 0; i < resultsArray.length(); i++) {
                JSONObject resultIndex = resultsArray.getJSONObject(i);

                String imageUrl = IMAGE_BASE_URL + IMAGE_SIZE + resultIndex.getString(POSTER_PATH);
                String movieName = resultIndex.getString(TITLE);
                String synopsis = resultIndex.getString(SYNOPSIS_OVERVIEW);
                String releaseDate = resultIndex.getString(RELEASE_DATE);
                String voteAverage = resultIndex.getString(VOTE_AVERAGE);
                String popularity = resultIndex.getString(POPULARITY);
                int movieId = resultIndex.getInt(MOVIE_ID);

                mMovieList.add(new MovieItem(imageUrl, movieName, synopsis, releaseDate,
                        voteAverage, popularity, movieId));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
