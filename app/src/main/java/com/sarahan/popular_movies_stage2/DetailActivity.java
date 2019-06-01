package com.sarahan.popular_movies_stage2;


import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.NavUtils;
import android.support.v4.content.Loader;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.sarahan.popular_movies_stage2.Loaders.LoadReviews;
import com.sarahan.popular_movies_stage2.Loaders.LoadVideos;
import com.sarahan.popular_movies_stage2.MovieRoom.FavoriteMovies;
import com.sarahan.popular_movies_stage2.MovieRoom.MovieViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<String>
{

    private TextView movieTitle;
    private TextView mSynopsis;
    private TextView mReleaseDate;
    private TextView mVoteAverage;
    private RatingBar ratingBar;
    private TextView mPopular;
    private ImageView mImageView;
    private ImageButton mLikeButton;

    private RecyclerView rv_Reviews;
    private RecyclerView rv_Videos;
    //read from the get Intent , get parcelable extra
    private MovieItem movieItem;

    private String author;
    private String content;

    //get parcelable extra
    public static final String MOVIE_ITEM = "MovieItem";
    //loader ID
    private static final int L_VIDEOS = 3;
    private static final int L_REVIEWS = 4;
    //JSON keys
    private static final String RESULTS = "results";
    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String VIDEO_KEY = "key";
    //viewModel
    private MovieViewModel movieViewModel;

    private ArrayList<ReviewItem> reviewItems= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        //Log.d("DetailActivity", "detail activity - on create () ");

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        movieItem = getIntent().getParcelableExtra(MOVIE_ITEM);
        if(movieItem != null){
            populateViews();
        }else{
            return;
        }


        getSupportLoaderManager().initLoader(L_VIDEOS, null, this);
        getSupportLoaderManager().initLoader(L_REVIEWS, null, this);

        final boolean[] isContained = new boolean[1];
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        movieViewModel.getmMyFavouriteMovies().observe(this, new Observer<List<FavoriteMovies>>() {
            @Override
            public void onChanged(@Nullable List<FavoriteMovies> favoriteMovies) {
                for(int i = 0; i < favoriteMovies.size(); i++){
                    String movieTitle = favoriteMovies.get(i).geteMovieTitle();
                    isContained[0] = movieTitle.equals(movieItem.getmMovieName());
                    if(isContained[0] == true){
                        setIsLiked(isContained[0]);
                        return;
                    }
                    }
                }
        });

        likeButtonClick();
         //sets the onClick function to the like button.
        setReviewRecyclerView();
        setVideoRecyclerView();
    }


    public void setReviewRecyclerView(){
        rv_Reviews = findViewById(R.id.rv_detail_review);
        rv_Reviews.setHasFixedSize(true);
        rv_Reviews.setLayoutManager(new LinearLayoutManager(this));
        rv_Reviews.setNestedScrollingEnabled(false);

    }

    public void setVideoRecyclerView(){
        rv_Videos = findViewById(R.id.rv_detail_video);
        rv_Videos.setHasFixedSize(true);
        rv_Videos.setLayoutManager(new LinearLayoutManager(this));
        rv_Videos.setNestedScrollingEnabled(false);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home: //specify manifest -> parentActivityName
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void populateViews(){
        movieTitle = findViewById(R.id.tv_title);
        movieTitle.append(movieItem.getmMovieName());

        mSynopsis = findViewById(R.id.tv_plot_synopsis);
        mSynopsis.append(movieItem.getmSynopsis());

        mReleaseDate = findViewById(R.id.tv_release_date);
        mReleaseDate.append(movieItem.getmReleaseDate());

        mVoteAverage = findViewById(R.id.tv_vote_average);
        mVoteAverage.append(movieItem.getmVoteAverage());
        ratingBar = findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(movieItem.getmVoteAverage()));

        mPopular = findViewById(R.id.tv_popularity);
        mPopular.append(movieItem.getmPopular());

        mImageView = findViewById(R.id.detail_image_view);
        Picasso.get().load(movieItem.getmImageUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .fit().centerInside().into(mImageView);

        mLikeButton = findViewById(R.id.imageButton);

    }


    @NonNull
    @Override
    public Loader<String> onCreateLoader(int loaderID, @Nullable Bundle bundle) {
        int movieID = movieItem.getmMovieID(); //adapter 로부터 current item 의 ID가 전달된다.

        switch (loaderID){
            case L_VIDEOS:
                return new LoadVideos(DetailActivity.this, movieID);
            case L_REVIEWS:
                return new LoadReviews(DetailActivity.this, movieID);
        }
        return null;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String s) {
        //on create loader 가 on load background 를 실행시키고, 끝나면 실행되는 메소드
        int loaderID = loader.getId();
        if(loaderID == L_VIDEOS){
            jsonToVideoString(s);
            if(isKeyValid == false){ //Recycler view 에다가 video key 에 해당하는 비디오 띄우기
                Toast.makeText(this,
                        R.string.trailer_not_available,
                        Toast.LENGTH_LONG).show();
            }
        }else if(loaderID == L_REVIEWS){
            jsonToReviewString(s);
            if(isReviewValid == false) {
                Toast.makeText(this,
                        R.string.default_review,
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {
    //leaving this empty
    }

    //onLoadFinished() -> JSON -> String

    private boolean isKeyValid;
    private static final String VIDEO_NAME = "name";
    private ArrayList<VideoItem> videoItems = new ArrayList<>();

      public void jsonToVideoString(String s){
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray resultArray = jsonObject.getJSONArray(RESULTS);
            for(int i = 0; i < resultArray.length(); i++){
                JSONObject result = resultArray.getJSONObject(i);
                if(result != null) {
                    videoItems.add(
                            new VideoItem(result.getString(VIDEO_NAME)
                                    , result.getString(VIDEO_KEY))
                    );
                    isKeyValid = true;
                }else{
                    isKeyValid = false;
                }
            }
            DetailVideoAdapter detailVideoAdapter =
                    new DetailVideoAdapter(DetailActivity.this, videoItems);
            rv_Videos.setAdapter(detailVideoAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    //onLoadFinished() -> JSON -> String -> maps author and contents of the reviews.
    private boolean isReviewValid;
    public void jsonToReviewString(String s){
        try{
        JSONObject jsonObject = new JSONObject(s);
        JSONArray resultArray = jsonObject.getJSONArray(RESULTS);
        for(int i = 0; i < resultArray.length(); i++){
            JSONObject reviewResult = resultArray.getJSONObject(i);
            if(reviewResult !=null){
                author = reviewResult.getString(AUTHOR);
                content = reviewResult.getString(CONTENT);
                reviewItems.add(new ReviewItem(author, content));
                isReviewValid = true;
            }else{
                isReviewValid = false;
            }

        }
        DetailReviewAdapter adapter = new DetailReviewAdapter(DetailActivity.this, reviewItems);
        rv_Reviews.setAdapter(adapter);
    }catch (JSONException e){
        e.printStackTrace();
        }
    }


    private static boolean isLiked;
    public void likeButtonClick(){
        mLikeButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                final FavoriteMovies favoriteMovies = new FavoriteMovies(
                    movieItem.getmMovieID(),
                    movieItem.getmMovieName(),
                    movieItem.getmImageUrl(),
                    movieItem.getmReleaseDate(),
                    movieItem.getmVoteAverage(),
                    movieItem.getmPopular(),
                    movieItem.getmSynopsis()
                ); //store the clicked movie info into favorite movies database.
                //use ViewModel class to access database and make queries.
                MovieViewModel movieViewModel = new MovieViewModel(getApplication());
                if (!isLiked){ //if it is not yet liked, then insert.
                    movieViewModel.insert(favoriteMovies);
                    setIsLiked(true);
                    Toast.makeText(DetailActivity.this,
                            "Added to my favorites collection!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    movieViewModel.deleteById(favoriteMovies);
                    setIsLiked(false);
                    Toast.makeText(DetailActivity.this,
                            "Deleted from my favorites collection!",
                            Toast.LENGTH_SHORT).show();
              }
            }
        });
    }

    public void setIsLiked(Boolean fav){
        if(fav){
            isLiked = true;
            mLikeButton.setImageResource(R.drawable.ic_star_black_24dp);
        }else{
            isLiked = false;
            mLikeButton.setImageResource(R.drawable.ic_star_border_black_24dp);
        }
    }

}
