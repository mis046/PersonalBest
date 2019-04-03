package com.example.team13.personalbest_team13_skeleton;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.team13.personalbest_team13_skeleton.activities.ChatActivity;

public class ChatRecyclerViewAdapter extends RecyclerView.Adapter<ChatRecyclerViewAdapter.MyViewHolder>{
    private static final String TAG = "RecyclerViewAdapter";

    private String[] mDataset;

    private ChatActivity chatActivity;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "MyViewHolder";

        // each data item is just a string in this case
        public TextView textView;
        public MyViewHolder(View v, ChatActivity chatActivity, String[] mDataset) {
            super(v);
            // Define click listener for the ViewHolder's View.
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "Element " + getAdapterPosition() + " clicked.");
                    chatActivity.openChatBox(mDataset[getAdapterPosition()]);
                }
            });
            textView = (TextView) v.findViewById(R.id.textView);
        }
        public TextView getTextView() {
            return textView;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ChatRecyclerViewAdapter(String[] myDataset, ChatActivity chatActivity) {
        mDataset = myDataset;
        this.chatActivity = chatActivity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // create a new view
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friend_list_view, viewGroup, false);

        return new MyViewHolder(v, chatActivity, mDataset);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Log.d(TAG, "Element " + position + " set.");
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.getTextView().setText(mDataset[position]);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}
