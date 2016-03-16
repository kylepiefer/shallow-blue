package com.shallowblue.shallowblue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
    }

    public void startMultiplayerGame(View button) {
        Intent startGameIntent = new Intent(getApplicationContext(), MultiplayerSettings.class);
        startActivity(startGameIntent);
    }

    public void startSingleplayerGame(View button) {
        Intent startGameIntent = new Intent(getApplicationContext(), StartGameActivity.class);
        startActivity(startGameIntent);
    }

}
