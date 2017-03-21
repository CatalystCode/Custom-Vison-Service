package com.claudiusmbemba.irisdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.claudiusmbemba.irisdemo.models.Fields;
import com.claudiusmbemba.irisdemo.models.Hit;
import com.claudiusmbemba.irisdemo.models.NutritionixData;
import com.claudiusmbemba.irisdemo.services.NutritionixService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    String food_item;
    PieChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        food_item = (String) getIntent().getStringExtra(MainActivity.FOOD_RESULT);

        chart = (PieChart) findViewById(R.id.chart);

        prepareChartData((Hit)getIntent().getParcelableExtra(MainActivity.NUTRITION_RESULT));
    }

    public void prepareChartData(Hit hit){
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        entries.add(new PieEntry((float) hit.getFields().getNfCalories(), "Calories"));
        entries.add(new PieEntry((float) hit.getFields().getNfTotalFat(), "Total Fat"));

        PieDataSet dataSet = new PieDataSet(entries, "");

        setChartData(dataSet);
    }

    public void setChartData(PieDataSet dataSet){
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(20f);
        data.setValueTextColor(Color.BLACK);
        data.setValueTypeface(Typeface.MONOSPACE);

        chart.setUsePercentValues(true);
        chart.setCenterTextTypeface(Typeface.MONOSPACE);
        chart.getDescription().setEnabled(true);
        chart.setData(data);
        chart.highlightValues(null);
        chart.setEntryLabelTextSize(15f);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setCenterText(String.format("%s %s", food_item, "Results").toUpperCase());
        chart.setCenterTextColor(Color.BLACK);
        chart.setCenterTextSize(20f);
        chart.invalidate();

        //Legend
        Legend l = chart.getLegend();
        l.setFormSize(10f); // set the size of the legend forms/shapes
        l.setForm(Legend.LegendForm.CIRCLE); // set what type of form/shape should be used
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setTextSize(15f);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);
    }
}
