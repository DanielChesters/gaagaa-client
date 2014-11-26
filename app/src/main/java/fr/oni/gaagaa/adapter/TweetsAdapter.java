package fr.oni.gaagaa.adapter;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fr.oni.gaagaa.R;
import fr.oni.gaagaa.model.twitter.Tweet;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private List<Tweet> tweets;

    public List<Tweet> getTweets() {
        if (tweets == null) {
            tweets = new ArrayList<>();
        }
        return tweets;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = getTweets().get(position);
        String text = String.format(String.format("%s\n%s (%s)", tweet.getText(),
                tweet.getUser().getName(), tweet.getDateCreated()));
        holder.getTextView().setText(text);
    }

    @Override
    public int getItemCount() {
        return getTweets().size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tweet_text);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
