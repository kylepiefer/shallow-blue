package com.shallowblue.shallowblue;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class GameBoardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    public void startinghelper(View v){

    }

    public void altmove(View v){

    }

    public void undomove(View v){

    }

    public void redomove(View v){

    }

    public void optionsScreen(View v){

    }
}
