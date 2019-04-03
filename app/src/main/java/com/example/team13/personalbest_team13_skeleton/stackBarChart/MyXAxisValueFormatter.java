package com.example.team13.personalbest_team13_skeleton.stackBarChart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

public class MyXAxisValueFormatter implements IAxisValueFormatter {
    private String[] mValues = new String[] {""};

    public MyXAxisValueFormatter(/*String[] values*/) {
        //this.mValues = values;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // "value" represents the position of the label on the axis (x or y)
        return mValues[0];
    }

    /** this is only needed if numbers are returned, else return 0 */
    //@Override
    //public int getDecimalDigits() { return 0; }
}
