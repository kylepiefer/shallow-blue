package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class LoadGameActivity extends AppCompatActivity {

    private String gameMode;
    private SavedGameManager savedGameManager;
    public Intent nextActivity;
    private LoadGameActivity loadGameActivity;
    private Bundle settings;
    private ArrayAdapter<String> adapter;
    private Toast toast;
    private boolean selected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.savedGameManager = new SavedGameManager();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        updateAdapter();

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

    private void updateAdapter() {
        adapter.clear();
        List<String> savedGameFileNames = this.savedGameManager.getSavedGameFileNames(this);
        adapter.addAll(savedGameFileNames);
        selected = false;
    }

    private void showToast(String message) {
        if (this.toast == null) this.toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        CharSequence text = (CharSequence)message;
        this.toast.setText(text);
        this.toast.setGravity(Gravity.BOTTOM, 0, 0);
        this.toast.setDuration(Toast.LENGTH_SHORT);
        TextView view = (TextView) this.toast.getView().findViewById(android.R.id.message);
        if (view != null) view.setGravity(Gravity.CENTER);
        toast.show();
    }

    public void startGame() {
        if (adapter.getCount() == 0) {
            showToast("You have not saved any games.");
            return;
        } if (!selected) {
            showToast("You must first select a saved game to load!");
            return;
        }

        String fileName = adapter.getItem(0);
        boolean result = savedGameManager.loadGame(loadGameActivity, fileName);
        if (!result) {
            showToast("Could not load that saved game. Please try again.");
            return;
        }

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
        startGame();
    }

    public void onBackPressed(){
        if (selected) {
            updateAdapter();
            return;
        }
        finish();
    }

    public void deleteSavedGame(View button) {
        if (adapter.getCount() == 0) {
            showToast("You have not saved any games.");
            return;
        } else if (!selected) {
            showToast("You must first select a saved game to delete!");
            return;
        }

        String fileName = adapter.getItem(0);
        boolean result = savedGameManager.removeSavedGame(this, fileName);
        if (!result) {
            showToast("Could not delete that saved game. Please try again.");
            return;
        }

        updateAdapter();
    }

    private AdapterView.OnItemClickListener fileNameClickedHandler = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String fileName = (String) parent.getItemAtPosition(position);
            selected = true;
            adapter.clear();
            adapter.add(fileName);
        }
    };
}
