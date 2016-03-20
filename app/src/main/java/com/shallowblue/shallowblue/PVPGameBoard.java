package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class PVPGameBoard extends AppCompatActivity {

    private ImageView pieceSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvpgame_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent gather = getIntent();
        Bundle temp = new Bundle();
        temp = gather.getBundleExtra("start");
        int count = temp.getInt("game");
        if (count == 1){
            startNewPvpGame();
        }

    }

    public void startNewPvpGame(){
        ImageView temp = (ImageView) findViewById(R.id.imageView130);
        temp.setImageResource(R.drawable.black_rook_flipped);
        temp.setTag(R.drawable.black_rook_flipped);
    }

    public void movePiece(View v){
        ImageView temp = (ImageView) v;
        if (pieceSelected == null){
            pieceSelected = temp;
            return;
        }
        int idsave = getDrawableId(pieceSelected);
        temp.setImageResource(idsave);
        temp.setTag(idsave);
        pieceSelected.setImageResource(0);
        pieceSelected = null;
    }

    private int getDrawableId(ImageView iv) {
        return (Integer) iv.getTag();
    }
}
