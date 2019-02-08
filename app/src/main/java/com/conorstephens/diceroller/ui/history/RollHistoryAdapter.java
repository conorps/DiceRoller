package com.conorstephens.diceroller.ui.history;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.conorstephens.diceroller.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RollHistoryAdapter extends RecyclerView.Adapter<RollHistoryAdapter.ViewHolder> {

    private static final String TAG = RollHistoryAdapter.class.getSimpleName();
    private ArrayList<JSONObject> mHistory;
    private final Context mActivity;

    public RollHistoryAdapter(Context context, ArrayList<JSONObject> mHistory) {
        this.mActivity = context;
        this.mHistory = mHistory;
    }

    /**
     * This class represents each individual item in the RecyclerView
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView diceNumber;
        TextView result;

        ViewHolder(View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardview_history);
            diceNumber = itemView.findViewById(R.id.textview_history_dice);
            result = itemView.findViewById(R.id.textview_history_result);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item_roll, viewGroup, false);
        return new ViewHolder(view);
    }

    /**
     * Binds the data in the mHistory ArrayList to the UI for each RecyclerView Item
     * @param viewHolder The class that represents each item in the list
     * @param index The index of the item in the ArrayList
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int index) {
        final JSONObject jsonObject = mHistory.get(mHistory.size() - 1 -index);
        try {
            viewHolder.diceNumber.setText("D" + jsonObject.getString("dice"));
            viewHolder.result.setText(jsonObject.getString("result"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        viewHolder.cardView.setTag(mHistory.size() - 1 - index);
    }

    /**
     * Returns the size of the ArrayList
     * @return
     */
    @Override
    public int getItemCount() {
        return mHistory.size();
    }
}

