package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class loadgame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loadgame);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void loadSavedGame(View button) {
        Intent loadGameIntent = new Intent(getApplicationContext(), GameBoardActivity.class);
        startActivity(loadGameIntent);
    }

    public void onBackPressed(){
        finish();
    }
}
