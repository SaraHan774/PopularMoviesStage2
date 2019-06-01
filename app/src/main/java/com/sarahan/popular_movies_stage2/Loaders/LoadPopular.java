package com.sarahan.popular_movies_stage2.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class LoadPopular extends AsyncTaskLoader<String> {
    private static final String LOG_TAG = LoadPopular.class.getSimpleName();
    private String mData;

    public LoadPopular(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mData != null){
            Log.d(LOG_TAG, "Popular sort , using cached data from mData");
            //use cached data
            deliverResult(mData);
        }else{ //no data, so loading it.
            Log.d(LOG_TAG, "no cached data, forcing load");
            forceLoad();
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        Log.d(LOG_TAG, "method loadInBackground running");
        String data = NetworkUtils.getSortedByPopularityJSON();
        return data;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        super.deliverResult(data);
        //save data for later retrieval
        Log.d(LOG_TAG, "method deliverResult running");
        mData = data;
    }
}
