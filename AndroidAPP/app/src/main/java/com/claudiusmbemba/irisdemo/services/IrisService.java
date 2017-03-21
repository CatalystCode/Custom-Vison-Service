package com.claudiusmbemba.irisdemo.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.claudiusmbemba.irisdemo.helpers.HttpHelper;
import com.claudiusmbemba.irisdemo.helpers.RequestPackage;
import com.claudiusmbemba.irisdemo.model.IrisData;
import com.google.gson.Gson;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class IrisService extends IntentService {

    public static final String IRIS_SERVICE_NAME = "irisService";
    public static final String IRIS_SERVICE_PAYLOAD = "irisServicePayload";
    public static final String IRIS_SERVICE_ERROR = "irisServiceError";
    public static final String REQUEST_PACKAGE = "requestPackage";
    public static final String REQUEST_IMAGE = "requestImage";
    public static final String NUTRITION_REQUEST = "nutrionixPaylod";


    public IrisService() {
        super("IrisService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage =
                intent.getParcelableExtra(REQUEST_PACKAGE);

        Intent messageIntent = new Intent(IRIS_SERVICE_NAME);
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());

        InputStream imageData = null;
        if (intent.getExtras().containsKey(REQUEST_IMAGE)){
           imageData = new ByteArrayInputStream(intent.getExtras().getByteArray(REQUEST_IMAGE));
        }
        String response;
        try {
            response = HttpHelper.makeRequest(requestPackage, imageData);
            Gson gson = new Gson();
            IrisData irisData = gson.fromJson(response, IrisData.class);
            messageIntent.putExtra(IRIS_SERVICE_PAYLOAD, irisData);
        } catch (Exception e) {
            e.printStackTrace();
            messageIntent.putExtra(IRIS_SERVICE_ERROR, e.getMessage());
        }
        manager.sendBroadcast(messageIntent);
    }

}
