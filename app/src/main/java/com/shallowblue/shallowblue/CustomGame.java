package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;


public class CustomGame extends AppCompatActivity {

    private boolean wking;
    private boolean bking;
    private ImageView selected;
    private ImageView wkingimage;
    private ImageView bkingimage;
    private ImageView currSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }
    public void startCustomGame(View button) {
        Intent startGame = new Intent(getApplicationContext(), GameBoardActivity.class);
        Intent startPvpGame = new Intent(getApplicationContext(), PVPGameBoard.class);
        Bundle game = new Bundle();
        game.putInt("game",2);

        Intent gametype = getIntent();
        Bundle temp = new Bundle();
        temp = gametype.getBundleExtra("type");
        int count = temp.getInt("players");
        if (count == 1){
            startGame.putExtra("start",game);
            startActivity(startGame);
        }
        if (count == 2){
            startPvpGame.putExtra("start",game);
            startActivity(startPvpGame);
        }

        //Save the ImageViews of the wking and bking to help know when they are selected.
        wkingimage = (ImageView) findViewById(R.id.white_king);
        bkingimage = (ImageView) findViewById(R.id.black_king);
        currSelected = (ImageView) findViewById(R.id.currentlyselected);

        //Initialize that they kings haven't been placed yet
        wking = false;
        bking = false;

    }

    public void cgoptionsScreen(View v){
        Intent openOptions = new Intent(getApplicationContext(),OptionsPopUpWindow.class);
        startActivity(openOptions);
    }

    public void placePiece(View v){
        ImageView temp = (ImageView) v;

        if (selected == null){
            selected = temp;
            int idsave = getDrawableId(temp);
            currSelected.setImageResource(idsave);
            return;
        }
        if (selected == wkingimage){
            wking = true;
            int idsave = getDrawableId(selected);
            temp.setImageResource(idsave);
            temp.setTag(idsave);
            selected.setImageResource(0);
            selected = null;
            wkingimage.setImageResource(0);
            currSelected.setImageResource(0);
            return;
        }
        if (selected == bkingimage){
            bking = true;
            int idsave = getDrawableId(selected);
            temp.setImageResource(idsave);
            temp.setTag(idsave);
            selected.setImageResource(0);
            selected = null;
            bkingimage.setImageResource(0);
            currSelected.setImageResource(0);
            return;
        }

        int idsave = getDrawableId(selected);
        temp.setImageResource(idsave);
        temp.setTag(idsave);
        selected = null;
        currSelected.setImageResource(0);
        return;

    }

    private int getDrawableId(ImageView iv) {
        return (Integer) iv.getTag();
    }
}
