package com.conorstephens.diceroller.ui.history;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;

import com.conorstephens.diceroller.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RollHistoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ArrayList<JSONObject> mHistory;
    private RollHistoryAdapter mHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roll_history);

        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        initRecyclerView();
        getHistory();
    }

    /**
     * Initializes the recyclerView that will store the rolls
     */
    private void initRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerview_history);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(OrientationHelper.VERTICAL);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(llm);

        DividerItemDecoration myDivider = new DividerItemDecoration(this, llm.getOrientation());
        myDivider.setDrawable(this.getResources().getDrawable(R.drawable.light_line));

        mRecyclerView.addItemDecoration(myDivider);
    }

    /**
     * Gets the history that was passed to this activity as a String, and converts the string to
     * a JSONArray, and then calls getListOfJsonObject to convert the array to an arraylist of
     * JSON objects. Sets the adapter for the recyclerView
     */
    private void getHistory() {
        if (getIntent().hasExtra("rollHistory")) {
            String historyString = getIntent().getStringExtra("rollHistory");
            try {
                JSONArray jsonArray = new JSONArray(historyString);
                mHistory = getListOfJsonObject(jsonArray);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (mHistory != null) {
                mHistoryAdapter = new RollHistoryAdapter(this, mHistory);
                mRecyclerView.setAdapter(mHistoryAdapter);
            }
        }
    }

    /**
     * Takes in a JSON array and returns an ArrayList of JSON objects. Each object in the array is
     * added to the ArrayList
     * @param jsonArray
     * @return
     */
    public static ArrayList<JSONObject> getListOfJsonObject(
            JSONArray jsonArray) {
        ArrayList<JSONObject> list = new ArrayList<>();

        if (jsonArray != null && jsonArray.length() > 0) {

            for (int index = 0; index < jsonArray.length(); index++) {
                JSONObject jsonObject = null;

                try {
                    jsonObject = jsonArray.getJSONObject(index);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (jsonObject != null) {
                    list.add(jsonObject);
                }
            }
        }
        return list;
    }

}
