package com.example.jorge.mybaking.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jorge.mybaking.R;
import com.example.jorge.mybaking.models.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jorge on 06/12/2017.
 */

public class StepsListAdapter extends BaseAdapter {
    // Keeps track of the context and list of images to display
    private Context mContext;
    private ArrayList<Steps> mListSteps;


    /**
     * Constructor method
     * @param listSteps The list of images to display
     */
    public StepsListAdapter(Context context,ArrayList<Steps> listSteps) {
        mContext = context;
        mListSteps = listSteps;
    }

    /**
     * Returns the number of items the adapter will display
     */
    @Override
    public int getCount() {
        return mListSteps.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    /**
     * Creates a new ImageView for each item referenced by the adapter
     */
    public View getView(final int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        TextView textView;
        textView = new TextView(mContext);
        imageView = new ImageView(mContext);
        RelativeLayout relativeLayout = new RelativeLayout(mContext);
        relativeLayout.setId(Integer.parseInt("1"));

        if (convertView == null) {
            // If the view is not recycled, this creates a new ImageView to hold an image
            // Define the layout parameters
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
            imageView.setImageResource(R.mipmap.ic_launcher);
            textView.setText(mListSteps.get(position).getDescription());
            textView.setPadding(150, 8, 8, 8);

            if (!mListSteps.get(position).getThumbnailURL().equals("")) {
                Picasso.with(mContext)
                        .load(mListSteps.get(position).getThumbnailURL())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);
            }
        } else {
            if (convertView.getClass().getName().equalsIgnoreCase("android.widget.RelativeLayout")) {
                {
                    relativeLayout = (RelativeLayout) convertView;
                }
            }
        }
        relativeLayout.addView(imageView);
        relativeLayout.addView(textView);
        return relativeLayout;
    }

}
