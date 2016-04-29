package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;

public class StartGameActivity extends AppCompatActivity {

    private String gameMode = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent settings = getIntent();
        if (settings.hasExtra("Game Mode")) {
            this.gameMode = settings.getStringExtra("Game Mode");
        } else {
            this.gameMode = "PVC";
        }

        // set up the spinners
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.StartGameActivity_ai_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner whiteSpinner = (Spinner) findViewById(R.id.white_ai_spinner);
        Spinner blackSpinner = (Spinner) findViewById(R.id.black_ai_spinner);
        whiteSpinner.setAdapter(adapter);
        blackSpinner.setAdapter(adapter);
    }

    public void newGame(View button) {
        Intent newGameIntent = new Intent(getApplicationContext(), GameBoardActivity.class);

        Bundle settingsBundle = makeSettingsBundle();
        settingsBundle.putString("Type", "New");
        settingsBundle.putString("Game Type", "New");
        newGameIntent.putExtra("Settings", settingsBundle);

        startActivity(newGameIntent);
        finish();
    }

    public void createGame(View button){
        Intent createGameIntent = new Intent(getApplicationContext(), CustomGame.class);
        Bundle playercount = new Bundle();
        playercount.putInt("players", 1);
        createGameIntent.putExtra("Type",playercount);

        Bundle settingsBundle = makeSettingsBundle();
        settingsBundle.putString("Game Type", "Custom");
        createGameIntent.putExtra("Settings", settingsBundle);

        startActivity(createGameIntent);
    }

    public void loadGame(View button){
        Intent loadGameIntent = new Intent(getApplicationContext(), LoadGameActivity.class);
        Bundle game = new Bundle();
        game.putInt("game", 4);
        loadGameIntent.putExtra("start", game);

        Bundle settingsBundle = makeSettingsBundle();
        settingsBundle.putString("Game Type", "Load");
        loadGameIntent.putExtra("Settings", settingsBundle);

        startActivity(loadGameIntent);
    }

    private Bundle makeSettingsBundle() {
        Bundle settings = new Bundle();
        SeekBar difficultySeekBar = (SeekBar) findViewById(R.id.seek_bar_difficulty);
        int difficulty = difficultySeekBar.getProgress();
        settings.putInt("Difficulty", difficulty);

        RadioButton radioButtonWhite = (RadioButton)this.findViewById(R.id.button_white);
        if (radioButtonWhite.isChecked()) {
            settings.putString("Color", "White");
        } else {
            settings.putString("Color", "Black");
        }

        settings.putString("Game Mode", this.gameMode);

        double whiteAI = GameBoardActivity.STRATEGY_BALANCED;
        Spinner whiteAISpinner = (Spinner) findViewById(R.id.white_ai_spinner);
        String whiteAIStrategy = whiteAISpinner.getSelectedItem().toString();
        if (whiteAIStrategy.equalsIgnoreCase("Aggressive")) whiteAI = GameBoardActivity.STRATEGY_AGGRESSIVE;
        else if (whiteAIStrategy.equalsIgnoreCase("Defensive")) whiteAI = GameBoardActivity.STRATEGY_DEFENSIVE;
        else whiteAI = GameBoardActivity.STRATEGY_BALANCED;
        settings.putDouble("White Strategy", whiteAI);

        double blackAI = GameBoardActivity.STRATEGY_BALANCED;
        Spinner blackAISpinner = (Spinner) findViewById(R.id.black_ai_spinner);
        String blackAIStrategy = blackAISpinner.getSelectedItem().toString();
        if (blackAIStrategy.equalsIgnoreCase("Aggressive")) blackAI = GameBoardActivity.STRATEGY_AGGRESSIVE;
        else if (blackAIStrategy.equalsIgnoreCase("Defensive")) blackAI = GameBoardActivity.STRATEGY_DEFENSIVE;
        else blackAI = GameBoardActivity.STRATEGY_BALANCED;
        settings.putDouble("Black Strategy", blackAI);

        return settings;
    }

    public void onBackPressed(){
        finish();
    }

}
