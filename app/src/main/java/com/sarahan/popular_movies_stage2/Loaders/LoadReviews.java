package com.sarahan.popular_movies_stage2.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class LoadReviews extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = LoadReviews.class.getSimpleName();

    private String movieId;
    private String mData;

    public LoadReviews(@NonNull Context context, int movieId) {
        super(context);
        this.movieId = Integer.toString(movieId); //1. 포스터 클릭 -> 아이디 전달
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mData != null){
            Log.d(LOG_TAG, "load reviews -> using cached data from mData");
            deliverResult(mData);
        }else{
            Log.d(LOG_TAG, "load reviews -> no cached data, force load. ");
            forceLoad();
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {//2. 로더 실행
        String data = NetworkUtils.getReviewsJSON(movieId);
        return data;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        super.deliverResult(data);
        mData = data;
    }
}
