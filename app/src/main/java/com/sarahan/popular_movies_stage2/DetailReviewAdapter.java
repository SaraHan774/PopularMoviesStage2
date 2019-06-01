package com.sarahan.popular_movies_stage2;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;


public class DetailReviewAdapter extends RecyclerView.Adapter<DetailReviewAdapter.ReviewViewHolder> {

    private Context mContext;
    private ArrayList<ReviewItem> mReviewList;

    public DetailReviewAdapter(Context context, ArrayList<ReviewItem> reviewList) {
        mContext = context;
        mReviewList = reviewList;
    }

    @NonNull
    @Override
    public DetailReviewAdapter.ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.movie_review_list, viewGroup, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder reviewViewHolder, int position) {
        ReviewItem currentReviewItem = mReviewList.get(position);
            reviewViewHolder.tv_author.append(currentReviewItem.getAuthor());
            reviewViewHolder.expandableTextView.setText(currentReviewItem.getContent());

    }


    @Override
    public int getItemCount() {
        return mReviewList.size();
    }

    class ReviewViewHolder extends RecyclerView.ViewHolder{
        TextView tv_author;

        ExpandableTextView expandableTextView;
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_author = itemView.findViewById(R.id.tv_author);
            //The Review Contents are inside the expandable text view.
            expandableTextView = (ExpandableTextView) itemView.findViewById(R.id.expand_text_view);
        }



    }
}
