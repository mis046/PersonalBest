package com.example.team13.personalbest_team13_skeleton.friendActivities;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.example.team13.personalbest_team13_skeleton.chatmessage.FirestoreChatAdapter;
import com.example.team13.personalbest_team13_skeleton.friendlist.friendListService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FirestoreFriendActivityAdapter implements IFriendActivityService {

    private static final String TAG = com.example.team13.personalbest_team13_skeleton.friendActivities.FirestoreFriendActivityAdapter.class.getSimpleName();

    private static CollectionReference activities;

    // collection path
    private static final String COLLECTION_KEY = "friends";
    private static String DOCUMENT_KEY = "friendActivity";
    private String userEmail;   // current user's email address

    // each document
    private static final String DATE_KEY = "date";
    private static final String STEP_KEY = "totalStep";
    private static final String PLAN_KEY = "plannedStep";
    private static final String GOAL_KEY = "goal";

    int[] totalSteps;
    int[] plannedSteps;
    int[] goals;

    int i;

    private String friendEmail;

    public FirestoreFriendActivityAdapter(String userEmail) {
        this.userEmail = userEmail;
        this.activities = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(FirestoreChatAdapter.convertToPercent(userEmail));

    }

    public void addData(String date, int totalSteps, int plannedSteps, int goal) {

        Log.e(TAG, "adding current activities");

        //Map<String, HashMap<String, Integer>> currentData = new HashMap<>();
        HashMap<String, Integer> data = new HashMap<>();
        data.put(STEP_KEY, totalSteps);
        data.put(PLAN_KEY, plannedSteps);
        data.put(GOAL_KEY, goal);
        // currentData.put(date, data);

        this.activities.document(date).set(data).addOnSuccessListener(result -> {
            Log.e(TAG, "successfully added current date's activities.");
        }).addOnFailureListener(error -> {
            Log.e(TAG, error.getLocalizedMessage());
        });
    }


    public void getActivities(String friendEmail, Consumer<int[][]> listener) {

        CollectionReference friendActivities = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(FirestoreChatAdapter.convertToPercent(friendEmail));

        String[] dates = getDates();
        Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < dates.length; i++) {
            map.put(dates[i], i);
        }
        totalSteps = new int[28];
        plannedSteps = new int[28];
        goals = new int[28];

        i = 0;

        // Log.d(TAG, "email: " + friendEmail + ", date" + dates[i]);

        friendActivities.addSnapshotListener((newActivitySnapshot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }
            if (newActivitySnapshot != null && !newActivitySnapshot.isEmpty()) {
                List<DocumentSnapshot> documentsList = newActivitySnapshot.getDocuments();

                documentsList.forEach(document -> {
                    //Log.d(TAG, "FOR EACH");

                    if (map.containsKey(document.getId())){
                        int pos = map.get(document.getId());
                        totalSteps[pos] = Integer.valueOf(document.get(STEP_KEY).toString());
                        plannedSteps[pos] = Integer.valueOf(document.get(PLAN_KEY).toString());
                        goals[pos] = Integer.valueOf(document.get(GOAL_KEY).toString());
                    }
                });
                listener.accept(new int[][] {totalSteps, plannedSteps, goals});
            }
        });
    }

    public String[] getDates() {
        String[] dates = new String[28];
        for (int i = 0; i < 28; i++) {
            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime ( date ); // convert your date to Calendar object

            int daysToDecrement = -i;

            cal.add(Calendar.DATE, daysToDecrement);
            date = cal.getTime();

            String strDateFormat = "yyyy-MM-dd";
            DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
            String formattedDate= dateFormat.format(date);

            dates[i] = formattedDate;
        }

        return dates;
    }

    public void setUser(String userEmail) {

    }
}
