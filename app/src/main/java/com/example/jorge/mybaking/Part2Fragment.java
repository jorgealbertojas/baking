package com.example.jorge.mybaking;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import static com.example.jorge.mybaking.utilities.Utility.KEY_SHARED_DURATION;
import static com.example.jorge.mybaking.utilities.Utility.KEY_SHARED_FILE;
import static com.example.jorge.mybaking.utilities.Utility.KEY_SHARED_POSITION;
import static com.example.jorge.mybaking.utilities.Utility.KEY_SHARED_PREFERENCES;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_BANDWIDTH_FRACTION;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_INITIAL_BITRATE;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS;



/**
 * Created by jorge on 08/12/2017.
 */

public class Part2Fragment extends Fragment implements ExoPlayer.EventListener{

    private String mFile;
    private long mResumePosition = 0;
    private long mResumeDuration = 0;
    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;
    private LinearLayout mLinearLayoutPlayPause;
    private TrackSelector mTrackSelector;
    private View mRootView;
    private TrackSelection.Factory mVideoTrackSelectionFactory;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the fragment
     */
    public Part2Fragment() {
    }

    /**
     * Inflates the fragment layout file and sets the correct resource for the image to display
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Load the saved state (the list of images and list index) if there is one

        mRootView = inflater.inflate(R.layout.fragment_video, container, false);

        if(savedInstanceState == null) {
            // Inflate the Android-Me fragment layout
            mRootView = inflater.inflate(R.layout.fragment_video, container, false);

            /** View Detail with event*/

            mLinearLayoutPlayPause = (LinearLayout) mRootView.findViewById(R.id.ll_play_pause);
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



            mRootView = initializePlayer(mRootView);
        }else {
            SharedPreferences settings = this.getActivity().getSharedPreferences(KEY_SHARED_PREFERENCES, 0);
            mResumePosition = settings.getLong(KEY_SHARED_POSITION,0);
            mResumeDuration = settings.getLong(KEY_SHARED_DURATION,0);
            mFile = settings.getString(KEY_SHARED_FILE,"0");
            if (mResumePosition < mResumeDuration) {
                player = null;
                mRootView = initializePlayer(mRootView);

            }

        }



        return mRootView;
    }





    public void setListIndex(String index) {
        mFile = index;
    }


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
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }


    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            initializePlayer(mRootView);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || player == null) {
            initializePlayer(mRootView);
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

    private View initializePlayer(View rootView) {

        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        /*** 2. Put the best QUALITY*/
        mVideoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter,DEFAULT_MAX_INITIAL_BITRATE, DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS, DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS, DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,DEFAULT_BANDWIDTH_FRACTION);
        mTrackSelector = new DefaultTrackSelector(mVideoTrackSelectionFactory);

        // 3. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 4. Create the player
        player = ExoPlayerFactory.newSimpleInstance(rootView.getContext(), mTrackSelector, loadControl);

        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);

        if (mResumePosition > 0) {
            player.seekTo(mResumePosition);
        }

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                Util.getUserAgent(getContext(), "Exo2"), defaultBandwidthMeter);
        // Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        // This is the MediaSource representing the media to be played.


        MediaSource mediaSource = null;
        mediaSource = createPlayerSound(dataSourceFactory, extractorsFactory);
        player.prepare(mediaSource);

        player.addListener(this);
        simpleExoPlayerView.requestFocus();
        player.setPlayWhenReady(true);
        return rootView;
    }

    private void releasePlayer() {
        if (player != null) {
            player.stop();
            updateResumePosition();
            player.release();
            player = null;
            mTrackSelector = null;
            mVideoTrackSelectionFactory= null;

        }
    }

    private void updateResumePosition() {
        mResumePosition = player.getCurrentPosition();
        mResumeDuration = player.getDuration();

        SharedPreferences settings = this.getActivity().getSharedPreferences(KEY_SHARED_PREFERENCES, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putLong(KEY_SHARED_POSITION, mResumePosition);
        editor.putLong(KEY_SHARED_DURATION, mResumeDuration);
        editor.putString(KEY_SHARED_FILE, mFile);

        // Commit the edits!
        editor.commit();


    }

}
