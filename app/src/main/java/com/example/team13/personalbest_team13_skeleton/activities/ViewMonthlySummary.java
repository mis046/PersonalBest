package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import com.example.team13.personalbest_team13_skeleton.R;
import com.example.team13.personalbest_team13_skeleton.chatmessage.ChatMessageService;
import com.example.team13.personalbest_team13_skeleton.chatmessage.FirestoreChatAdapter;
import com.example.team13.personalbest_team13_skeleton.friendActivities.FirestoreFriendActivityAdapter;
import com.example.team13.personalbest_team13_skeleton.friendActivities.IFriendActivityService;
import com.example.team13.personalbest_team13_skeleton.stackBarChart.MyValueFormatter;
import com.example.team13.personalbest_team13_skeleton.stackBarChart.MyXAxisValueFormatter;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.graphics.Color.rgb;

public class ViewMonthlySummary extends AppCompatActivity {
    private String friendEmail;
    private String userEmail;
    private BarChart mChart;
    private IFriendActivityService firestoreFriendActivityAdapter;
    private ChatMessageService firestoreChatAdapter;

    String FROM_KEY = "from";
    String TEXT_KEY = "text";
    String TIMESTAMP_KEY = "timestamp";

    String TAG = ViewMonthlySummary.class.getSimpleName();

    int[][] dataset = new int[3][28];
    EditText msgBox;
    Button sendBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_monthly_summary);

        Intent intent = getIntent();
        //this.friendEmail = intent.getExtras().getString("friendEmail");
        this.userEmail = intent.getExtras().getString("userEmail");
        // this.friendEmail = "zhl270@ucsd.edu"; // for test

        Log.d(TAG, "query user email in collection : " + userEmail);
        this.firestoreFriendActivityAdapter = new FirestoreFriendActivityAdapter(userEmail);

        mChart = (BarChart) findViewById(R.id.chart1);
        mChart.setMaxVisibleValueCount(40);
        setData(28);


    }

    public void setData(int count) {
        ArrayList<BarEntry> yValues = new ArrayList<>();
        firestoreFriendActivityAdapter.getActivities(userEmail,
                realDBArr -> {
                    for (int i = 0; i < realDBArr.length; i++) {
                        for(int j = 0; j < realDBArr[0].length; j++) {
                            dataset[i][j] = realDBArr[i][j];
                        }
                    }

                    int[] totalsteps = reverse(dataset[0]);
                    int[] plannedsteps = reverse(dataset[1]);
                    int currgoal = dataset[2][0];

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
                    Log.d("in view friends activities", Integer.toString(dataset[0][0]));
                    //dataset = realDBArr;
                });
    }

    private int[] reverse (int[] original) {
        int[] ans = new int[original.length];
        for (int i = 0; i < original.length; i ++) {
            ans[i] = original[original.length-1-i];
        }
        return ans;
    }
}
