package com.sarahan.popular_movies_stage2;


import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

//ADAPTER
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private static final String LOG_TAG = MovieAdapter.class.getSimpleName();
    private Context mContext;
    private ArrayList<MovieItem> mMovieItem;


    public MovieAdapter(Context context, ArrayList<MovieItem> movieItems){
        mContext = context;
        mMovieItem = movieItems;
    }



    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_cardview, viewGroup, false);
        return new MovieViewHolder(view); //view -> cardView
    }

    @Override
    public void onBindViewHolder(@NonNull final MovieViewHolder holder, int position) {
        Log.d(LOG_TAG, "on bind view holder running");
        final MovieItem currentItem = mMovieItem.get(position);

        final String imageUrl = currentItem.getmImageUrl();

        Log.d("Picasso", "image url : " + imageUrl);

        Picasso.get().load(imageUrl)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fit().centerInside().into(holder.mImageView); //MainActivity -> moviePoster

        holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) { //onClick -> DetailActivity -> populate movie info.

                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE_ITEM, currentItem);
                mContext.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return mMovieItem.size();
    }

    void setmMovieItem(ArrayList<MovieItem> movieItems){

        mMovieItem = movieItems;
        notifyDataSetChanged();
    }


//VIEW HOLDER
    public class MovieViewHolder extends RecyclerView.ViewHolder{ //ViewHolder -> movie poster & movie title
        public ImageView mImageView;

        public MovieViewHolder(View itemView) { //set image and text to a view
            super(itemView);

            mImageView = itemView.findViewById(R.id.image_view);

        }
    }
}
