package com.example.jorge.mybaking.adapters;

import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jorge.mybaking.R;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by jorge on 05/12/2017.
 * Adapter for support recyclerView
 */

public class AdapterBaking extends RecyclerView.Adapter<AdapterBaking.AdapterBankingViewHolder> {

    /*
     * An on-click handler that we've defined to make it easy for an Activity to interface with
     * our RecyclerView
     */
    private static AdapterBankingOnClickHandler mClickHandler;
    private List<Baking> data;
    private String mNameRecipes;
    private Context mContext;

    public AdapterBaking(AdapterBankingOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    public AdapterBaking(List<Baking> data, Context context) {
        this.data = data;
    }

    /**
     * This gets called when each new ViewHolder is created. This happens when the RecyclerView
     * is laid out. Enough ViewHolders will be created to fill the screen and allow for scrolling.
     */
    @Override
    public AdapterBaking.AdapterBankingViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View v;
        v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_baking, viewGroup, false);
        mContext = viewGroup.getContext();
        return new AdapterBankingViewHolder(v);
    }

    /**
     * OnBindViewHolder is called by the RecyclerView to display the data at the specified
     * position. In this method, we update the contents of the ViewHolder to display the weather
     * details for this particular position, using the "position" argument that is conveniently
     * passed into us.
     */

    @Override
    public void onBindViewHolder(AdapterBaking.AdapterBankingViewHolder holder, int position) {
        Baking baking = data.get(position);

        mNameRecipes = baking.getName();

        holder.mIdTextView.setText(baking.getId());
        holder.mNameTextView.setText(baking.getName());
        holder.mServingTextView.setText(baking.getServings());
        if (!baking.getImage().equals("")) {
            Picasso.with(mContext)
                    .load(baking.getImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(holder.mBakingImageView);
        }

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

    public String getNameRecipits() {
        return mNameRecipes;
    }

    public List<Baking> getData() {
        return data;
    }

    /**
     * The interface that receives onClick messages.
     */
    public interface AdapterBankingOnClickHandler {
        void onClick(List<Steps> steps);
    }

    /**
     * Cache of the children views for a forecast list item.
     */
    public class AdapterBankingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_id)
        TextView mIdTextView;

        @BindView(R.id.tv_name)
        TextView mNameTextView;

        @BindView(R.id.tv_serving)
        TextView mServingTextView;

        @BindView(R.id.iv_baking)
        ImageView mBakingImageView;

        @BindView(R.id.cv_card_view)
        CardView mCarView;

        @BindView(R.id.cl_constraint_layout)
        ConstraintLayout mConstraintLayout;


        public AdapterBankingViewHolder(View view) {
            super(view);
            ButterKnife.setDebug(true);
            ButterKnife.bind(this, view);
            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            Baking baking = data.get(adapterPosition);
            mClickHandler.onClick(baking.getSteps());
        }
    }

}
