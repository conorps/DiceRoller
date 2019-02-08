package com.conorstephens.diceroller.ui.main;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.conorstephens.diceroller.R;
import com.conorstephens.diceroller.Utils;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    private Spinner mDiceSpinner;
    private TextView mDiceResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_roll_dice:
                int result = Utils.rollDice(Utils.getDiceMax(mDiceSpinner.getSelectedItemPosition()));
                mDiceResult.setText(String.valueOf(result));
                break;
            case R.id.button_roll_history:
//                Intent intent = new Intent(this, RollHistoryActivity.class);
//                startActivity(intent);
        }
    }
}