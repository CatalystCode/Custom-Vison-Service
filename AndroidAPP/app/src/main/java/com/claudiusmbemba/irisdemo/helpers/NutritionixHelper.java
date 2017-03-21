package com.claudiusmbemba.irisdemo.helpers;

import android.util.Log;

import com.claudiusmbemba.irisdemo.models.NutritionixData;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * Created by jgadsby on 3/21/17.
 */

public class NutritionixHelper {

    public static void getNutritionInfo(String term, final NutritionixListener listener) {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get("https://api.nutritionix.com/v1_1/search/" + term + " ?fields=item_na" +
                "me%2Citem_id%2Cbrand_name%2Cnf_calories%2Cnf_total_fat&appId=f7292d95&appKey=" +
                "9927906f82d77be0f0239d5a4dd71179", new AsyncHttpResponseHandler() {

            @Override
            public void onStart() {
                // called before request is started
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {
                // called when response HTTP status is "200 OK"
                String responseString = new String(response);
                Gson gson = new Gson();
                NutritionixData nutritionixData = gson.fromJson(responseString, NutritionixData.class);

                Log.d("getNutritionInfo", "onSuccess: nutritionixData = " + nutritionixData.getHits().get(0).getFields().getBrandName());
                Log.d("getNutritionInfo", "onSuccess: nutritionixData = " + nutritionixData.getHits().get(0).getFields().getItemName());
                Log.d("getNutritionInfo", "onSuccess: nutritionixData = " + nutritionixData.getHits().get(0).getFields().getNfCalories());
                Log.d("getNutritionInfo", "onSuccess: nutritionixData = " + nutritionixData.getHits().get(0).getFields().getNfTotalFat());

                listener.onSuccess(nutritionixData);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)

                Log.d("getNutritionInfo", "onFailure: on Failure - " + statusCode);
                listener.onFailure("StatusCode = " + statusCode);
            }

            @Override
            public void onRetry(int retryNo) {
                // called when request is retried
            }
        });
    }
}
