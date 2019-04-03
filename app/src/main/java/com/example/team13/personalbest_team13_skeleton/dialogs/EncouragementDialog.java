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


public class EncouragementDialog extends AppCompatDialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.encouragement_dialog, null);

        builder.setView(view)
                .setTitle(getEncouragementTitle())
//                .setNegativeButton(getNegativeResponseMessage(), new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                    }
//                })
                .setPositiveButton(getPositiveResponseMessage(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
        TextView goalMsgView = view.findViewById(R.id.encouragementMsgView);
        goalMsgView.setText(getEncouragementBody());

        return builder.create();
    }

    private String getEncouragementTitle() {
        return "You are almost there!";
    }

    /**
     * Text that goes in the body of the dialog window.
     * @return
     */
    private String getEncouragementBody() {
        return "You increased your steps from last time!";
    }

    private String getNegativeResponseMessage() {
        return "Not this time";
    }

    private String getPositiveResponseMessage() {
        return "Woot!";
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

}