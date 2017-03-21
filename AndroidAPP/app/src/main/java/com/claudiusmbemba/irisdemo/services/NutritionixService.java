package com.claudiusmbemba.irisdemo.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.claudiusmbemba.irisdemo.helpers.HttpHelper;
import com.claudiusmbemba.irisdemo.helpers.RequestPackage;
import com.claudiusmbemba.irisdemo.models.NutritionixData;
import com.google.gson.Gson;

public class NutritionixService extends IntentService {

    public static final String NUTRITION_SERVICE_NAME = "nutritionixService";
    public static final String NUTRITION_SERVICE_ERROR = "nutritionixServiceError";
    public static final String NUTRITION_SERVICE_PAYLOAD = "nutritionixPaylod";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public NutritionixService() {
        super("NutritionixService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage =
                intent.getParcelableExtra(REQUEST_PACKAGE);

        Intent messageIntent = new Intent(NUTRITION_SERVICE_NAME);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());

        String response;
        try {
            response = HttpHelper.makeRequest(requestPackage, null);
            Gson gson = new Gson();
            NutritionixData nutritionixData = gson.fromJson(response, NutritionixData.class);
            messageIntent.putExtra(NUTRITION_SERVICE_PAYLOAD, nutritionixData);
        } catch (Exception e) {
            e.printStackTrace();
            messageIntent.putExtra(NUTRITION_SERVICE_ERROR, e.getMessage());
        }
        manager.sendBroadcast(messageIntent);
    }

}
