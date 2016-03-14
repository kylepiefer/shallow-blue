package com.shallowblue.shallowblue;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MainMenu extends AppCompatActivity {




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        Configuration configInfo = getResources().getConfiguration();
        fragmentTransaction.commit();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
    }
    public void multiplayerSettings(View multiplayer) {
        Intent startMultiplayer = new Intent(getApplicationContext(), MultiplayerSettings.class);
        startActivity(startMultiplayer);
    }
    public void singleplayerSettings(View singleplayer) {
        Intent startSingleplayer = new Intent(getApplicationContext(), SingleplayerSettings.class);
        startActivity(startSingleplayer);
    }
    public void optionsSettings(View options) {
        Intent options_settings = new Intent(getApplicationContext(), Options_Settings_MAIN.class);
        startActivity(options_settings);
    }

}
