package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class CustomGame extends AppCompatActivity {

    private boolean wking;
    private boolean bking;
    private ImageView selected;
    private ImageView wkingimage;
    private ImageView bkingimage;
    private ImageView currSelected;
    List<ImageView> initialPieces;
    int whitekingid;
    int blackkingid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currSelected = (ImageView) findViewById(R.id.currentlyselected);
        initialPieces = new ArrayList<ImageView>();
        customInitialize();

        wkingimage = (ImageView) findViewById(R.id.wking);
        wkingimage.setTag(R.drawable.white_king);
        bkingimage = (ImageView) findViewById(R.id.bking);
        bkingimage.setTag(R.drawable.black_king);

        wking = false;
        bking = false;

        whitekingid = R.drawable.white_king;
        blackkingid = R.drawable.black_king;

    }

    public void customInitialize(){
        ImageView temp1 = (ImageView) findViewById(R.id.wpawn);
        temp1.setTag(R.drawable.white_pawn);
        initialPieces.add(temp1);
        ImageView temp2 = (ImageView) findViewById(R.id.wbishop);
        temp2.setTag(R.drawable.white_bishop);
        initialPieces.add(temp2);
        ImageView temp3 = (ImageView) findViewById(R.id.wknight);
        temp3.setTag(R.drawable.white_knight);
        initialPieces.add(temp3);
        ImageView temp4 = (ImageView) findViewById(R.id.wrook);
        temp4.setTag(R.drawable.white_rook);
        initialPieces.add(temp4);
        ImageView temp5 = (ImageView) findViewById(R.id.wqueen);
        temp5.setTag(R.drawable.white_queen);
        initialPieces.add(temp5);
        ImageView temp6 = (ImageView) findViewById(R.id.wking);
        temp6.setTag(R.drawable.white_king);
        initialPieces.add(temp6);
        ImageView temp7 = (ImageView) findViewById(R.id.bpawn);
        temp7.setTag(R.drawable.black_pawn);
        initialPieces.add(temp7);
        ImageView temp8 = (ImageView) findViewById(R.id.bbishop);
        temp8.setTag(R.drawable.black_bishop);
        initialPieces.add(temp8);
        ImageView temp9 = (ImageView) findViewById(R.id.bknight);
        temp9.setTag(R.drawable.black_knight);
        initialPieces.add(temp9);
        ImageView temp10 = (ImageView) findViewById(R.id.brook);
        temp10.setTag(R.drawable.black_rook);
        initialPieces.add(temp10);
        ImageView temp11 = (ImageView) findViewById(R.id.bqueen);
        temp11.setTag(R.drawable.black_queen);
        initialPieces.add(temp11);
        ImageView temp12 = (ImageView) findViewById(R.id.bking);
        temp12.setTag(R.drawable.black_king);
        initialPieces.add(temp12);
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

        //Initialize that they kings haven't been placed yet
    }

    public void cgoptionsScreen(View v){
        Intent openOptions = new Intent(getApplicationContext(),OptionsPopUpWindow.class);
        startActivity(openOptions);
    }

    public void placePiece(View v){
        ImageView temp = (ImageView) v;
        if (selected == null && temp.getDrawable() == null){
            return;
        }
        if (selected != null && temp.getDrawable() != null){
            if (getDrawableId(selected) == whitekingid && getDrawableId(temp) == whitekingid){
                selected = null;
                currSelected.setImageResource(0);
                return;
            }
            if (getDrawableId(selected) == blackkingid && getDrawableId(temp) == blackkingid){
                selected = null;
                currSelected.setImageResource(0);
                return;
            }
            if (getDrawableId(temp) == whitekingid){
                wkingimage.setImageResource(whitekingid);
                int idsave = getDrawableId(selected);
                temp.setImageResource(idsave);
                temp.setTag(idsave);
                if (!initialPieces.contains(selected)){
                    selected.setImageResource(0);
                }
                selected = null;
                currSelected.setImageResource(0);
                wking = false;
                return;
            }
            if (getDrawableId(temp) == blackkingid){
                bkingimage.setImageResource(blackkingid);
                int idsave = getDrawableId(selected);
                temp.setImageResource(idsave);
                temp.setTag(idsave);
                if (!initialPieces.contains(selected)){
                    selected.setImageResource(0);
                }
                currSelected.setImageResource(0);
                selected = null;
                bking = false;
                return;
            }
        }
        if (selected == temp){
            selected = null;
            currSelected.setImageResource(0);
            return;
        }
        if (initialPieces.contains(temp) && selected != null){
            currSelected.setImageResource(0);

            if (getDrawableId(selected) == whitekingid){
                wkingimage.setImageResource(whitekingid);
                if (!initialPieces.contains(selected)){
                    selected.setImageResource(0);
                }
                selected = null;
                wking = false;
                return;
            }
            if (getDrawableId(selected) == blackkingid){
                bkingimage.setImageResource(blackkingid);
                if (!initialPieces.contains(selected)){
                    selected.setImageResource(0);
                }
                selected = null;
                bking = false;
                return;
            }
            if (initialPieces.contains(selected)){
                selected = null;
                return;
            }
            selected.setImageResource(0);
            selected = null;
            return;
        }
        if (selected == null){
            selected = temp;
            int idsave = getDrawableId(temp);
            currSelected.setImageResource(idsave);
            currSelected.setTag(temp.getDrawable());
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
        if (initialPieces.contains(selected)){
            selected = null;
        }
        else{
            selected.setImageResource(0);
            selected = null;
        }
        currSelected.setImageResource(0);
        return;

    }

    private int getDrawableId(ImageView iv) {
        return (Integer) iv.getTag();
    }
}
