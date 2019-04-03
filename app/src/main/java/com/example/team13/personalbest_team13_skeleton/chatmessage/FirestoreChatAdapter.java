package com.example.team13.personalbest_team13_skeleton.chatmessage;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class FirestoreChatAdapter implements ChatMessageService {
    //private static FirestoreChatAdapter singeleton;

    private static final String TAG = FirestoreChatAdapter.class.getSimpleName();

    private String userEmail;
    private String friendEmail;

    private static final String COLLECTION_KEY = "chats";
    private static final String COLLECTION_KEY2 = "people";
    private static final String MESSAGE_KEY = "message";
    private static final String TIMESTAMP_KEY = "timestamp";
    private static final String FROM_KEY = "from";
    private static final String TEXT_KEY = "text";


    private CollectionReference chat;
    private CollectionReference chatList;
    private DocumentReference chatEntry;

    public FirestoreChatAdapter(String userEmail, String friendEmail) {
        this.userEmail = userEmail;
        this.friendEmail = friendEmail;
        this.chat = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(convertToPercent(userEmail))
                .collection(COLLECTION_KEY2)
                .document(convertToPercent(friendEmail))
                .collection(MESSAGE_KEY);
        this.chatEntry = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(convertToPercent(userEmail))
                .collection(COLLECTION_KEY2)
                .document(convertToPercent(friendEmail));
    }

    public FirestoreChatAdapter(String userEmail) {
        this.userEmail = userEmail;
        this.chatList = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(convertToPercent(userEmail))
                .collection(COLLECTION_KEY2);
    }

    public static String convertToPercent(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '@') {
                sb.append('%');
            } else {
                sb.append(input.charAt(i));
            }
        }
        return sb.toString();
    }

    public static String convertToAt(String input) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < input.length(); i++) {
            if (input.charAt(i) == '%') {
                sb.append('@');
            } else {
                sb.append(input.charAt(i));
            }
        }
        return sb.toString();
    }

    @Override
    public Task<?> addMessage(Map<String, String> message) {
        Map<String, Object> map = new HashMap<>();
        map.put("notNull", "true");
        chatEntry.set(map);
        updateFriendsHistory(message, map);
        return chat.add(message);
    }

    public void updateFriendsHistory(Map<String, String> message, Map<String, Object> map) {
        CollectionReference friendHistory = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(convertToPercent(friendEmail))
                .collection(COLLECTION_KEY2)
                .document(convertToPercent(userEmail))
                .collection(MESSAGE_KEY);
        Map<String, String> newMap = new HashMap<>();
        newMap.put(FROM_KEY, message.get(FROM_KEY));
        newMap.put(TEXT_KEY, message.get(TEXT_KEY));
        friendHistory.add(newMap);

        DocumentReference friendChatList = FirebaseFirestore.getInstance()
                .collection(COLLECTION_KEY)
                .document(convertToPercent(friendEmail))
                .collection(COLLECTION_KEY2)
                .document(convertToPercent(userEmail));
        friendChatList.set(map);
    }

    @Override
    public void addOrderedMessagesListener(Consumer<List<ChatMessage>> listener) {
        chat.orderBy(TIMESTAMP_KEY, Query.Direction.ASCENDING)
                .addSnapshotListener((newChatSnapShot, error) -> {
                    if (error != null) {
                        Log.e(TAG, error.getLocalizedMessage());
                        return;
                    }

                    if (newChatSnapShot != null && !newChatSnapShot.isEmpty()) {
                        if (!newChatSnapShot.getMetadata().hasPendingWrites()) {
                            List<DocumentChange> documentChanges = newChatSnapShot.getDocumentChanges();

                            List<ChatMessage> newMessages = documentChanges.stream()
                                    .map(DocumentChange::getDocument)
                                    .map(doc -> new ChatMessage(doc.getString(FROM_KEY), doc.getString(TEXT_KEY)))
                                    .collect(Collectors.toList());

                            Log.d(TAG, "added ordered list");
                            listener.accept(newMessages);
                        }
                    }
                });
    }

    public void getChatList(Consumer<List<String>> listener) {


        chatList.addSnapshotListener((newActivitySnapshot, error) -> {
            if (error != null) {
                Log.e(TAG, error.getLocalizedMessage());
                return;
            }

            Log.d(TAG, "print email: " + COLLECTION_KEY + ", " + userEmail + ", " + COLLECTION_KEY2);

            if (newActivitySnapshot != null && !newActivitySnapshot.isEmpty()) {
                Log.d(TAG, "check");

                List<DocumentSnapshot> documentsList = newActivitySnapshot.getDocuments();

                Log.d(TAG, "in getChatList");

                List<String> res = new ArrayList<>();
                documentsList.forEach(document -> {
                    //Log.d(TAG, "FOR EACH");
                    res.add(convertToAt(document.getId()));
                });
                listener.accept(res);
            }
        });
    }

    public String getContent() {
        return null;
    }
}
