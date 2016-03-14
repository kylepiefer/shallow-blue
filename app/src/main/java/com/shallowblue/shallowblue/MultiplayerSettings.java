package com.shallowblue.shallowblue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MultiplayerSettings extends AppCompatActivity {

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_settings);
    }
    public void newgamestart(View multiplayer) {
        Intent multiplayersettings = new Intent(getApplicationContext(), MultiplayerSettings.class);
        Intent startMultiplayer = new Intent(getApplicationContext(), PVPGameBoard.class);
        Intent startCustom = new Intent(getApplicationContext(), CustomGameBoard.class);
        Intent loadGame = new Intent(getApplicationContext(), loadgame.class);


        String gametype;

        radioGroup = (RadioGroup) findViewById(R.id.gameselection);
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);
        //String buttonsid;
        //buttonsid = radioButton.get
        gametype = radioButton.getText().toString();
        char check = gametype.charAt(0);
        if (check == 'N'){
            startActivity(startMultiplayer);
        }
        else if (check == 'C'){
            startActivity(startCustom);
        }
        else if (check == 'L'){
            startActivity(loadGame);
        }
        else {
            startActivity(multiplayersettings);
        }
    }

}
