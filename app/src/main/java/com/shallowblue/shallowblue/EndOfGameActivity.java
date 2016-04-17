package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

public class EndOfGameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end_of_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.2));

    }

    public void undoLastMove(View v){
        Intent result = new Intent();
        result.putExtra("Action", "Undo");
        setResult(RESULT_OK, result);
        finish();
    }

    public void mainMenu(View v){
        Intent result = new Intent();
        result.putExtra("Action", "Quit");
        setResult(RESULT_OK, result);
        finish();
    }
}
