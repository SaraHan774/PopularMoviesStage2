package com.sarahan.popular_movies_stage2.MovieRoom;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

public class MovieRepository {
    private static final String LOG_TAG = MovieRepository.class.getSimpleName();
    private MovieDao mMovieDao;
    private LiveData<List<FavoriteMovies>> mMyFavouriteMovies;
    private static FavoriteMovies favoriteMovies;


    public MovieRepository(Context context){
        MovieDatabase movieDatabase = MovieDatabase.getDatabase(context); //get database
        mMovieDao = movieDatabase.movieDao(); //from database -> Dao
        mMyFavouriteMovies = mMovieDao.getMyFavourite(); // from Dao -> entity
    }

    public LiveData<List<FavoriteMovies>> getmMyFavouriteMovies(){
        return mMyFavouriteMovies;
    }


    public void insertMovies(FavoriteMovies favoriteMovies){
        new InsertAsync(mMovieDao).execute(favoriteMovies);
    }

    public void deleteMovies(FavoriteMovies favoriteMovies){
        new DeleteAsync(mMovieDao).execute(favoriteMovies);
    }

//    public FavoriteMovies loadMovieByTitle(String movieTitle){
//        new LoadMovieByTitleAsync(mMovieDao).execute(movieTitle);
//        return favoriteMovies;
//    }



    public static class InsertAsync extends AsyncTask<FavoriteMovies, Void, Void>{
        private  MovieDao mAsyncTaskDao;

        InsertAsync(MovieDao dao){
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavoriteMovies... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public static class DeleteAsync extends AsyncTask<FavoriteMovies, Void, Void>{
        private  MovieDao mAsyncTaskDao;

        public DeleteAsync(MovieDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(FavoriteMovies... params) {
            mAsyncTaskDao.deleteById(params[0].geteMovieId()); //get the movie id
            return null;
        }
    }
//
//    private static class LoadMovieByTitleAsync extends AsyncTask<String, Void, FavoriteMovies>{
//        private  MovieDao mAsyncTaskDao;
//        public LoadMovieByTitleAsync(MovieDao dao) {
//            mAsyncTaskDao = dao;
//        }
//
//        @Override
//        protected List<FavoriteMovies> doInBackground(String... params) {
//            favoriteMovies = mAsyncTaskDao.loadMovieByTitle(params[0]);
//
//            return favoriteMovies;
//        }
//
//    }


}
