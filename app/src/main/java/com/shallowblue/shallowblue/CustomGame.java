package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class CustomGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void startCustomGame(View button) {
        Intent startGame = new Intent(getApplicationContext(), GameBoardActivity.class);
        Intent startPvpGame = new Intent(getApplicationContext(), PVPGameBoard.class);
        Intent gametype = getIntent();
        Bundle temp = new Bundle();
        temp = gametype.getBundleExtra("type");
        int count = temp.getInt("players");
        if (count == 1){
            startActivity(startGame);
        }
        if (count == 2){
            startActivity(startPvpGame);
        }

    }
    public void customOptions(View button){

    }
}
