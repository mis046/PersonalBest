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

import com.example.team13.personalbest_team13_skeleton.Person;
import com.example.team13.personalbest_team13_skeleton.R;

import org.w3c.dom.Text;

import static java.lang.Integer.parseInt;

public class SettingsActivity extends AppCompatActivity {

    float height;
    float strideLen;

    Button btnUpdate;
    EditText inputHeight;
    TextView currHeight;
    TextView currStride;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        height = intent.getExtras().getFloat("HEIGHT");
        strideLen = intent.getExtras().getFloat("STRIDE_LENGTH");

        setContentView(R.layout.activity_settings);

        btnUpdate = (Button)findViewById(R.id.btnUpdateHeight);
        inputHeight = (EditText)findViewById(R.id.inputHeight);
        currHeight = (TextView)findViewById(R.id.currHeight);
        currStride = (TextView)findViewById(R.id.currStrideLength);

//        final SharedPreferences sharedPreferences = getSharedPreferences("height_stride", MODE_PRIVATE);
//        String height = sharedPreferences.getString("height", "Default: 72");
//        String stride = sharedPreferences.getString("stride", "Default: 29");

        display();

        btnUpdate.setOnClickListener(
                new View.OnClickListener()
                {
                    public void onClick(View view)
                    {

                        save(view);
                        display();
                    }
                });
    }

    public void save(View view) {
        EditText heightEditText = findViewById(R.id.inputHeight);
        String newHeight = heightEditText.getText().toString();

        if (newHeight.trim().length() > 0 && parseInt(newHeight) >= 1) {
            height = parseInt(newHeight);
            strideLen = Person.heightToStride(height);
            Toast.makeText(SettingsActivity.this, "Saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SettingsActivity.this, "Height must be at least 1", Toast.LENGTH_SHORT).show();
        }
    }

    public void display() {
        currHeight.setText(String.valueOf(height));
        currStride.setText(String.valueOf(strideLen));
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("NEW_HEIGHT", height);
        setResult(RESULT_OK, intent);
        finish();
    }
}