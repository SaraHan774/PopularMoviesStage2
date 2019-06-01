package com.sarahan.popular_movies_stage2.Loaders;

import android.net.Uri;
import android.util.Log;

import com.sarahan.popular_movies_stage2.Config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

//helper class for connecting to the internet
public class NetworkUtils {
    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String MOVIE_BASE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String QUERY_POPULARITY = "popular";
    private static final String QUERY_VOTE_AVERAGE = "top_rated";
    private static final String QUERY_REVIEWS = "/reviews";
    private static final String QUERY_TRAILERS = "/videos";


    static String getSortedByPopularityJSON(){ //network connection이 null을 반환함.
        HttpsURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String sortedPopularJSON = null;

        try{
            Log.d(LOG_TAG, "getSortedByPopularityJSON()");
            String uriSortByPopularity = MOVIE_BASE_URL + QUERY_POPULARITY + Config.MOVIE_API_KEY;
            URL requestUrlPopularity = new URL(uriSortByPopularity);
            urlConnection = (HttpsURLConnection)requestUrlPopularity.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            int resCode = urlConnection.getResponseCode();
            System.out.println("res code: " + resCode);
            if(resCode != HttpsURLConnection.HTTP_OK)
                return null;

            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();
            //buffer 로 바꿔보기
            String line;
            while((line = reader.readLine()) != null){ //disconnection boilerplate 제거하기!!!
                builder.append(line);
                builder.append("\n");
                if(builder.length() == 0){
                    return null;
                }
            }
            sortedPopularJSON = builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(urlConnection != null){ //안하면 null pointer exception
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return sortedPopularJSON;
    }

    static String getSortedByVoteAverageJSON(){
        HttpsURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String sortedVoteJSON = null;

        try{
            Log.d(LOG_TAG, "getSortedByVoteAverageJSON");
            String uriSortByVoteAverage = MOVIE_BASE_URL + QUERY_VOTE_AVERAGE  + Config.MOVIE_API_KEY;
            URL requestUrlVoteAverage = new URL(Uri.decode(uriSortByVoteAverage));

            urlConnection = (HttpsURLConnection) requestUrlVoteAverage.openConnection();
            urlConnection.setRequestMethod("GET");
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
                if(builder.length() == 0){
                    return null;
                }
            }
            sortedVoteJSON = builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return sortedVoteJSON;
    }

    static String getReviewsJSON(String movieID){ //param : specific movie id
        HttpsURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String reviewsJSON = null;

        try{
            Log.d(LOG_TAG, "getReviewsJSON");
            StringBuffer buffer = new StringBuffer();
            String uriReview = buffer.append(MOVIE_BASE_URL)
                    .append(movieID)
                    .append(QUERY_REVIEWS)
                    .append(Config.MOVIE_API_KEY).toString();
            URL urlReview = new URL(Uri.decode(uriReview));

            urlConnection = (HttpsURLConnection) urlReview .openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
                if(builder.length() == 0){
                    return null;
                }
            }
            reviewsJSON = builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return reviewsJSON;
    }

    static String getVideosJSON(String movieID){
        HttpsURLConnection urlConnection  = null;
        BufferedReader reader = null;
        String videosJSON = null;

        try{
            Log.d(LOG_TAG, "getVideosJSON");
            StringBuffer buffer = new StringBuffer();
            String uriVideo = buffer.append(MOVIE_BASE_URL)
                    .append(movieID)
                    .append(QUERY_TRAILERS)
                    .append(Config.MOVIE_API_KEY).toString();
            URL urlVideo = new URL(Uri.decode(uriVideo));

            urlConnection = (HttpsURLConnection) urlVideo.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            StringBuilder builder = new StringBuilder();

            String line;
            while((line = reader.readLine()) != null){
                builder.append(line);
                builder.append("\n");
                if(builder.length() == 0){
                    return null;
                }
            }
            videosJSON = builder.toString();
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if(urlConnection != null){
                urlConnection.disconnect();
            }
            if(reader != null){
                try{
                    reader.close();
                }catch(IOException e){
                    e.printStackTrace();
                }
            }

        }
        return videosJSON;
    }


}
