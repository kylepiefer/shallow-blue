package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class LoadGameActivity extends AppCompatActivity {
    private String gameMode;

    private SavedGameManager savedGameManager;

    public Intent nextActivity;

    private LoadGameActivity loadGameActivity;

    private Bundle settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.savedGameManager = new SavedGameManager();
        List<String> savedGameFileNames = this.savedGameManager.getSavedGameFileNames(this);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, savedGameFileNames);

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(fileNameClickedHandler);

        Intent check = getIntent();

        this.settings = check.getBundleExtra("Settings");

        if (check.hasExtra("next")){
            Bundle next = check.getBundleExtra("next");
            String activity = next.getString("activity");
            if (activity.equals("load")){
                nextActivity = new Intent(getApplicationContext(), LeaveGameVerify.class);
                nextActivity.putExtra("next",next);
            }
        }
        else if (check.hasExtra("start")){
            Bundle next = check.getBundleExtra("start");
            int activity = next.getInt("game");
            if (activity == 3){
                Bundle start = new Bundle();
                start.putInt("game",0);
                nextActivity = new Intent(getApplicationContext(), PVPGameBoard.class);
                nextActivity.putExtra("start",start);
            }
            else {
                nextActivity = new Intent(getApplicationContext(), GameBoardActivity.class);
            }
        }

        if (this.settings != null) {
            nextActivity.putExtra("Settings", this.settings);
            this.gameMode = this.settings.getString("Game Mode");
        } else {
            this.gameMode = "PVP";
        }
        loadGameActivity = this;
    }

    public void startGame() {
        Intent startGame = nextActivity;
        if (gameMode.equals("PVC")) {
            startGame = new Intent(getApplicationContext(), GameBoardActivity.class);
            startGame.putExtra("Game Mode", "PVC");
        } else if (gameMode.equals("PVP")) {
            startGame = new Intent(getApplicationContext(), PVPGameBoard.class);
            startGame.putExtra("Game Mode", "PVP");
        } else {
            startGame = new Intent(getApplicationContext(), GameBoardActivity.class);
            startGame.putExtra("Game Mode", "CVC");
        }

        startGame.putExtra("Settings", this.settings);
        startActivity(startGame);
        finish();
    }

    public void loadSavedGame(View button) {
        startActivity(nextActivity);
    }

    public void onBackPressed(){
        finish();
    }

    private AdapterView.OnItemClickListener fileNameClickedHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String fileName = (String) parent.getItemAtPosition(position);
            boolean result = savedGameManager.loadGame(loadGameActivity, fileName);
            if (result) startGame();
        }
    };
}
