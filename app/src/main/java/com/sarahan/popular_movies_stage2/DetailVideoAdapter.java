package com.sarahan.popular_movies_stage2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailVideoAdapter extends RecyclerView.Adapter<DetailVideoAdapter.VideoViewHolder> {

    private Context mContext;
    private ArrayList<VideoItem> videoItems;
    private static final String YOUTUBE_BASE_URL = "https://www.youtube.com/watch?v=";

    public DetailVideoAdapter(Context context, ArrayList<VideoItem> videoItems) {
        super();
        mContext = context;
        this.videoItems = videoItems;
    }

    @NonNull
    @Override
    public VideoViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_video_list, viewGroup, false);
        return new VideoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoViewHolder videoViewHolder, int position) {
        final VideoItem currentVideoItem =  videoItems.get(position);
        videoViewHolder.tv_videoName.setText(currentVideoItem.getVideoName()); //get video title

        videoViewHolder.tv_videoName.setOnClickListener(new View.OnClickListener() { //click the title to watch video
            @Override
            public void onClick(View v) {
                mContext.startActivity(new Intent(Intent.ACTION_VIEW)
                .setData(Uri.parse(YOUTUBE_BASE_URL + currentVideoItem.getVideoLink()))); // get video link
            }
        });
    }

    @Override
    public int getItemCount() {
        return videoItems.size();
    }

    class VideoViewHolder extends RecyclerView.ViewHolder {
        TextView tv_videoName;
        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_videoName = itemView.findViewById(R.id.tv_video_name);

        }

    }
}
