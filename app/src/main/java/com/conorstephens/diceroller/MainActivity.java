package com.conorstephens.diceroller;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FOLDER = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DiceRoller/";
    private static final String ROLL_HISTORY_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + "/DiceRoller/history.json";
    private static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 2;

    private Spinner mDiceSpinner;
    private TextView mDiceResult;
    private JSONArray mRollHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        //Permission to write external storage is required because that app store history in a file
        if (permissionGranted(MY_PERMISSIONS_REQUEST_WRITE_STORAGE)) {
            initRollHistory();
        }
    }

    /**
     * Matching up UI elements to their views, and setting click listeners
     */
    private void initUI() {
        mDiceSpinner = findViewById(R.id.spinner_dice_number);
        mDiceResult = findViewById(R.id.textview_dice_result);
        findViewById(R.id.button_roll_dice).setOnClickListener(this);
        findViewById(R.id.button_roll_history).setOnClickListener(this);

        //This is for the spinner that allows the user to select the dice they are rolling
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dice_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDiceSpinner.setAdapter(adapter);
    }

    //Loads the data from the file that stores the roll history
    private void initRollHistory() {
        File file = new File(ROLL_HISTORY_FILE);
        if (file.exists()) {
            String json = null;
            try {
                FileInputStream is = new FileInputStream(file);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                mRollHistory = new JSONArray(json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            mRollHistory = new JSONArray();
        }
    }

    /**
     * Adds a roll to the in-app roll history, mRollHistory
     * @param dice The number of sides on the dice that was used
     * @param result The result of the roll
     */
    private void addRollToHistory(int dice, int result) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("dice", dice);
            jsonObject.put("result", result);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mRollHistory.put(jsonObject);
        Log.d(TAG, mRollHistory.toString());
    }

    /**
     * Returns a random number
     * @param max The max number that will be returned, 1 is always the minimum
     * @return the random number
     */
    private int rollDice(int max) {
        int range = (max - 1) + 1;
        int result = (int) (Math.random() * range) + 1;

        return result;
    }

    /**
     * Matches the index of the spinner to the corresponding dice.
     * Calls rollDice to get the result and addRollToHistory to add the roll to the in-app history
     */
    private void setDiceResult() {
        int selectedPosition = mDiceSpinner.getSelectedItemPosition();
        int max = 0;

        switch (selectedPosition) {
            case 0:
                max = 4;
                break;
            case 1:
                max = 6;
                break;
            case 2:
                max = 8;
                break;
            case 3:
                max = 10;
                break;
            case 4:
                max = 12;
                break;
            case 5:
                max = 20;
        }
        int result = rollDice(max);
        addRollToHistory(max, result);
        mDiceResult.setText(String.valueOf(result));
    }


    @Override
    public void onClick(View view) {
        if (permissionGranted(MY_PERMISSIONS_REQUEST_WRITE_STORAGE)) {
            switch (view.getId()) {
                case R.id.button_roll_dice:
                    setDiceResult();
                    break;
                case R.id.button_roll_history:
                    Intent intent = new Intent(this, RollHistoryActivity.class);
                    intent.putExtra("rollHistory", mRollHistory.toString());
                    startActivity(intent);
            }
        } else{
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }

    /**
     * When the activity leaves the screen, the app will save it's current history to a json file
     * in external storage.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (permissionGranted(MY_PERMISSIONS_REQUEST_WRITE_STORAGE)) {
            File folder = new File(FOLDER);
            if (!folder.exists()) {
                folder.mkdirs();
            }
            File file = new File(folder, "history.json");
            try {
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(mRollHistory.toString());
                fileWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Fires permission requests when user attempts to access feature. The only permission requested
     * is to write external storage.
     */
    private boolean permissionGranted(int requestCode) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                if (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED) {
                    return true;
                } else {
                    return false;
                }
        }
        return false;
    }

    /**
     * Handles results of permission requests
     *
     * @param requestCode  Code for the permission that was requested
     * @param permissions  Permissions that were requested
     * @param grantResults Results of the permission requests
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initRollHistory();
                } else {
                    // permission denied
                }
            }
        }
    }
}
