package com.claudiusmbemba.irisdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.claudiusmbemba.irisdemo.services.IrisService;
import com.claudiusmbemba.irisdemo.services.NutritionixService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras().containsKey(NutritionixService.NUTRITION_SERVICE_ERROR)) {

            } else if (intent.getExtras().containsKey(NutritionixService.NUTRITION_SERVICE_NAME)) {
                List<Entry> entries = new ArrayList<Entry>();

//                for (YourData data : dataObjects) {
//
//                    // turn your data into Entry objects
//                    entries.add(new Entry(data.getValueX(), data.getValueY()));
//                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        PieChart chart = (PieChart) findViewById(R.id.chart);

        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(receiver, new IntentFilter(NutritionixService.NUTRITION_SERVICE_NAME));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getApplicationContext())
                .unregisterReceiver(receiver);
    }
}
