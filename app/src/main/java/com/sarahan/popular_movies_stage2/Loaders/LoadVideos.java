package com.sarahan.popular_movies_stage2.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class LoadVideos extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = LoadVideos.class.getSimpleName();

    private String movieId;
    private String mData;

    public LoadVideos(@NonNull Context context, int movieId) {
        super(context);
        this.movieId = Integer.toString(movieId);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mData != null){
            Log.d(LOG_TAG, "load videos -> using cached data from mData");
            deliverResult(mData);
        }else{
            Log.d(LOG_TAG, "load videos -> no cached data, force load.");
            forceLoad();
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String data = NetworkUtils.getVideosJSON(movieId);
        return data;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        super.deliverResult(data);
        mData = data;
    }
}
