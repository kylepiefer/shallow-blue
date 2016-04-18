package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class OptionsPopUpWindow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_pop_up_window);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.7));

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.StartGameActivity_ai_array, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner opponentSpinner = (Spinner) findViewById(R.id.optionsOppAISpinner);
        Spinner suggestionSpinner = (Spinner) findViewById(R.id.optionsSuggAISpinner);
        opponentSpinner.setAdapter(adapter);
        suggestionSpinner.setAdapter(adapter);

    }

    public void opSaveGame(View v){
        Intent openSaveWindow = new Intent(getApplicationContext(), SaveGame.class);
        startActivity(openSaveWindow);
    }

    public void opLoadGame(View v){
        Intent loadcheck = new Intent(getApplicationContext(),LoadGameActivity.class);
        Bundle nextactivity = new Bundle();
        nextactivity.putString("activity","load");
        loadcheck.putExtra("next",nextactivity);
        startActivity(loadcheck);
    }

    public void opEndGame(View v){
        Intent endcheck = new Intent(getApplicationContext(),LeaveGameVerify.class);
        Bundle nextactivity = new Bundle();
        nextactivity.putString("activity","end");
        endcheck.putExtra("next",nextactivity);
        startActivity(endcheck);
    }

    public void opReturn(View v){
        finish();
    }
    public void onBackPressed(){
        finish();
    }
}
