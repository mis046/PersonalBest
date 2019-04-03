package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.team13.personalbest_team13_skeleton.DataIOStream;
import com.example.team13.personalbest_team13_skeleton.MainActivity;
import com.example.team13.personalbest_team13_skeleton.stackBarChart.MyValueFormatter;
import com.example.team13.personalbest_team13_skeleton.stackBarChart.MyXAxisValueFormatter;
import com.example.team13.personalbest_team13_skeleton.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class StackedBarChartActivity extends AppCompatActivity {
    private BarChart mChart;
    private DataIOStream dataIOStream;
    int currentTotalSteps;
    int currentPlannedSteps;
    int currentGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stacked_bar_chart);

        Intent intent = getIntent();
        String email = intent.getExtras().getString("EMAIL");
        this.dataIOStream = new DataIOStream(this, email);
        this.currentTotalSteps = intent.getExtras().getInt("currentTotalSteps");
        this.currentPlannedSteps = intent.getExtras().getInt("currentPlannedSteps");
        this.currentGoal = intent.getExtras().getInt("currentGoal");

        mChart = (BarChart) findViewById(R.id.chart1);

        mChart.setMaxVisibleValueCount(40);

        setData(7);
    }

    public void setData(int count) {
        ArrayList<BarEntry> yValues = new ArrayList<>();

        int[] totalsteps = getTotalSteps();
        int[] plannedsteps = getPlannedSteps();
        int maxDailyStep = 0;

        for (int i = 0; i < count; i++) {
            maxDailyStep = Math.max(maxDailyStep, totalsteps[i]);
            int val1 = totalsteps[i] - plannedsteps[i];
            int val2 = plannedsteps[i];

            yValues.add(new BarEntry(i, new float[]{val1, val2}));
        }

        BarDataSet set1;

        set1 = new BarDataSet(yValues, "");
        set1.setDrawIcons(false);
        set1.setStackLabels(new String[] {"Unintentional", "Intentional"});
        set1.setColors(ColorTemplate.createColors(new int[] {rgb(69, 91, 160), rgb(224, 137, 31)}));

        BarData data = new BarData(set1);
        data.setValueFormatter(new MyValueFormatter());
        XAxis xAxis = mChart.getXAxis();
        xAxis.setValueFormatter(new MyXAxisValueFormatter());

        mChart.setData(data);
        mChart.setFitBars(true);
        mChart.invalidate();
        mChart.getDescription().setEnabled(false);

        int currgoal = this.currentGoal;

        mChart.setVisibleYRangeMinimum(Math.max(currgoal + 500, maxDailyStep + 500), YAxis.AxisDependency.LEFT);

        YAxis yAxis = mChart.getAxisLeft();
        LimitLine ll1 = new LimitLine(currgoal, "Goal: " + currgoal);
        ll1.setLineColor(rgb(214, 93, 68));
        ll1.setLineWidth(4);
        //ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        ll1.setTextSize(10f);
        //ll1.setTypeface(tf);
        yAxis.addLimitLine(ll1);
    }

    private int[] getTotalSteps() {
        /*
        SharedPreferences sharedPreferences = getSharedPreferences("week_progress", MODE_PRIVATE);
        return new int[] {sharedPreferences.getInt("Sunday", 0),
                sharedPreferences.getInt("Monday", 0),
                sharedPreferences.getInt("Tuesday", 0),
                sharedPreferences.getInt("Wednesday", 0),
                sharedPreferences.getInt("Thursday", 0),
                sharedPreferences.getInt("Friday", 0),
                sharedPreferences.getInt("Saturday", 0)};*/
        int[] reversed = dataIOStream.readPast7DaysTotalSteps();
        int[] ans = new int[reversed.length];
        for (int i = 0; i < reversed.length - 1; i ++) {
            ans[i] = reversed[reversed.length-1-1-i]; // today + past 6 days
        }
        ans[ans.length-1] = currentTotalSteps;
        return ans;
    }
    private int[] getPlannedSteps() {
        /*SharedPreferences sharedPreferences = getSharedPreferences("week_progress", MODE_PRIVATE);
        return new int[] {sharedPreferences.getInt("SundaySteps", 0),
                sharedPreferences.getInt("MondaySteps", 0),
                sharedPreferences.getInt("TuesdaySteps", 0),
                sharedPreferences.getInt("WednesdaySteps", 0),
                sharedPreferences.getInt("ThursdaySteps", 0),
                sharedPreferences.getInt("FridaySteps", 0),
                sharedPreferences.getInt("SaturdaySteps", 0)};*/
        int[] reversed = dataIOStream.readPast7DaysPlannedSteps();
        int[] ans = new int[reversed.length];
        for (int i = 0; i < reversed.length-1; i ++) {
            ans[i] = reversed[reversed.length-1-1-i]; // today + past 6 days
        }
        ans[ans.length-1] = currentPlannedSteps;
        return ans;
    }
}
