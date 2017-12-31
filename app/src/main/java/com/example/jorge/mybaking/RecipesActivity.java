package com.example.jorge.mybaking;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.example.jorge.mybaking.adapters.AdapterBaking;
import com.example.jorge.mybaking.interfaces.InterfaceBaking;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.models.Steps;
import com.example.jorge.mybaking.utilities.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.jorge.mybaking.utilities.Utility.KEY_BUNDLE_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.KEY_INGREDIENTS;
import static com.example.jorge.mybaking.utilities.Utility.KEY_LIST_BAKING;
import static com.example.jorge.mybaking.utilities.Utility.URL_BASE;

public class RecipesActivity extends AppCompatActivity implements AdapterBaking.AdapterBankingOnClickHandler{

    private final String KEY_RECYCLER_STATE = "recycler_state";

    private final String KEY_ADAPTER_STATE = "adapter_state";

    AdapterBaking mAdapterBanking;

    private InterfaceBaking mInterfaceBanking;

    private RecyclerView mRecyclerView;

    private ProgressBar mLoadingIndicator;

    private static Bundle mBundleRecyclerViewState;

    private Context mContext;

    private ArrayList<Baking> mListBakingAdapter;

    private Parcelable mListState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mContext = this;

        // Get a reference to the ProgressBar using findViewById
        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);

        if (savedInstanceState == null) {

            initRecyclerView();
            /*
            * For salve state the activity when rotate
            * will end up displaying our weather data.
            */
            mBundleRecyclerViewState = new Bundle();
            Parcelable listState = mRecyclerView.getLayoutManager().onSaveInstanceState();
            mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, listState);

            /*
            * The ForecastAdapter is responsible for linking our weather data with the Views that
            * will end up displaying our weather data.
            */

            mAdapterBanking = new AdapterBaking(this);

            /* Setting the adapter attaches it to the RecyclerView in our layout. */
            mRecyclerView.setAdapter(mAdapterBanking);

            /*
            * The ProgressBar that will indicate to the user that we are loading data. It will be
            * hidden when no data is loading.
            *
            * Please note: This so called "ProgressBar" isn't a bar by default. It is more of a
            * circle. We didn't make the rules (or the names of Views), we just follow them.
            */
            mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);



            /* Once all of our views are setup, we can load the weather data. */
            if (Common.isOnline(this)) {
                createStackOverflowAPI();

                mInterfaceBanking.getBaking().enqueue(bakingCallback);

            } else {
                Context context = getApplicationContext();
                Toast toast = Toast.makeText(context, R.string.Error_Access, Toast.LENGTH_SHORT);
                toast.show();
            }
        } else {
            initRecyclerView();
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mListBakingAdapter = (ArrayList<Baking>) mBundleRecyclerViewState.getSerializable(KEY_ADAPTER_STATE);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mListState);

            mAdapterBanking = new AdapterBaking(mListBakingAdapter, mContext);

            mRecyclerView.setAdapter(mAdapterBanking);
        }

    }

    /**
     * Call Get InformationNew Movies .
     */
    private Callback<List<Baking>> bakingCallback = new Callback<List<Baking>>() {
        @Override
        public void onResponse(Call<List<Baking>> call, Response<List<Baking>> response) {
            try {
                if (response.isSuccessful()) {
                    List<Baking> data = new ArrayList<>();

                    data.addAll(response.body());


                    mAdapterBanking = new AdapterBaking( data, mContext);
                    mRecyclerView.setAdapter(mAdapterBanking);
                    mLoadingIndicator.setVisibility(View.INVISIBLE);

                } else {
                    Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            } catch (NullPointerException e) {
                System.out.println("onActivityResult consume crashed");
                runOnUiThread(new Runnable() {
                    public void run() {

                        Context context = getApplicationContext();
                        Toast toast = Toast.makeText(context, R.string.Error_Access_empty, Toast.LENGTH_SHORT);
                        toast.show();
                    }
                });
            }
        }

        @Override
        public void onFailure(Call<List<Baking>> call, Throwable t) {
            Context context = getApplicationContext();
            Toast toast = Toast.makeText(context, R.string.Error_Access_empty, Toast.LENGTH_SHORT);
            toast.show();
        }

    };


    /**
     * Find Data the API Json with Retrofit
     */
    private void createStackOverflowAPI() {
        Gson gson = new GsonBuilder()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL_BASE)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();


        mInterfaceBanking = retrofit.create(InterfaceBaking.class);
    }

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_numbers);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(RecipesActivity.this));


        mRecyclerView.setHasFixedSize(true);
        mAdapterBanking = new AdapterBaking(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // save RecyclerView state
        mBundleRecyclerViewState = new Bundle();
        mListState = mRecyclerView.getLayoutManager().onSaveInstanceState();
        mListBakingAdapter = (ArrayList<Baking>) mListBakingAdapter;
        mBundleRecyclerViewState.putParcelable(KEY_RECYCLER_STATE, mListState);
        mBundleRecyclerViewState.putSerializable(KEY_ADAPTER_STATE, mListBakingAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // restore RecyclerView state
        if (mBundleRecyclerViewState != null) {
            mListState = mBundleRecyclerViewState.getParcelable(KEY_RECYCLER_STATE);
            mListBakingAdapter = (ArrayList<Baking>) mBundleRecyclerViewState.getSerializable(KEY_ADAPTER_STATE);

        }
    }

    private int numberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // You can change this divider to adjust the size of the poster
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }


    @Override
    public void onClick(List<Steps> steps) {
        String ingredients = mAdapterBanking.getNameRecipites();


        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LIST_BAKING, (Serializable) steps);
        bundle.putString(KEY_INGREDIENTS,ingredients);

        // Attach the Bundle to an intent
        final Intent intent = new Intent(this, IngredientsActivity.class);
        intent.putExtra(KEY_BUNDLE_BAKING,bundle);
        startActivity(intent);
    }
}
