package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class PVPGameBoard extends AppCompatActivity {

    private ImageView pieceSelected;
    public static ImageView[][] customGameBoard;
    public static int[][] customBoardResources;
    public ImageView[][] pvpGameboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvpgame_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent gather = getIntent();
        Bundle temp = new Bundle();
        temp = gather.getBundleExtra("start");
        int count = temp.getInt("game");

        pvpGameboard = new ImageView[8][8];

        initializeBoard();

        if (count == 1){
            startNewPvpGame();
        }
        if (count == 2){
            addCustomSetup();
        }



    }

    public void startNewPvpGame(){
        for (int y = 0; y < 8; y++){
            pvpGameboard[1][y].setImageResource(R.drawable.black_pawn_flipped);
            pvpGameboard[1][y].setTag(R.drawable.black_pawn_flipped);

            pvpGameboard[6][y].setImageResource(R.drawable.white_pawn);
            pvpGameboard[6][y].setTag(R.drawable.white_pawn);
        }

        pvpGameboard[0][0].setImageResource(R.drawable.black_rook_flipped);
        pvpGameboard[0][7].setImageResource(R.drawable.black_rook_flipped);
        pvpGameboard[7][0].setImageResource(R.drawable.white_rook);
        pvpGameboard[7][7].setImageResource(R.drawable.white_rook);

        pvpGameboard[0][1].setImageResource(R.drawable.black_knight_flipped);
        pvpGameboard[0][6].setImageResource(R.drawable.black_knight_flipped);
        pvpGameboard[7][1].setImageResource(R.drawable.white_knight);
        pvpGameboard[7][6].setImageResource(R.drawable.white_knight);

        pvpGameboard[0][2].setImageResource(R.drawable.black_bishop_flipped);
        pvpGameboard[0][5].setImageResource(R.drawable.black_bishop_flipped);
        pvpGameboard[7][2].setImageResource(R.drawable.white_bishop);
        pvpGameboard[7][5].setImageResource(R.drawable.white_bishop);

        pvpGameboard[0][3].setImageResource(R.drawable.black_queen_flipped);
        pvpGameboard[7][3].setImageResource(R.drawable.white_queen);

        pvpGameboard[0][4].setImageResource(R.drawable.black_king_flipped);
        pvpGameboard[7][4].setImageResource(R.drawable.white_king);



        pvpGameboard[0][0].setTag(R.drawable.black_rook_flipped);
        pvpGameboard[0][7].setTag(R.drawable.black_rook_flipped);
        pvpGameboard[7][0].setTag(R.drawable.white_rook);
        pvpGameboard[7][7].setTag(R.drawable.white_rook);

        pvpGameboard[0][1].setTag(R.drawable.black_knight_flipped);
        pvpGameboard[0][6].setTag(R.drawable.black_knight_flipped);
        pvpGameboard[7][1].setTag(R.drawable.white_knight);
        pvpGameboard[7][6].setTag(R.drawable.white_knight);

        pvpGameboard[0][2].setTag(R.drawable.black_bishop_flipped);
        pvpGameboard[0][5].setTag(R.drawable.black_bishop_flipped);
        pvpGameboard[7][2].setTag(R.drawable.white_bishop);
        pvpGameboard[7][5].setTag(R.drawable.white_bishop);

        pvpGameboard[0][3].setTag(R.drawable.black_queen_flipped);
        pvpGameboard[7][3].setTag(R.drawable.white_queen);

        pvpGameboard[0][4].setTag(R.drawable.black_king_flipped);
        pvpGameboard[7][4].setTag(R.drawable.white_king);
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

    public void pvpoptionsScreen1(View v){
        Intent openOptions = new Intent(getApplicationContext(),OptionsPopUpWindow.class);
        startActivity(openOptions);
    }

    public void pvpoptionsScreen2(View v){
        Intent openOptions = new Intent(getApplicationContext(),OptionsPopUpWindow.class);
        startActivity(openOptions);
    }

    public void pvpundo1(View v){

    }

    public void pvpundo2(View v){

    }

    public void pvpredo1(View v){

    }

    public void pvpredo2(View v){

    }

    public void pvpsuggalt1(View v){

    }

    public void pvpsuggalt2(View v){

    }

    public void pvpstarthelp1(View v){

    }

    public void pvpstarthelp2(View v){

    }

    public void onBackPressed(){
        Intent check = new Intent(getApplicationContext(),LeaveGameVerify.class);
        Bundle verify = new Bundle();
        verify.putString("activity","main");
        check.putExtra("next",verify);
        startActivity(check);
    }

    public void initializeBoard(){
        pvpGameboard[0][0] = (ImageView) findViewById(R.id.pvpgame00);
        pvpGameboard[0][1] = (ImageView) findViewById(R.id.pvpgame01);
        pvpGameboard[0][2] = (ImageView) findViewById(R.id.pvpgame02);
        pvpGameboard[0][3] = (ImageView) findViewById(R.id.pvpgame03);
        pvpGameboard[0][4] = (ImageView) findViewById(R.id.pvpgame04);
        pvpGameboard[0][5] = (ImageView) findViewById(R.id.pvpgame05);
        pvpGameboard[0][6] = (ImageView) findViewById(R.id.pvpgame06);
        pvpGameboard[0][7] = (ImageView) findViewById(R.id.pvpgame07);

        pvpGameboard[1][0] = (ImageView) findViewById(R.id.pvpgame10);
        pvpGameboard[1][1] = (ImageView) findViewById(R.id.pvpgame11);
        pvpGameboard[1][2] = (ImageView) findViewById(R.id.pvpgame12);
        pvpGameboard[1][3] = (ImageView) findViewById(R.id.pvpgame13);
        pvpGameboard[1][4] = (ImageView) findViewById(R.id.pvpgame14);
        pvpGameboard[1][5] = (ImageView) findViewById(R.id.pvpgame15);
        pvpGameboard[1][6] = (ImageView) findViewById(R.id.pvpgame16);
        pvpGameboard[1][7] = (ImageView) findViewById(R.id.pvpgame17);

        pvpGameboard[2][0] = (ImageView) findViewById(R.id.pvpgame20);
        pvpGameboard[2][1] = (ImageView) findViewById(R.id.pvpgame21);
        pvpGameboard[2][2] = (ImageView) findViewById(R.id.pvpgame22);
        pvpGameboard[2][3] = (ImageView) findViewById(R.id.pvpgame23);
        pvpGameboard[2][4] = (ImageView) findViewById(R.id.pvpgame24);
        pvpGameboard[2][5] = (ImageView) findViewById(R.id.pvpgame25);
        pvpGameboard[2][6] = (ImageView) findViewById(R.id.pvpgame26);
        pvpGameboard[2][7] = (ImageView) findViewById(R.id.pvpgame27);

        pvpGameboard[3][0] = (ImageView) findViewById(R.id.pvpgame30);
        pvpGameboard[3][1] = (ImageView) findViewById(R.id.pvpgame31);
        pvpGameboard[3][2] = (ImageView) findViewById(R.id.pvpgame32);
        pvpGameboard[3][3] = (ImageView) findViewById(R.id.pvpgame33);
        pvpGameboard[3][4] = (ImageView) findViewById(R.id.pvpgame34);
        pvpGameboard[3][5] = (ImageView) findViewById(R.id.pvpgame35);
        pvpGameboard[3][6] = (ImageView) findViewById(R.id.pvpgame36);
        pvpGameboard[3][7] = (ImageView) findViewById(R.id.pvpgame37);

        pvpGameboard[4][0] = (ImageView) findViewById(R.id.pvpgame40);
        pvpGameboard[4][1] = (ImageView) findViewById(R.id.pvpgame41);
        pvpGameboard[4][2] = (ImageView) findViewById(R.id.pvpgame42);
        pvpGameboard[4][3] = (ImageView) findViewById(R.id.pvpgame43);
        pvpGameboard[4][4] = (ImageView) findViewById(R.id.pvpgame44);
        pvpGameboard[4][5] = (ImageView) findViewById(R.id.pvpgame45);
        pvpGameboard[4][6] = (ImageView) findViewById(R.id.pvpgame46);
        pvpGameboard[4][7] = (ImageView) findViewById(R.id.pvpgame47);

        pvpGameboard[5][0] = (ImageView) findViewById(R.id.pvpgame50);
        pvpGameboard[5][1] = (ImageView) findViewById(R.id.pvpgame51);
        pvpGameboard[5][2] = (ImageView) findViewById(R.id.pvpgame52);
        pvpGameboard[5][3] = (ImageView) findViewById(R.id.pvpgame53);
        pvpGameboard[5][4] = (ImageView) findViewById(R.id.pvpgame54);
        pvpGameboard[5][5] = (ImageView) findViewById(R.id.pvpgame55);
        pvpGameboard[5][6] = (ImageView) findViewById(R.id.pvpgame56);
        pvpGameboard[5][7] = (ImageView) findViewById(R.id.pvpgame57);

        pvpGameboard[6][0] = (ImageView) findViewById(R.id.pvpgame60);
        pvpGameboard[6][1] = (ImageView) findViewById(R.id.pvpgame61);
        pvpGameboard[6][2] = (ImageView) findViewById(R.id.pvpgame62);
        pvpGameboard[6][3] = (ImageView) findViewById(R.id.pvpgame63);
        pvpGameboard[6][4] = (ImageView) findViewById(R.id.pvpgame64);
        pvpGameboard[6][5] = (ImageView) findViewById(R.id.pvpgame65);
        pvpGameboard[6][6] = (ImageView) findViewById(R.id.pvpgame66);
        pvpGameboard[6][7] = (ImageView) findViewById(R.id.pvpgame67);

        pvpGameboard[7][0] = (ImageView) findViewById(R.id.pvpgame70);
        pvpGameboard[7][1] = (ImageView) findViewById(R.id.pvpgame71);
        pvpGameboard[7][2] = (ImageView) findViewById(R.id.pvpgame72);
        pvpGameboard[7][3] = (ImageView) findViewById(R.id.pvpgame73);
        pvpGameboard[7][4] = (ImageView) findViewById(R.id.pvpgame74);
        pvpGameboard[7][5] = (ImageView) findViewById(R.id.pvpgame75);
        pvpGameboard[7][6] = (ImageView) findViewById(R.id.pvpgame76);
        pvpGameboard[7][7] = (ImageView) findViewById(R.id.pvpgame77);
    }

    public void addCustomSetup(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                pvpGameboard[x][y].setImageResource(customBoardResources[x][y]);
                pvpGameboard[x][y].setTag(customBoardResources[x][y]);
            }
        }
    }
}
