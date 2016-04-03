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
    private int[][] boardResources;
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

        ImageView[][] gameboard = new ImageView[8][8];
        boardResources = new int[8][8];

        gameboard = boardPositions(gameboard);

        PVPGameBoard.customGameBoard = gameboard;
        PVPGameBoard.customBoardResources = boardResources;

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

    private int getDrawableId(ImageView iv){
        return (Integer) iv.getTag();
    }

    private ImageView[][] boardPositions(ImageView[][] setup) throws NullPointerException{

        setup[0][0] = (ImageView) findViewById(R.id.custom00);
        setup[0][1] = (ImageView) findViewById(R.id.custom01);
        setup[0][2] = (ImageView) findViewById(R.id.custom02);
        setup[0][3] = (ImageView) findViewById(R.id.custom03);
        setup[0][4] = (ImageView) findViewById(R.id.custom04);
        setup[0][5] = (ImageView) findViewById(R.id.custom05);
        setup[0][6] = (ImageView) findViewById(R.id.custom06);
        setup[0][7] = (ImageView) findViewById(R.id.custom07);

        setup[1][0] = (ImageView) findViewById(R.id.custom10);
        setup[1][1] = (ImageView) findViewById(R.id.custom11);
        setup[1][2] = (ImageView) findViewById(R.id.custom12);
        setup[1][3] = (ImageView) findViewById(R.id.custom13);
        setup[1][4] = (ImageView) findViewById(R.id.custom14);
        setup[1][5] = (ImageView) findViewById(R.id.custom15);
        setup[1][6] = (ImageView) findViewById(R.id.custom16);
        setup[1][7] = (ImageView) findViewById(R.id.custom17);

        setup[2][0] = (ImageView) findViewById(R.id.custom20);
        setup[2][1] = (ImageView) findViewById(R.id.custom21);
        setup[2][2] = (ImageView) findViewById(R.id.custom22);
        setup[2][3] = (ImageView) findViewById(R.id.custom23);
        setup[2][4] = (ImageView) findViewById(R.id.custom24);
        setup[2][5] = (ImageView) findViewById(R.id.custom25);
        setup[2][6] = (ImageView) findViewById(R.id.custom26);
        setup[2][7] = (ImageView) findViewById(R.id.custom27);

        setup[3][0] = (ImageView) findViewById(R.id.custom30);
        setup[3][1] = (ImageView) findViewById(R.id.custom31);
        setup[3][2] = (ImageView) findViewById(R.id.custom32);
        setup[3][3] = (ImageView) findViewById(R.id.custom33);
        setup[3][4] = (ImageView) findViewById(R.id.custom34);
        setup[3][5] = (ImageView) findViewById(R.id.custom35);
        setup[3][6] = (ImageView) findViewById(R.id.custom36);
        setup[3][7] = (ImageView) findViewById(R.id.custom37);

        setup[4][0] = (ImageView) findViewById(R.id.custom40);
        setup[4][1] = (ImageView) findViewById(R.id.custom41);
        setup[4][2] = (ImageView) findViewById(R.id.custom42);
        setup[4][3] = (ImageView) findViewById(R.id.custom43);
        setup[4][4] = (ImageView) findViewById(R.id.custom44);
        setup[4][5] = (ImageView) findViewById(R.id.custom45);
        setup[4][6] = (ImageView) findViewById(R.id.custom46);
        setup[4][7] = (ImageView) findViewById(R.id.custom47);

        setup[5][0] = (ImageView) findViewById(R.id.custom50);
        setup[5][1] = (ImageView) findViewById(R.id.custom51);
        setup[5][2] = (ImageView) findViewById(R.id.custom52);
        setup[5][3] = (ImageView) findViewById(R.id.custom53);
        setup[5][4] = (ImageView) findViewById(R.id.custom54);
        setup[5][5] = (ImageView) findViewById(R.id.custom55);
        setup[5][6] = (ImageView) findViewById(R.id.custom56);
        setup[5][7] = (ImageView) findViewById(R.id.custom57);

        setup[6][0] = (ImageView) findViewById(R.id.custom60);
        setup[6][1] = (ImageView) findViewById(R.id.custom61);
        setup[6][2] = (ImageView) findViewById(R.id.custom62);
        setup[6][3] = (ImageView) findViewById(R.id.custom63);
        setup[6][4] = (ImageView) findViewById(R.id.custom64);
        setup[6][5] = (ImageView) findViewById(R.id.custom65);
        setup[6][6] = (ImageView) findViewById(R.id.custom66);
        setup[6][7] = (ImageView) findViewById(R.id.custom67);

        setup[7][0] = (ImageView) findViewById(R.id.custom70);
        setup[7][1] = (ImageView) findViewById(R.id.custom71);
        setup[7][2] = (ImageView) findViewById(R.id.custom72);
        setup[7][3] = (ImageView) findViewById(R.id.custom73);
        setup[7][4] = (ImageView) findViewById(R.id.custom74);
        setup[7][5] = (ImageView) findViewById(R.id.custom75);
        setup[7][6] = (ImageView) findViewById(R.id.custom76);
        setup[7][7] = (ImageView) findViewById(R.id.custom77);

        Integer drawable;
        int dummy;
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if (setup[x][y].getDrawable() != null){
                    drawable = getDrawableId(setup[x][y]);
                    boardResources[x][y] = drawable;
                }
            }
        }

        return setup;
    }
}
