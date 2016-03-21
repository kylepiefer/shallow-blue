package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

public class LeaveGameVerify extends AppCompatActivity {

    public Intent nextactivity = new Intent();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_game_verify);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.2));

        Intent gametype = getIntent();
        Bundle temp = new Bundle();
        temp = gametype.getBundleExtra("next");
        String next = temp.getString("activity");
        if (next.equals("end")){
            nextactivity = new Intent(LeaveGameVerify.this, MainMenuActivity.class);
        }
        if (next.equals("load")){
            Bundle load = new Bundle();
            load.putString("toload","go");
            nextactivity.putExtra("next",load);
            nextactivity = new Intent(LeaveGameVerify.this, GameBoardActivity.class);
        }
        if(next.equals("main")){
            nextactivity = new Intent(LeaveGameVerify.this, MainMenuActivity.class);
        }
    }

    public void verifyleaving(View v){
        nextactivity.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(nextactivity);
        finish();
    }

    public void cancelleaving(View v){
        finish();
    }
}
