package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PVPGameBoard extends AppCompatActivity {

    private ImageView pieceSelected;
    public static ImageView[][] customGameBoard;
    public static int[][] customBoardResources;
    public ImageView[][] pvpGameboard;
    public static Map<Position,Piece> boardSetup;
    public static Position[][] availPos;
    public Map<ImageView, Position> imagePositions;
    public static List<Piece> blackPieces;
    public static List<Piece> whitePieces;
    public ImageView selImage;
    public Piece selPiece;
    public Color turn;
    public Position selPosition;
    public List<Position> selMoves;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pvpgame_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent gather = getIntent();
        Bundle temp = new Bundle();
        temp = gather.getBundleExtra("start");
        int count = temp.getInt("game");
        imagePositions = new HashMap<ImageView, Position>();
        boardSetup = new HashMap<Position, Piece>();
        pvpGameboard = new ImageView[8][8];
        selMoves = new ArrayList<>();
        turn = Color.WHITE;

        if (count == 1){
            availPos = new Position[8][8];
            createPositionArray();
            initializeBoard();
            startNewPvpGame();
        }

        if (count == 2){
            initializeBoard();
            addCustomSetup();
        }

    }

    private void createPositionArray() {
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                availPos[x][y] = new Position(x, y);
            }
        }
    }

    public void startNewPvpGame(){
        for (int y = 0; y < 8; y++){
            Pawn start = new Pawn(availPos[1][y], Color.BLACK);
            flipBlackPieces(start);
            pvpGameboard[1][y].setImageResource(start.getDrawableId());
            boardSetup.put(availPos[1][y], start);

            Pawn begin = new Pawn(availPos[6][y], Color.WHITE);
            pvpGameboard[6][y].setImageResource(begin.getDrawableId());
            boardSetup.put(availPos[6][y], begin);
        }

        Rook setup = new Rook(availPos[0][0], Color.BLACK);
        flipBlackPieces(setup);
        pvpGameboard[0][0].setImageResource(setup.getDrawableId());
        Rook setup1 = new Rook(availPos[0][7], Color.BLACK);
        flipBlackPieces(setup1);
        pvpGameboard[0][7].setImageResource(setup1.getDrawableId());
        Rook setup2 = new Rook(availPos[7][0], Color.WHITE);
        pvpGameboard[7][0].setImageResource(setup2.getDrawableId());
        Rook setup3 = new Rook(availPos[7][7], Color.WHITE);
        pvpGameboard[7][7].setImageResource(setup3.getDrawableId());

        boardSetup.put(availPos[0][0], setup);
        boardSetup.put(availPos[0][7], setup1);
        boardSetup.put(availPos[7][0], setup2);
        boardSetup.put(availPos[7][7], setup3);

        Knight setup4 = new Knight(availPos[0][1], Color.BLACK);
        flipBlackPieces(setup4);
        pvpGameboard[0][1].setImageResource(setup4.getDrawableId());
        Knight setup5 = new Knight(availPos[0][6], Color.BLACK);
        flipBlackPieces(setup5);
        pvpGameboard[0][6].setImageResource(setup5.getDrawableId());
        Knight setup6 = new Knight(availPos[7][1], Color.WHITE);
        pvpGameboard[7][1].setImageResource(setup6.getDrawableId());
        Knight setup7 = new Knight(availPos[7][6], Color.WHITE);
        pvpGameboard[7][6].setImageResource(setup7.getDrawableId());

        boardSetup.put(availPos[0][1], setup4);
        boardSetup.put(availPos[0][6], setup5);
        boardSetup.put(availPos[7][1], setup6);
        boardSetup.put(availPos[7][6], setup7);

        Bishop setup8 = new Bishop(availPos[0][2], Color.BLACK);
        flipBlackPieces(setup8);
        pvpGameboard[0][2].setImageResource(setup8.getDrawableId());
        Bishop setup9 = new Bishop(availPos[0][5], Color.BLACK);
        flipBlackPieces(setup9);
        pvpGameboard[0][5].setImageResource(setup9.getDrawableId());
        Bishop setup10 = new Bishop(availPos[7][2], Color.WHITE);
        pvpGameboard[7][2].setImageResource(setup10.getDrawableId());
        Bishop setup11 = new Bishop(availPos[7][5], Color.WHITE);
        pvpGameboard[7][5].setImageResource(setup11.getDrawableId());

        boardSetup.put(availPos[0][2], setup8);
        boardSetup.put(availPos[0][5], setup9);
        boardSetup.put(availPos[7][2], setup10);
        boardSetup.put(availPos[7][5], setup11);

        Queen setup12 = new Queen(availPos[0][3], Color.BLACK);
        flipBlackPieces(setup12);
        pvpGameboard[0][3].setImageResource(setup12.getDrawableId());
        Queen setup13 = new Queen(availPos[7][3], Color.WHITE);
        pvpGameboard[7][3].setImageResource(setup13.getDrawableId());

        boardSetup.put(availPos[0][3], setup12);
        boardSetup.put(availPos[7][3], setup13);

        King setup14 = new King(availPos[0][4], Color.BLACK);
        flipBlackPieces(setup14);
        pvpGameboard[0][4].setImageResource(setup14.getDrawableId());
        King setup15 = new King(availPos[7][4], Color.WHITE);
        pvpGameboard[7][4].setImageResource(setup15.getDrawableId());

        boardSetup.put(availPos[0][4], setup14);
        boardSetup.put(availPos[7][4], setup15);

        //This is to set all other positions to null for their Piece value so that they can be
        // called without a NullPointerException
        for (int x = 2; x < 6; x++ ){
            for (int y = 0; y < 8; y++){
                boardSetup.put(availPos[x][y], null);
            }
        }
    }

    public void movePiece(View v){
        ImageView temp = (ImageView) v;
        Position tempPos = imagePositions.get(temp);
        Piece tempPiece = boardSetup.get(tempPos);
        boolean foundMatch = false;
        if (selImage == temp){
            selImage = null;
            return;
        }
        if (selImage == null){
            if (tempPiece == null){
                return;
            } else {
                if (tempPiece.possibleMoves().isEmpty()){
                    Toast.makeText(PVPGameBoard.this, "There are not any moves available for " +
                            "this piece.", Toast.LENGTH_SHORT).show();
                    return;
                }
                selImage = temp;
                selPiece = tempPiece;
                selPosition = tempPos;
                selMoves = selPiece.possibleMoves();
                return;
            }
        } else {
            int tempX = tempPos.getRow();
            int tempY = tempPos.getColumn();
            for (int x = 0; x < selMoves.size(); x++){
                int selX = selMoves.get(x).getRow();
                int selY = selMoves.get(x).getColumn();
                if (selX == tempX && selY == tempY){
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch){
                selImage = null;
                return;
            }
        }
        selPiece.setPosition(tempPos);
        boardSetup.put(tempPos, selPiece);
        boardSetup.put(selPosition, null);
        temp.setImageResource(selPiece.getDrawableId());
        selImage.setImageResource(0);
        selImage = null;
        return;

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

        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                imagePositions.put(pvpGameboard[x][y], availPos[x][y]);

            }
        }
    }

    public void addCustomSetup(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                pvpGameboard[x][y].setImageResource(customBoardResources[x][y]);
                Position saving = imagePositions.get(pvpGameboard[x][y]);
                if (boardSetup.containsKey(saving)){
                    Piece pieceSave = boardSetup.get(saving);
                    pvpGameboard[x][y].setImageResource(pieceSave.getDrawableId());
                    pvpGameboard[x][y].setTag(pieceSave);
                }
            }
        }
    }

    public void flipBlackPieces(Piece checkPiece) {

        String pieceType = checkPiece.toString();
        switch (pieceType) {
            case "p":
                checkPiece.setDrawableId(R.drawable.black_pawn_flipped);
                break;
            case "b":
                checkPiece.setDrawableId(R.drawable.black_bishop_flipped);
                break;
            case "n":
                checkPiece.setDrawableId(R.drawable.black_knight_flipped);
                break;
            case "r":
                checkPiece.setDrawableId(R.drawable.black_rook_flipped);
                break;
            case "q":
                checkPiece.setDrawableId(R.drawable.black_queen_flipped);
                break;
            case "k":
                checkPiece.setDrawableId(R.drawable.black_king_flipped);
                break;
            default:
                break;
        }
    }
}
