package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.team13.personalbest_team13_skeleton.R;

public class DisplayProgressActivity extends AppCompatActivity {
    private ProgressBar progressBarSun;
    private ProgressBar progressBarMon;
    private ProgressBar progressBarTue;
    private ProgressBar progressBarWed;
    private ProgressBar progressBarThu;
    private ProgressBar progressBarFri;
    private ProgressBar progressBarSat;
    private ProgressBar progressBarSunPlan;
    private ProgressBar progressBarMonPlan;
    private ProgressBar progressBarTuePlan;
    private ProgressBar progressBarWedPlan;
    private ProgressBar progressBarThuPlan;
    private ProgressBar progressBarFriPlan;
    private ProgressBar progressBarSatPlan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_progress);

        progressBarSun = (ProgressBar) findViewById(R.id.progressBarSun);
        progressBarMon = (ProgressBar) findViewById(R.id.progressBarMon);
        progressBarTue = (ProgressBar) findViewById(R.id.progressBarTue);
        progressBarWed = (ProgressBar) findViewById(R.id.progressBarWed);
        progressBarThu = (ProgressBar) findViewById(R.id.progressBarThu);
        progressBarFri = (ProgressBar) findViewById(R.id.progressBarFri);
        progressBarSat = (ProgressBar) findViewById(R.id.progressBarSat);

        progressBarSunPlan = (ProgressBar) findViewById(R.id.progressBarSunPlan);
        progressBarMonPlan = (ProgressBar) findViewById(R.id.progressBarMonPlan);
        progressBarTuePlan = (ProgressBar) findViewById(R.id.progressBarTuePlan);
        progressBarWedPlan = (ProgressBar) findViewById(R.id.progressBarWedPlan);
        progressBarThuPlan = (ProgressBar) findViewById(R.id.progressBarThuPlan);
        progressBarFriPlan = (ProgressBar) findViewById(R.id.progressBarFriPlan);
        progressBarSatPlan = (ProgressBar) findViewById(R.id.progressBarSatPlan);

        SharedPreferences sharedPreferences = getSharedPreferences("week_progress", MODE_PRIVATE);
        SharedPreferences sharedPreferencesGoal = getSharedPreferences("goal_num", MODE_PRIVATE);

        String currGoal = sharedPreferencesGoal.getString("goal", "5000");
        int currGoalInt = Integer.parseInt(currGoal);

        int progressSun = sharedPreferences.getInt("Sunday", 0);
        int progressMon = sharedPreferences.getInt("Monday", 0);
        int progressTue = sharedPreferences.getInt("Tuesday", 0);
        int progressWed = sharedPreferences.getInt("Wednesday", 0);
        int progressThu = sharedPreferences.getInt("Thursday", 0);
        int progressFri = sharedPreferences.getInt("Friday", 0);
        int progressSat = sharedPreferences.getInt("Saturday", 0);

        int progressPlannedSun = sharedPreferences.getInt("SundaySteps", 0);
        int progressPlannedMon = sharedPreferences.getInt("MondaySteps", 0);
        int progressPlannedTue = sharedPreferences.getInt("TuesdaySteps", 0);
        int progressPlannedWed = sharedPreferences.getInt("WednesdaySteps", 0);
        int progressPlannedThu = sharedPreferences.getInt("ThursdaySteps", 0);
        int progressPlannedFri = sharedPreferences.getInt("FridaySteps", 0);
        int progressPlannedSat = sharedPreferences.getInt("SaturdaySteps", 0);

        Toast.makeText(DisplayProgressActivity.this, "GoalSettingsActivity: " + currGoal,
                //+ " Day of week: " + 5
                //+ " Sun " + progressSun
                //+ " Mon: " + progressMon
                //+ " Tue: " + progressTue
                //+ " Wed: " + progressWed
                //+ " Thu: " + progressThu
                //+ " Fri: " + progressFri
                //+ " Sat: " + progressSun,
                Toast.LENGTH_LONG).show();

        progressBarSun.setMax(currGoalInt);
        progressBarMon.setMax(currGoalInt);
        progressBarTue.setMax(currGoalInt);
        progressBarWed.setMax(currGoalInt);
        progressBarThu.setMax(currGoalInt);
        progressBarFri.setMax(currGoalInt);
        progressBarSat.setMax(currGoalInt);

        progressBarSun.setProgress(Math.min(currGoalInt, progressSun));
        progressBarMon.setProgress(Math.min(currGoalInt, progressMon));
        progressBarTue.setProgress(Math.min(currGoalInt, progressTue));
        progressBarWed.setProgress(Math.min(currGoalInt, progressWed));
        progressBarThu.setProgress(Math.min(currGoalInt, progressThu));
        progressBarFri.setProgress(Math.min(currGoalInt, progressFri));
        progressBarSat.setProgress(Math.min(currGoalInt, progressSat));


        progressBarSunPlan.setMax(currGoalInt);
        progressBarMonPlan.setMax(currGoalInt);
        progressBarTuePlan.setMax(currGoalInt);
        progressBarWedPlan.setMax(currGoalInt);
        progressBarThuPlan.setMax(currGoalInt);
        progressBarFriPlan.setMax(currGoalInt);
        progressBarSatPlan.setMax(currGoalInt);

        progressBarSunPlan.setProgress(Math.min(currGoalInt, progressPlannedSun)); // TODO: FAKE DATAs
        progressBarMonPlan.setProgress(Math.min(currGoalInt, progressPlannedMon));
        progressBarTuePlan.setProgress(Math.min(currGoalInt, progressPlannedTue));
        progressBarWedPlan.setProgress(Math.min(currGoalInt, progressPlannedWed));
        progressBarThuPlan.setProgress(Math.min(currGoalInt, progressPlannedThu));
        progressBarFriPlan.setProgress(Math.min(currGoalInt, progressPlannedFri));
        progressBarSatPlan.setProgress(Math.min(currGoalInt, progressPlannedSat));

    }
}