package com.sarahan.popular_movies_stage2.MovieRoom;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(FavoriteMovies favoriteMovies);

    @Query("DELETE FROM myFavoritesTable WHERE movieId = :movieId")
    void deleteById(int movieId);

    @Query("SELECT * FROM myFavoritesTable WHERE movieTitle = :movieTitle")
    FavoriteMovies loadMovieByTitle(String movieTitle);


    @Query("SELECT * FROM myFavoritesTable ORDER BY movieTitle")
    LiveData<List<FavoriteMovies>> getMyFavourite();
}