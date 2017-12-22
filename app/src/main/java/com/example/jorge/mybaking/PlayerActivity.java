package com.example.jorge.mybaking;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import static com.example.jorge.mybaking.utilities.Utility.KEY_EXTRA_FILE;
import static com.example.jorge.mybaking.utilities.Utility.KEY_SHARED_POSITION;
import static com.example.jorge.mybaking.utilities.Utility.KEY_SHARED_PREFERENCES;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_BANDWIDTH_FRACTION;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_INITIAL_BITRATE;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS;

import static com.example.jorge.mybaking.utilities.Utility.TAG;

public class PlayerActivity extends AppCompatActivity implements ExoPlayer.EventListener {

    private ProgressBar progressBar;

    String mFile;

    private TrackSelector trackSelector;

    private long mResumePosition = 0;


    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private LinearLayout mLinearLayoutPlayPause;
    private TrackSelection.Factory videoTrackSelectionFactory;

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
            if (player != null) {
                player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
            if (player != null) {
                player.setPlayWhenReady(false); //to pause a video because now our video player is not in focus
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseAdsLoader();

    }


    private void releaseAdsLoader() {
        if (player != null) {
            player.release();
            simpleExoPlayerView.getOverlayFrameLayout().removeAllViews();
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        /** Returns Put extra*/
        Bundle extras = getIntent().getExtras();
        mFile = extras.getString(KEY_EXTRA_FILE);


        /** View Detail with event*/


        mLinearLayoutPlayPause = (LinearLayout) findViewById(R.id.ll_play_pause);
        mLinearLayoutPlayPause.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                ClipData.Item item = new ClipData.Item((CharSequence)v.getTag());
                String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
                ClipData dragData = new ClipData(v.getTag().toString(),mimeTypes, item);
                View.DragShadowBuilder myShadow = new View.DragShadowBuilder(mLinearLayoutPlayPause);
                v.startDrag(dragData,myShadow,null,0);
                return true;
            }
        });

        if (savedInstanceState != null) {
            SharedPreferences settings = getSharedPreferences(KEY_SHARED_PREFERENCES, 0);
            mResumePosition = settings.getLong(KEY_SHARED_POSITION,0);

        }
        initializePlayer();



    }


    private void initializePlayer() {{
        // 1. Create a default TrackSelector
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        /*** 2. Put the best QUALITY*/
        videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter,DEFAULT_MAX_INITIAL_BITRATE, DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS, DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS, DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,DEFAULT_BANDWIDTH_FRACTION);
        trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 3. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 4. Create the player
        player = ExoPlayerFactory.newSimpleInstance(this, trackSelector, loadControl);

        simpleExoPlayerView = (SimpleExoPlayerView) findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);





        if (mResumePosition > 0) {
            player.seekTo(mResumePosition);
        }

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "Exo2"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.


        MediaSource mediaSource = null;
        mediaSource = createPlayerSound(dataSourceFactory, extractorsFactory);
        player.prepare(mediaSource);

        player.addListener(this);
        simpleExoPlayerView.requestFocus();
        player.setPlayWhenReady(true);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }}

    /*** function return HlsMediaSource of the Sound*/
    private MediaSource createPlayerSound(DataSource.Factory dataSourceFactory, ExtractorsFactory extractorsFactory)  {
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(mFile),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);
        return  mediaSource;
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }






    /*** Change Progreess an Control Player dependent of the Status */
    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        switch (playbackState) {
            case ExoPlayer.STATE_BUFFERING:
                //You can use progress dialog to show user that video is preparing or buffering so please wait
                Log.i(TAG, "STATE_BUFFERING: ");
                progressBar.setVisibility(View.VISIBLE);
                break;
            case ExoPlayer.STATE_IDLE:
                Log.i(TAG, "STATE_IDLE: ");
                //idle state
                break;
            case ExoPlayer.STATE_READY:
                // dismiss your dialog here because our video is ready to play now
                Log.i(TAG, "STATE_READY: ");
                progressBar.setVisibility(View.GONE);

                break;
            case ExoPlayer.STATE_ENDED:
                Log.i(TAG, "STATE_ENDED: ");
                // do your processing after ending of video
                break;
        }
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            updateResumePosition();
            player.release();
            player = null;
            trackSelector = null;
            videoTrackSelectionFactory= null;

        }
    }

    private void updateResumePosition() {
        mResumePosition = player.getCurrentPosition();

        SharedPreferences settings = getSharedPreferences(KEY_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_SHARED_POSITION, mResumePosition);

        // Commit the edits!
        editor.commit();


    }











}
