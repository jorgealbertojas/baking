package com.example.jorge.mybaking;

import android.content.ClipData;
import android.content.ClipDescription;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

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
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_BANDWIDTH_FRACTION;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MAX_INITIAL_BITRATE;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS;
import static com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection.DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS;



/**
 * Created by jorge on 08/12/2017.
 */

public class Part2Fragment extends Fragment implements ExoPlayer.EventListener{


    private ProgressBar progressBar;

    String mFile;


    private SimpleExoPlayerView simpleExoPlayerView;
    private SimpleExoPlayer player;

    private LinearLayout mLinearLayoutPlayPause;


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
        if(savedInstanceState != null) {
           // mImageIds = savedInstanceState.getIntegerArrayList(IMAGE_ID_LIST);
          // mListIndex = savedInstanceState.getInt(LIST_INDEX);
        }

        // Inflate the Android-Me fragment layout
        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        /** Returns Put extra*/
        Bundle extras = getActivity().getIntent().getExtras();



        /** View Detail with event*/

        mLinearLayoutPlayPause = (LinearLayout) rootView.findViewById(R.id.ll_play_pause);
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





        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

        /*** 2. Put the best QUALITY*/
        TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveVideoTrackSelection.Factory(bandwidthMeter,DEFAULT_MAX_INITIAL_BITRATE, DEFAULT_MIN_DURATION_FOR_QUALITY_INCREASE_MS, DEFAULT_MAX_DURATION_FOR_QUALITY_DECREASE_MS, DEFAULT_MIN_DURATION_TO_RETAIN_AFTER_DISCARD_MS,DEFAULT_BANDWIDTH_FRACTION);
        TrackSelector trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

        // 3. Create a default LoadControl
        LoadControl loadControl = new DefaultLoadControl();

        // 4. Create the player
        player = ExoPlayerFactory.newSimpleInstance(rootView.getContext(), trackSelector, loadControl);

        simpleExoPlayerView = (SimpleExoPlayerView) rootView.findViewById(R.id.player_view);
        simpleExoPlayerView.setPlayer(player);

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
        progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        return rootView;
    }



    /**
     * Save the current state of this fragment
     */
    @Override
    public void onSaveInstanceState(Bundle currentState) {
      //  currentState.putIntegerArrayList(IMAGE_ID_LIST, (ArrayList<Integer>) mImageIds);
      //  currentState.putInt(LIST_INDEX, mListIndex);
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
}
