package com.example.team13.personalbest_team13_skeleton.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.team13.personalbest_team13_skeleton.R;

import static java.lang.Integer.parseInt;

public class GoalSettingsActivity extends AppCompatActivity {
    int goal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        goal = intent.getExtras().getInt("GOAL");
        //goal = 1000;
        setContentView(R.layout.activity_goal);

        Button btn_save = (Button) findViewById(R.id.btn_save);
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(v);
                display(v);
            }
        });

        display(btn_save);
    }

    public void save(View view) {
        EditText goalEditText = findViewById(R.id.editGoal);
        String newGoal = goalEditText.getText().toString();
        if (newGoal.trim().length() > 0 && parseInt(newGoal) >= 1) {
            goal = parseInt(newGoal);
            Toast.makeText(GoalSettingsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(GoalSettingsActivity.this, "Goal must be at least 1 step", Toast.LENGTH_SHORT).show();
        }
    }

    public void display(View v) {
        TextView displayGoal = (TextView) findViewById(R.id.textGoal);
        displayGoal.setText("Goal: "+ goal + " steps");
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("NEW_GOAL", goal);
        setResult(RESULT_OK, intent);
        finish();
    }
}