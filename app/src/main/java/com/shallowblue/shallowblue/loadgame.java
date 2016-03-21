package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class loadgame extends AppCompatActivity {

    public Intent nextActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadgame);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent check = getIntent();
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
    }
    public void loadSavedGame(View button) {
        startActivity(nextActivity);
    }

    public void onBackPressed(){
        finish();
    }
}
