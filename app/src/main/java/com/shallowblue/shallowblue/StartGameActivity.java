package com.shallowblue.shallowblue;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.MenuItem;
import android.support.v4.app.NavUtils;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_game);

        // set up the spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.StartGameActivity_ai_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner opponentSpinner = (Spinner) findViewById(R.id.opponent_ai_spinner);
        Spinner suggestionSpinner = (Spinner) findViewById(R.id.suggestion_ai_spinner);
        opponentSpinner.setAdapter(adapter);
        suggestionSpinner.setAdapter(adapter);
    }

    public void newGame(View button) {
        Intent newGameIntent = new Intent(getApplicationContext(), GameBoardActivity.class);
        startActivity(newGameIntent);
    }

}
