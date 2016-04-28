package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MultiplayerSettings extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiplayer_settings);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void PVPGameStart(View button) {
        Intent startGameIntent = new Intent(getApplicationContext(), PVPGameBoard.class);
        Bundle game = new Bundle();
        game.putInt("game", 1);
        startGameIntent.putExtra("start", game);
        startActivity(startGameIntent);
    }
    public void loadinggame(View button){
        Intent loadgameIntent = new Intent(getApplicationContext(),LoadGameActivity.class);
        Bundle game = new Bundle();
        game.putInt("game", 3);
        loadgameIntent.putExtra("start", game);
        startActivity(loadgameIntent);
    }
    public void creatinggame(View button){
        Intent creatinggame = new Intent(getApplicationContext(), CustomGame.class);
        Bundle playercount = new Bundle();
        playercount.putInt("players", 2);
        creatinggame.putExtra("Type",playercount);
        startActivity(creatinggame);
    }

    public void onBackPressed(){
        finish();
    }
}
