package com.example.team13.personalbest_team13_skeleton.dialogs;


import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.R;


// Reference code.
// https://codinginflow.com/tutorials/android/custom-dialog-interface

public class StatsDialog extends AppCompatDialogFragment {
    private TextView stepsText;
    private TextView distanceText;
    private TextView speedText;
    private TextView timeText;
    private StatsDialogListener listener;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.stats_dialog, null);

        builder.setView(view)
                .setTitle("Nice Job! Here are your stats for this walk/run.")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

        stepsText = view.findViewById(R.id.steps);
        distanceText = view.findViewById(R.id.distance);
        speedText = view.findViewById(R.id.speed);
        timeText = view.findViewById(R.id.time);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if(bundle.get("steps") != null) {
                Double st = (Double) bundle.get("steps");
                String stTemp = String.format(getString(R.string.pwSteps), st);
                stepsText.setText(stTemp);
            }
            if(bundle.get("distance") != null) {
                Double dist = (Double) bundle.get("distance");
                String distTemp = String.format(getString(R.string.pwDistance), dist);
                distanceText.setText(distTemp);
            }
            if(bundle.get("speed") != null) {
                Double sp = (Double) bundle.get("speed");
                String spTemp = String.format(getString(R.string.pwSpeed), sp);
                speedText.setText(spTemp);
            }
            if(bundle.get("time") != null){
                Double time = (Double) bundle.get("time");
                String timeTemp = String.format(getString(R.string.pwTime), time);
                timeText.setText(timeTemp);
            }
        }
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (StatsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface StatsDialogListener {
    }

}
