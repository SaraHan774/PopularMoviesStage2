package com.sarahan.popular_movies_stage2.Loaders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

public class LoadVote extends AsyncTaskLoader<String>{
    private static final String LOG_TAG = LoadVote.class.getSimpleName();

    String mData;

    public LoadVote(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if(mData != null){
            Log.d(LOG_TAG, "load vote - top rated sort - using cached data ");
            deliverResult(mData);
        }else{
            Log.d(LOG_TAG, "load vote - top rated sort - force load");
            forceLoad();
        }
    }

    @Nullable
    @Override
    public String loadInBackground() {
        String data = NetworkUtils.getSortedByVoteAverageJSON();
        return data;
    }

    @Override
    public void deliverResult(@Nullable String data) {
        super.deliverResult(data);
        mData = data;
    }
}
