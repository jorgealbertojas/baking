package com.example.jorge.mybaking.widgets;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.example.jorge.mybaking.R;
import com.example.jorge.mybaking.interfaces.InterfaceBaking;
import com.example.jorge.mybaking.models.Baking;
import com.example.jorge.mybaking.utilities.Common;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.jorge.mybaking.utilities.Utility.URL_BASE;

/**
 * Created by jorge on 11/12/2017.
 */

public class MyWidgetProvider extends AppWidgetProvider {

    private InterfaceBaking mInterfaceBanking;
    private Context mContext;
    private AppWidgetManager mAppWidgetManager;
    private int[] mAppWidgetIds;
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

                    putDataWidget(mContext, mAppWidgetManager, mAppWidgetIds, data);


                } else {
                    Log.d("QuestionsCallback", "Code: " + response.code() + " Message: " + response.message());
                }
            } catch (NullPointerException e) {
                System.out.println("onActivityResult consume crashed");

            }
        }

        @Override
        public void onFailure(Call<List<Baking>> call, Throwable t) {

        }
    };

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {

             /* Once all of our views are setup, we can load the weather data. */
        mContext = context;
        mAppWidgetManager = appWidgetManager;
        mAppWidgetIds = appWidgetIds;

        if (Common.isOnline(context)) {
            createStackOverflowAPI();
            mInterfaceBanking.getBaking().enqueue(bakingCallback);

        } else {
            Toast toast = Toast.makeText(context, R.string.Error_Access, Toast.LENGTH_SHORT);
            toast.show();
        }

    }

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


    private void putDataWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds, List<Baking> listBaking) {
        // Get all ids
        ComponentName thisWidget = new ComponentName(context,
                MyWidgetProvider.class);
        int[] allWidgetIds = appWidgetManager.getAppWidgetIds(thisWidget);


        int x = 0;


        for (int widgetId : allWidgetIds) {
            // create some random data

            x = (new Random().nextInt(listBaking.size() - 1));

            String ingredients = "";
            String ingredientsName = listBaking.get(x).getName() ;

            for (int i = 0; i < listBaking.get(x).getIngredients().size(); i++) {
                ingredients = ingredients + "  (" + Integer.toString(i) + ") " + listBaking.get(x).getIngredients().get(i).getQuantity() + " - " + listBaking.get(x).getIngredients().get(i).getIngredient();
            }

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                    R.layout.widget_layout);

            // Set the text
            remoteViews.setTextViewText(R.id.update, (ingredients));

            remoteViews.setTextViewText(R.id.empty_view, (ingredientsName));


            // Register an onClickListener
            Intent intent = new Intent(context, MyWidgetProvider.class);

            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                    0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            remoteViews.setOnClickPendingIntent(R.id.update, pendingIntent);
            remoteViews.setOnClickPendingIntent(R.id.empty_view, pendingIntent);


            appWidgetManager.updateAppWidget(widgetId, remoteViews);
        }
    }


}