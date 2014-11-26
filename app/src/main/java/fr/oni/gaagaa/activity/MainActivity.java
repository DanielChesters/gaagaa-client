package fr.oni.gaagaa.activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.List;

import fr.oni.gaagaa.R;
import fr.oni.gaagaa.adapter.TweetsAdapter;
import fr.oni.gaagaa.fragment.NavigationDrawerFragment;
import fr.oni.gaagaa.model.twitter.Tweet;
import fr.oni.gaagaa.module.TwitterApiModule;
import fr.oni.gaagaa.util.PrefUtil;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment navigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence title;

    private Subscription twitterUserTimelineSubscription;
    private TwitterApiModule twitterApiModule;
    private TweetsAdapter tweetsAdapter;

    @Override
    protected void onDestroy() {
        twitterUserTimelineSubscription.unsubscribe();
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        title = getTitle();

        // Set up the drawer.
        navigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        FrameLayout layout = (FrameLayout) findViewById(R.id.container);
        RelativeLayout listTweetsLayout = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.fragment_list_tweets, layout, false);

        RecyclerView listTweets = (RecyclerView) listTweetsLayout.findViewById(R.id.list_tweets);
        listTweets.setLayoutManager(new LinearLayoutManager(this));
        listTweets.setItemAnimator(new DefaultItemAnimator());

        tweetsAdapter = new TweetsAdapter();
        listTweets.setAdapter(tweetsAdapter);

        layout.addView(listTweetsLayout);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String token = PrefUtil.getTwitterToken(this);
        String tokenSecret = PrefUtil.getTwitterTokenSecret(this);

        if (TextUtils.isEmpty(token) || TextUtils.isEmpty(tokenSecret)) {
            Intent intent = new Intent(this, TwitterAuthActivity.class);
            startActivity(intent);
        } else {
            twitterApiModule = new TwitterApiModule();
            twitterApiModule.init(token, tokenSecret);
            twitterUserTimelineSubscription = twitterApiModule.getUserTimeline("___Oni___", 20)
                    .flatMap(new Func1<List<Tweet>, Observable<Tweet>>() {
                        @Override
                        public Observable<Tweet> call(List<Tweet> tweets) {
                            return Observable.from(tweets);
                        }
                    })
                    .subscribe(new Action1<Tweet>() {
                        @Override
                        public void call(Tweet tweet) {
                            tweetsAdapter.getTweets().add(tweet);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            Log.e(TAG, throwable.getMessage(), throwable);
                            Toast.makeText(MainActivity.this,
                                    String.format("Error : %s", throwable.getMessage()),
                                    Toast.LENGTH_LONG).show();
                        }
                    }, new Action0() {
                        @Override
                        public void call() {
                            tweetsAdapter.notifyDataSetChanged();
                        }
                    });
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                title = getString(R.string.title_section1);
                break;
            case 2:
                title = getString(R.string.title_section2);
                break;
            case 3:
                title = getString(R.string.title_section3);
                break;
            default:
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(title);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!navigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }
}
