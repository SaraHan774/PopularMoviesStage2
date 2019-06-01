package com.sarahan.popular_movies_stage2;


import android.os.Parcel;
import android.os.Parcelable;

public class MovieItem implements Parcelable {

    private String mImageUrl;
    private String mMovieName;
    private String mSynopsis;
    private String mReleaseDate;
    private String mVoteAverage;
    private String mPopular;
    private int mMovieID;


    public MovieItem(String mImageUrl, String mMovieName, String mSynopsis, String mReleaseDate
    , String mVoteAverage, String mPopular, int mMovieID){
        this.mImageUrl =  mImageUrl;
        this.mMovieName = mMovieName;
        this.mSynopsis = mSynopsis;
        this.mReleaseDate = mReleaseDate;
        this.mVoteAverage = mVoteAverage;
        this.mPopular = mPopular;
        this.mMovieID = mMovieID;
    }



    public String getmImageUrl(){
        return mImageUrl;
    }

    public String getmMovieName(){
        return mMovieName;
    }

    public String getmSynopsis() {
        return mSynopsis;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public String getmVoteAverage() {
        return mVoteAverage;
    }

    public String getmPopular() {
        return mPopular;
    }

    public int getmMovieID() {
        return mMovieID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mImageUrl);
        dest.writeString(mMovieName);
        dest.writeString(mSynopsis);
        dest.writeString(mReleaseDate);
        dest.writeString(mVoteAverage);
        dest.writeString(mPopular);
        dest.writeInt(mMovieID);
    }

    protected MovieItem(Parcel in) { //read data in the constructor
        this.mImageUrl = in.readString();
        this.mMovieName = in.readString();
        this.mSynopsis = in.readString();
        this.mReleaseDate = in.readString();
        this.mVoteAverage = in.readString();
        this.mPopular = in.readString();
        this.mMovieID = in.readInt();
    }
                                //field name must be public, CREATOR
        //generates instances of your Parcelable class from a Parcel.
    public static final Parcelable.Creator<MovieItem> CREATOR = new Parcelable.Creator<MovieItem>() {
        @Override
        public MovieItem createFromParcel(Parcel parcel) {
            return new MovieItem(parcel); //Parcelable 을 구현한 클래스의 인스턴스 생성
        } //un-marshalling the parcel.

        @Override
        public MovieItem[] newArray(int size) {
            return new MovieItem[size]; //Parcelable 을 구현한 클래스 객체의 어레이 생성
        }
    };
}
