package com.example.team13.personalbest_team13_skeleton.friendlist;

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.EditText;

import com.example.team13.personalbest_team13_skeleton.DataIOStream;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class FirestoreFriendListAdapter implements friendListService {

    private static FirestoreFriendListAdapter singeleton;

    private static final String TAG = FirestoreFriendListAdapter.class.getSimpleName();

    private static CollectionReference friends;

    // collection path
    private static final String COLLECTION_KEY = "friends";
    private static String DOCUMENT_KEY = "friendList";
    private String userEmail;   // current user's email address

    // each document
    private static final String FRIEND_KEY = "friend";
    private static final String SUCCESS_KEY = "success";

    boolean pended = false;
    boolean added = false;

    private String friendEmail;

    public FirestoreFriendListAdapter(String userEmail) {
        this.userEmail = userEmail;
        this.friends = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(userEmail);

    }

    /*
    public static friendListService getInstance(String useremail) {
        if (singeleton == null) {
            USER_EMAIL_KEY = useremail;
            CollectionReference collection = FirebaseFirestore.getInstance()
                    .collection(COLLECTION_KEY)
                    .document(DOCUMENT_KEY)
                    .collection(USER_EMAIL_KEY);
            singeleton = new FirestoreFriendListAdapter(collection);
        }
        return singeleton;
    }*/

    public void addFriend(Map<String, String> friendRequest, EditText editFriendEmail) {

        //checkPending(friendRequest, messageView);
        Log.e(TAG, "adding friend");
        friendEmail = friendRequest.get(FRIEND_KEY);

        class CheckingAsync extends AsyncTask<String, String, String> {

            @Override
            protected String doInBackground(String... params) {
                Log.e(TAG, "do in background");
                checkAdded();
                checkPending();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(String res) {
                Log.e(TAG, "finished checking added and pending");
                if (!added) {
                    if (pended) {
                        Log.e(TAG, "friend requested, update!");
                        friendRequest.put(SUCCESS_KEY, "true");
                        updateFriendCollection();
                    }
                    friends.add(friendRequest).addOnSuccessListener(result -> {
                        editFriendEmail.setText("");
                    }).addOnFailureListener(error -> {
                        Log.e(TAG, error.getLocalizedMessage());
                    });
                } else {
                  editFriendEmail.setText("");
                }
            }

            @Override
            protected void onPreExecute() {

            }

            @Override
            protected void onProgressUpdate(String... text) {
            }

        }

        CheckingAsync runner = new CheckingAsync();
        runner.execute();
    }

    public void getFriends(String userEmail, Consumer<List<String>> listener) {
        ArrayList<String> realDatasets = new ArrayList<>();
        friends.addSnapshotListener((newFriendSnapShot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }
            if (newFriendSnapShot != null && !newFriendSnapShot.isEmpty()) {
                List<DocumentChange> documentChanges = newFriendSnapShot.getDocumentChanges();
                documentChanges.forEach(change -> {
                    QueryDocumentSnapshot document = change.getDocument();
                    if (document.get(SUCCESS_KEY).equals("true")) { // check if real friend
                        realDatasets.add(document.get(FRIEND_KEY).toString());
                        //Log.e(TAG, document.get(FRIEND_KEY).toString());
                    }
                });
                listener.accept(realDatasets);
                Log.e(TAG,  "realDatabase size: " + String.valueOf(realDatasets.size()));
            }
        });
    }

    // check whether the friend has added the user already
    public void checkPending() {
        Log.e(TAG, "in checkPending");
        CollectionReference collection = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(friendEmail);
        collection.whereEqualTo(FRIEND_KEY, userEmail)
            .limit(1).get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        pended = !task.getResult().isEmpty();
                        if (pended) {
                            Log.e(TAG, "friends already requested");
                        } else {
                            Log.e(TAG, "the friend has not added you");
                        }
                    }
                }
            });
    }

    // check whether already added the friend already
    public void checkAdded() {
        Log.e(TAG, "in checkAddedThisFriendBefore");
        CollectionReference collection = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(userEmail);
        collection.whereEqualTo(FRIEND_KEY, friendEmail)
                .limit(1).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            added = !task.getResult().isEmpty();
                            if (added) {
                                Log.e(TAG, "already added this friend");
                            } else {
                                Log.e(TAG, "first time add this friend");
                            }
                        }
                    }
                });
    }


    // when both friends add each other, update friend's friend list
    public void updateFriendCollection() {
        CollectionReference collection = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(DOCUMENT_KEY)
                .collection(friendEmail);
        //collection.document(USER_EMAIL_KEY).delete();

        Map<String, String> updateRequest = new HashMap<>();
        updateRequest.put(FRIEND_KEY, userEmail);
        updateRequest.put(SUCCESS_KEY, "true");

        collection.add(updateRequest);
    }

    public void setUser(String userEmail) {

    }
}
