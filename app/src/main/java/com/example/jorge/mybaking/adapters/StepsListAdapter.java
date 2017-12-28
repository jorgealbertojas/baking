package com.example.jorge.mybaking.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.jorge.mybaking.R;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jorge on 06/12/2017.
 */

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.StepsListAdapterViewHolder>  {

private List<Steps> data;

private Context mContext;

private final String KEY_ADAPTER_STATE = "adapter_state";



    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
private static StepsListAdapterOnClickHandler mClickHandler;

public StepsListAdapter(StepsListAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
        }

public StepsListAdapter(List<Steps> data, Context context) {
        this.data = data;
        }




/**
 * The interface that receives onClick messages.
 */
public interface StepsListAdapterOnClickHandler {
    void onClick(View view);
}




    public StepsListAdapter(List<Steps> data) {
        this.data = data;
    }

/**
 * Cache of the children views for a forecast list item.
 */
public class StepsListAdapterViewHolder extends RecyclerView.ViewHolder  implements View.OnClickListener {

    @BindView(R.id.tv_id_steps)
    TextView mIdTextView;

    @BindView(R.id.tv_description)
    TextView mDescriptionTextView;

    @BindView(R.id.tv_short_description)
    TextView mShortDescriptionTextView;

    @BindView(R.id.iv_thumbnail)
    ImageView mStepsImageView;

    @BindView(R.id.cv_card_view_steps)
    CardView mCarView;

    @BindView(R.id.cl_constraint_layout_steps)
    ConstraintLayout mConstraintLayout;



    public StepsListAdapterViewHolder(View view) {
        super(view);
        // ButterKnife.bind(this, itemView);


        ButterKnife.setDebug(true);
        ButterKnife.bind(this, view);


        view.setOnClickListener (this);
    }

    @Override
    public void onClick(View view) {
        int adapterPosition = getAdapterPosition();
        Steps steps = data.get(adapterPosition);
        mClickHandler.onClick(view);
    }
}

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     *
     * @param viewGroup The ViewGroup that these ViewHolders are contained within.
     * @param viewType  If your RecyclerView has more than one type of item (which ours doesn't) you
     *                  can use this viewType integer to provide a different layout. See
     *                  {@link android.support.v7.widget.RecyclerView.Adapter#getItemViewType(int)}
     *                  for more details.
     * @return A new ForecastAdapterViewHolder that holds the View for each list item
     */
    @Override
    public StepsListAdapter.StepsListAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_steps, viewGroup, false);
        mContext = viewGroup.getContext();
        return new StepsListAdapter.StepsListAdapterViewHolder(v);
    }


    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     *
     * @param StepsListAdapterViewHolder The ViewHolder which should be updated to represent the
     *                                 contents of the item at the given position in the data set.
     * @param position                 The position of the item within the adapter's data set.
     */

    @Override
    public void onBindViewHolder(StepsListAdapter.StepsListAdapterViewHolder holder, int position) {
        Steps steps = ((Steps) data.get(position));
        holder.mIdTextView.setText(Integer.toString(steps.getId()));
        holder.mDescriptionTextView.setText(steps.getDescription());
        holder.mShortDescriptionTextView.setText(steps.getShortDescription());
        holder.mCarView.setTag(position);
        holder.mConstraintLayout.setTag(position);
    }






    /**
     * This method simply returns the number of items to display. It is used behind the scenes
     * to help layout our Views and for animations.
     *
     * @return The number of items available in our forecast
     */
    @Override
    public int getItemCount() {
        if (null == data) return 0;
        return data.size();
    }

    public List<Steps> getData() {
        return data;
    }




}






    