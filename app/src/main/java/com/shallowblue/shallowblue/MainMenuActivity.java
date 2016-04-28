package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainMenuActivity extends AppCompatActivity {

    private int taps = 0;
    private Toast toast = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        taps = 0;
    }

    public void startMultiplayerGame(View button) {
        Intent startGameIntent = new Intent(getApplicationContext(), MultiplayerSettings.class);
        startActivity(startGameIntent);
    }

    public void startSingleplayerGame(View button) {
        Intent startGameIntent = new Intent(getApplicationContext(), StartGameActivity.class);
        if (taps >= 7) startGameIntent.putExtra("Game Mode", "CVC");
        startActivity(startGameIntent);
    }

    public void tapLogo(View logo) {
        this.taps = this.taps + 1;
        if (this.taps == 4) showToast("Tap 3 more times to unlock the secret mode!");
        else if (this.taps == 5) showToast("Tap 2 more times to unlock the secret mode!");
        else if (this.taps == 6) showToast("Tap 1 more time to unlock the secret mode!");
        else if (this.taps >= 7) {
            showToast("You have unlocked the secret mode!");
            startSingleplayerGame(logo);
        }
    }

    private void showToast(String message) {
        if (this.toast == null) this.toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        CharSequence text = (CharSequence)message;
        this.toast.setText(text);
        this.toast.setGravity(Gravity.BOTTOM, 0, 0);
        this.toast.setDuration(Toast.LENGTH_SHORT);
        TextView view = (TextView) this.toast.getView().findViewById(android.R.id.message);
        if (view != null) view.setGravity(Gravity.CENTER);
        toast.show();
    }
}
