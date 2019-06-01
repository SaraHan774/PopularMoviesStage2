package com.sarahan.popular_movies_stage2.MovieRoom;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.util.Log;

@Entity(tableName = "myFavoritesTable")
public class FavoriteMovies {
    private static final String LOG_TAG = FavoriteMovies.class.getSimpleName();
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "movieId")
    public int eMovieId; //e for entity

    @ColumnInfo(name = "movieTitle")
    public String eMovieTitle;

    @ColumnInfo(name = "posterPath")
    public String eMoviePosterPath;

    @ColumnInfo(name = "releaseDate")
    public String eReleaseDate;

    @ColumnInfo(name = "voteAverage")
    public String eVoteAverage;

    @ColumnInfo(name = "popularity")
    public String ePopularity;

    @ColumnInfo(name = "synopsis")
    public String eSynopsis;


    public FavoriteMovies(int eMovieId, String eMovieTitle, String eMoviePosterPath, String eReleaseDate,
                          String eVoteAverage, String ePopularity, String eSynopsis) {
        Log.d(LOG_TAG, "FavoriteMovies entity constructor running");
        this.eMovieId = eMovieId;
        this.eMovieTitle = eMovieTitle;
        this.eMoviePosterPath = eMoviePosterPath;
        this.eReleaseDate = eReleaseDate;
        this.eVoteAverage = eVoteAverage;
        this.ePopularity = ePopularity;
        this.eSynopsis = eSynopsis;
    }

    public int geteMovieId() {
        return eMovieId;
    }

    public String geteMovieTitle() {
        return eMovieTitle;
    }

    public String geteMoviePosterPath() {
        return eMoviePosterPath;
    }

    public String geteReleaseDate() {
        return eReleaseDate;
    }

    public String geteVoteAverage() {
        return eVoteAverage;
    }

    public String getePopularity() {
        return ePopularity;
    }

    public String geteSynopsis() {
        return eSynopsis;
    }

}
