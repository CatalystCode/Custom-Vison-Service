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
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class NutritionActivity extends AppCompatActivity {

    String food_item;
    BarChart chart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition);

        food_item = (String) getIntent().getStringExtra(MainActivity.FOOD_RESULT);

        chart = (BarChart) findViewById(R.id.chart);
        prepareChartData((Hit)getIntent().getParcelableExtra(MainActivity.NUTRITION_RESULT));
    }

    public void prepareChartData(Hit hit){
        ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        entries.add(new BarEntry(0, Float.parseFloat(df.format((hit.getFields().getNfCalories()/2000)*100))));
        entries.add(new BarEntry(1, Float.parseFloat(df.format((hit.getFields().getNfTotalFat()/65)*100))));
        entries.add(new BarEntry(2, Float.parseFloat(df.format((hit.getFields().getNfCholesterol()/300)*100))));
        entries.add(new BarEntry(3, Float.parseFloat(df.format((hit.getFields().getNfSodium()/2400)*100))));
        entries.add(new BarEntry(4, Float.parseFloat(df.format((hit.getFields().getNfTotalCarbohydrate()/300)*100))));
        entries.add(new BarEntry(5, (float) hit.getFields().getNfSodium()));
        entries.add(new BarEntry(6, Float.parseFloat(df.format((hit.getFields().getNfProtein()/50)*100))));
        entries.add(new BarEntry(7, (float) hit.getFields().getNfVitaminADv()));
        entries.add(new BarEntry(8, (float) hit.getFields().getNfVitaminCDv()));
        entries.add(new BarEntry(9, (float) hit.getFields().getNfCalciumDv()));

        BarDataSet dataSet = new BarDataSet(entries, "Hello World");

        setChartData(dataSet);
    }

    public void setChartData(BarDataSet dataSet){
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

//        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(true);

        chart.getDescription().setEnabled(false);

        chart.setMaxVisibleValueCount(10);

        chart.setPinchZoom(true);
        chart.setDrawGridBackground(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(Typeface.MONOSPACE);
        xAxis.setDrawGridLines(false);
        xAxis.setLabelCount(7);
//        xAxis.setTextSize(20f);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Calories", "Fat(g)", "Cholesterol(mg)", "Sodium(mg)",
                "Carbs(g)", "Sugars", "Protein(g)", "VitaminA(mg)", "VitaminC(mg)", "Calcium(mg)"}));

        IAxisValueFormatter custom = new PercentFormatter();

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(Typeface.MONOSPACE);
        leftAxis.setLabelCount(5, false);
        leftAxis.setDrawGridLines(true);
        leftAxis.setValueFormatter(custom);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        leftAxis.setSpaceTop(5f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        leftAxis.setAxisMaximum(100f);

        chart.getLegend().setEnabled(false);
        chart.getAxisRight().setDrawLabels(false);
        chart.getAxisRight().setDrawGridLines(false);

        BarData data = new BarData(dataSet);
        data.setValueTextSize(10f);
        data.setValueTypeface(Typeface.MONOSPACE);
        data.setBarWidth(0.9f);

        chart.setData(data);
        chart.invalidate();
    }
}
