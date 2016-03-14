package com.shallowblue.shallowblue;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SingleplayerSettings extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singleplayer_settings);
    }
    public void newgamestart(View singleplayer) {
        Intent multiplayersettings = new Intent(getApplicationContext(), MultiplayerSettings.class);
        Intent startMultiplayer = new Intent(getApplicationContext(), PVPGameBoard.class);
        Intent startCustom = new Intent(getApplicationContext(), CustomGameBoard.class);
        Intent loadGame = new Intent(getApplicationContext(), loadgame.class);


        String gametype;

        radioGroup = (RadioGroup) findViewById(R.id.gameselection);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        gametype = radioButton.getText().toString();
        if (gametype.equals("New Game")){
            startActivity(startMultiplayer);
        }
        if (gametype.equals("Custom Game")){
            startActivity(startCustom);
        }
        if (gametype.equals("Load Game")){
            startActivity(loadGame);
        }
        startActivity(multiplayersettings);
    }
}
