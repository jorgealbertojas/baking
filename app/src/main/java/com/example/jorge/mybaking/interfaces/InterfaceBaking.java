package com.example.jorge.mybaking.interfaces;

import com.example.jorge.mybaking.models.Baking;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Streaming;

import static com.example.jorge.mybaking.utilities.Utility.URL_COMPLEMENT;

/**
 * Created by jorge on 05/12/2017.
 * Interface for support Json
 */

public interface InterfaceBaking {

    /**
     * Get order Popular API Retrofit
     */
    @GET(URL_COMPLEMENT)
    @Streaming
    Call<List<Baking>> getBaking();

}
