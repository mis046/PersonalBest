package com.example.team13.personalbest_team13_skeleton.stackBarChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter {
    private DecimalFormat mformat;

    public MyAxisValueFormatter() {
        mformat = new DecimalFormat("######.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mformat.format(value);
    }
}
