package com.example.team13.personalbest_team13_skeleton.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.DataIOStream;
import com.example.team13.personalbest_team13_skeleton.R;

import java.util.Locale;


// Reference code.
// https://codinginflow.com/tutorials/android/custom-dialog-interface


public class CongratsDialog extends AppCompatDialogFragment {
    public static final int GOAL_INCREMENT = 500;
    private CongratsDialogListener listener;
    private int currentGoal;
    private int newGoal;
    private String email;
    private DialogManager dialogManager;

    public CongratsDialog(DialogManager dialogManager) {
        this.dialogManager = dialogManager;
    }

    @Override
    public Dialog onCreateDialog(Bundle saved) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final String TAG = "CongratsDialog";

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.congrats_dialog, null);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            if (bundle.get("goal") != null) {
                currentGoal = (int) bundle.get("goal");
            }
            if (bundle.get("email") != null) {
                email = (String) bundle.get("email");
            }
        }

        builder.setView(view)
                .setTitle(getCongratsTitle())
                .setNegativeButton(getNegativeResponseMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton(getPositiveResponseMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        handlePositiveResponse();
                    }
                });
        TextView goalMsgView = view.findViewById(R.id.goalMsgView);
        goalMsgView.setText(getCongratsBody());

        return builder.create();
    }

    /**
     * Return the Title of the congrats message.
     * @return title of congrats message.
     */
    private String getCongratsTitle() {
        return "Congratulations!";
    }

    /**
     * Get what the main body of the message should say.
     * @return body text of dialog.
     */
    private String getCongratsBody() {
        // Get current goals from shared preference folder.
        Context context = this.getContext();

        newGoal = currentGoal + GOAL_INCREMENT;
        String goalMsg =  String.format(Locale.ENGLISH, "You reached your goal of %d steps!\n\n" +
                "Do you want to set a new goal of %d steps?", currentGoal, newGoal);
        return goalMsg;
    }

    /**
     * Handle accept new suggested goal.
     * TODO (Nate): eventually will want to delegate responses to listener object to preserve SRP.
     */
    private void handlePositiveResponse() {
        if (newGoal > 0) {
            dialogManager.handleNewGoal(newGoal);
            // TODO (Nate): Integrate with the DataIOStream
//            Context context = this.getContext();
//            DataIOStream dataIOStream = new DataIOStream(context, email);
//            dataIOStream.saveFlag(DataIOStream.STR_INC_GOAL, true);
        }
    }

    /**
     * Text for deny suggested goal.
     * @return
     */
    private String getNegativeResponseMessage() {
        return "Not this time";
    }

    /**
     * Test for accept suggested goal.
     * @return
     */
    private String getPositiveResponseMessage() {
        return "Yes, challenge me!";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (CongratsDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement CongratsDialogListener");
        }
    }

    public interface CongratsDialogListener {
    }
}