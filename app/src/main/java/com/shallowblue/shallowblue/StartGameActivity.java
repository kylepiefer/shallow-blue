package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
    public void createGame(View button){
        Intent createGameIntent = new Intent(getApplicationContext(), CustomGame.class);
        startActivity(createGameIntent);
    }
    public void loadGame(View button){
        Intent loadGameIntent = new Intent(getApplicationContext(), loadgame.class);
        startActivity(loadGameIntent);
    }

}
