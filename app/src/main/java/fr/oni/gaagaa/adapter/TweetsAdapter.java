package fr.oni.gaagaa.adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import fr.oni.gaagaa.R;
import fr.oni.gaagaa.model.twitter.Tweet;

public class TweetsAdapter extends RecyclerView.Adapter<TweetsAdapter.ViewHolder> {

    private SortedSet<Tweet> tweets;
    private Context context;

    public TweetsAdapter(Context context) {
        this.context = context;
    }

    public SortedSet<Tweet> getTweets() {
        if (tweets == null) {
            clearData();
        }
        return tweets;
    }

    public void clearData() {
        tweets = new TreeSet<>(new Comparator<Tweet>() {
            @Override
            public int compare(Tweet tweet1, Tweet tweet2) {
                return tweet2.getDateCreated().compareTo(tweet1.getDateCreated());
            }
        });
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tweet_item, parent,
                false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Tweet tweet = new ArrayList<>(getTweets()).get(position);
        holder.getTextView().setText(tweet.getText());
        holder.getUserView().setText(String.format("%s (@%s)", tweet.getUser().getName(),
                tweet.getUser().getScreenName()));
        final ImageView profileView = holder.getProfileView();
        Picasso.with(context).load(tweet.getUser().getProfileImageUrlHttps())
                .resize(profileView.getLayoutParams().width, profileView.getLayoutParams().height)
                .centerCrop()
                .into(profileView);
        holder.getDateView().setText(tweet.getDateCreated());
    }

    @Override
    public int getItemCount() {
        return getTweets().size();
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private TextView userView;
        private TextView dateView;
        private ImageView profileView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.tweet_text);
            this.userView = (TextView) itemView.findViewById(R.id.tweet_user);
            this.dateView = (TextView) itemView.findViewById(R.id.tweet_date);
            this.profileView = (ImageView) itemView.findViewById(R.id.user_profile_image);
        }

        public TextView getUserView() {
            return userView;
        }

        public TextView getDateView() {
            return dateView;
        }

        public ImageView getProfileView() {
            return profileView;
        }

        public TextView getTextView() {
            return textView;
        }
    }
}
