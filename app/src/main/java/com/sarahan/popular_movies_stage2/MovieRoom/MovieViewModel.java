package com.sarahan.popular_movies_stage2.MovieRoom;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {
    private static final String LOG_TAG = MovieViewModel.class.getSimpleName();

    private MovieRepository movieRepository;
    private LiveData<List<FavoriteMovies>> mMyFavouriteMovies;

    public MovieViewModel(Application application){
        super(application);
        Log.d(LOG_TAG, "view model constructor running");
        movieRepository = new MovieRepository(application); //from repository
        mMyFavouriteMovies = movieRepository.getmMyFavouriteMovies(); // get my favorite movie

    }

    //this completely hides the implementation from the UI.
    public LiveData<List<FavoriteMovies>> getmMyFavouriteMovies(){
        return movieRepository.getmMyFavouriteMovies();
    }

    public void insert(FavoriteMovies favoriteMovies){
        Log.d(LOG_TAG, "VIEW MODEL INSERT METHOD RUNNING");
        movieRepository.insertMovies(favoriteMovies);
    }

    public void deleteById(FavoriteMovies favoriteMovies){
        Log.d(LOG_TAG, "VIEW MODEL DELETE METHOD RUNNING");
        movieRepository.deleteMovies(favoriteMovies);
    }

//    public FavoriteMovies loadMovieByTitle(String movieTitle){
//        return movieRepository.loadMovieByTitle(movieTitle);
//    }


}
