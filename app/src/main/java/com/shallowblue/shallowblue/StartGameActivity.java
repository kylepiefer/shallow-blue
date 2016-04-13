package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
        RadioButton radioButtonWhite = (RadioButton)this.findViewById(R.id.button_white);
        if (radioButtonWhite.isChecked())
            newGameIntent.putExtra("Color", "White");
        else
            newGameIntent.putExtra("Color", "Black");
        startActivity(newGameIntent);
        finish();
    }

    public void createGame(View button){
        Intent createGameIntent = new Intent(getApplicationContext(), CustomGame.class);
        Bundle playercount = new Bundle();
        playercount.putInt("players", 1);
        createGameIntent.putExtra("type",playercount);
        startActivity(createGameIntent);
        finish();
    }

    public void loadGame(View button){
        Intent loadGameIntent = new Intent(getApplicationContext(), loadgame.class);
        Bundle game = new Bundle();
        game.putInt("game", 4);
        loadGameIntent.putExtra("start", game);
        startActivity(loadGameIntent);
    }

    public void onBackPressed(){
        finish();
    }

}
