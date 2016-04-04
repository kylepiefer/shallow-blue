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


public class CustomGame extends AppCompatActivity {

    private boolean wking;
    private boolean bking;
    private ImageView selected;
    private ImageView wkingimage;
    private ImageView bkingimage;
    private ImageView currSelected;
    private int[][] boardResources;
    List<ImageView> initialPieces;
    Map<ImageView, Position> imagePositions;
    int whitekingid;
    int blackkingid;
    List<Piece> pieces;
    Map<Piece, String> pieceType;
    Map<Position, Piece> boardSetup;
    Position[][] availPos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        currSelected = (ImageView) findViewById(R.id.currentlyselected);
        initialPieces = new ArrayList<ImageView>();
        pieces = new ArrayList<>();
        pieceType = new HashMap<Piece, String>();
        imagePositions = new HashMap<ImageView, Position>();
        ImageView[][] boardImages = new ImageView[8][8];
        boardImages = setupImageViewArray(boardImages);
        boardSetup = new HashMap<Position, Piece>();
        availPos = new Position[8][8];

        customInitialize();
        imagePositionsInitialize(boardImages);


        wkingimage = (ImageView) findViewById(R.id.wking);

        bkingimage = (ImageView) findViewById(R.id.bking);

        wking = false;
        bking = false;

        whitekingid = R.drawable.white_king;
        blackkingid = R.drawable.black_king;

    }

    private void imagePositionsInitialize(ImageView[][] setup) {
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                Position building = new Position(x,y);
                imagePositions.put(setup[x][y], building);
                availPos[x][y] = building;
            }
        }
    }

    public void customInitialize(){
        ImageView temp1 = (ImageView) findViewById(R.id.wpawn);
        Pawn pawn = new Pawn(new Position(-1,-1), Color.WHITE);
        temp1.setTag(pawn);
        pieces.add(pawn);
        initialPieces.add(temp1);
        pieceType.put(pawn,"wpawn");

        ImageView temp2 = (ImageView) findViewById(R.id.wbishop);
        Bishop bishop = new Bishop(new Position(-1,-1), Color.WHITE);
        temp2.setTag(bishop);
        pieces.add(bishop);
        initialPieces.add(temp2);
        pieceType.put(bishop, "wbishop");

        ImageView temp3 = (ImageView) findViewById(R.id.wknight);
        Knight knight = new Knight(new Position(-1,-1), Color.WHITE);
        temp3.setTag(knight);
        pieces.add(knight);
        initialPieces.add(temp3);
        pieceType.put(knight, "wknight");

        ImageView temp4 = (ImageView) findViewById(R.id.wrook);
        Rook rook = new Rook(new Position(-1,-1), Color.WHITE);
        temp4.setTag(rook);
        pieces.add(rook);
        initialPieces.add(temp4);
        pieceType.put(rook, "wrook");

        ImageView temp5 = (ImageView) findViewById(R.id.wqueen);
        Queen queen = new Queen(new Position(-1,-1), Color.WHITE);
        temp5.setTag(queen);
        pieces.add(queen);
        initialPieces.add(temp5);
        pieceType.put(queen, "wqueen");

        ImageView temp6 = (ImageView) findViewById(R.id.wking);
        King king = new King(new Position(-1,-1), Color.WHITE);
        temp6.setTag(king);
        pieces.add(king);
        initialPieces.add(temp6);
        pieceType.put(king, "wking");

        ImageView temp7 = (ImageView) findViewById(R.id.bpawn);
        Pawn bpawn = new Pawn(new Position(-1,-1), Color.BLACK);
        temp7.setTag(bpawn);
        pieces.add(bpawn);
        initialPieces.add(temp7);
        pieceType.put(bpawn, "bpawn");

        ImageView temp8 = (ImageView) findViewById(R.id.bbishop);
        Bishop bbishop = new Bishop(new Position(-1,-1), Color.BLACK);
        temp8.setTag(bbishop);;
        pieces.add(bbishop);
        initialPieces.add(temp8);
        pieceType.put(bbishop, "bbishop");

        ImageView temp9 = (ImageView) findViewById(R.id.bknight);
        Knight bknight = new Knight(new Position(-1,-1), Color.BLACK);
        temp9.setTag(bknight);
        pieces.add(bknight);
        initialPieces.add(temp9);
        pieceType.put(bknight, "bknight");

        ImageView temp10 = (ImageView) findViewById(R.id.brook);
        Rook brook = new Rook(new Position(-1,-1), Color.BLACK);
        temp10.setTag(brook);
        pieces.add(brook);
        initialPieces.add(temp10);
        pieceType.put(brook, "brook");

        ImageView temp11 = (ImageView) findViewById(R.id.bqueen);
        Queen bqueen = new Queen(new Position(-1,-1), Color.BLACK);
        temp11.setTag(bqueen);
        pieces.add(bqueen);
        initialPieces.add(temp11);
        pieceType.put(bqueen, "bqueen");

        ImageView temp12 = (ImageView) findViewById(R.id.bking);
        King bking = new King(new Position(-1,-1), Color.BLACK);
        temp12.setTag(bking);
        pieces.add(bking);
        initialPieces.add(temp12);
        pieceType.put(bking,"bking");
    }

    public void startCustomGame(View button) {
        if (wking == false || bking == false){
            Toast.makeText(CustomGame.this,"You must have both Kings placed to start a new game.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Intent startGame = new Intent(getApplicationContext(), GameBoardActivity.class);
        Intent startPvpGame = new Intent(getApplicationContext(), PVPGameBoard.class);
        Bundle game = new Bundle();
        game.putInt("game",2);

        ImageView[][] gameboard = new ImageView[8][8];
        boardResources = new int[8][8];

        ImageView[][] settingUp = setupImageViewArray(gameboard);
        gameboard = boardPositions(settingUp);

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
        Position newPosition = imagePositions.get(temp);
        Piece newPiece;
        int selindex;
        int tempindex;
        int selid;
        int tempid;



        if (selected == null && temp.getDrawable() == null){
            return;
        }
        if (initialPieces.contains(temp) && selected != null){
            currSelected.setImageResource(0);

            selindex = pieces.indexOf(selected.getTag());
            selid = pieces.get(selindex).getDrawableId();
            tempindex = pieces.indexOf(temp.getTag());
            tempid = pieces.get(tempindex).getDrawableId();

            if (selid == whitekingid){
                wkingimage.setImageResource(whitekingid);
                if (!initialPieces.contains(selected)){
                    Position selPos = imagePositions.get(selected);
                    boardSetup.remove(selPos);
                    selected.setImageResource(0);
                }
                selected = null;
                wking = false;
                return;
            }
            if (selid == blackkingid){
                bkingimage.setImageResource(blackkingid);
                if (!initialPieces.contains(selected)){
                    Position selPos = imagePositions.get(selected);
                    boardSetup.remove(selPos);
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
        if (selected != null && temp.getDrawable() != null){
            selindex = pieces.indexOf(selected.getTag());
            selid = pieces.get(selindex).getDrawableId();
            tempindex = pieces.indexOf(temp.getTag());
            tempid = pieces.get(tempindex).getDrawableId();
            if (selid == whitekingid && tempid == whitekingid){
                selected = null;
                currSelected.setImageResource(0);
                return;
            }
            if (selid == blackkingid && tempid == blackkingid){
                selected = null;
                currSelected.setImageResource(0);
                return;
            }
            if (selid == blackkingid && tempid == whitekingid && initialPieces.contains(selected)){
                wkingimage.setImageResource(whitekingid);
                bkingimage.setImageResource(0);
                temp.setImageResource(selid);
                newPiece = makePiece(pieces.get(selindex));
                newPiece.setPosition(newPosition);
                temp.setTag(newPiece);
                boardSetup.put(newPosition, newPiece);
                if (!initialPieces.contains(selected)){
                    Position selPos = imagePositions.get(selected);
                    boardSetup.remove(selPos);
                    selected.setImageResource(0);
                }
                selected = null;
                currSelected.setImageResource(0);
                wking = false;
                bking = true;
                return;
            }
            if (selid == whitekingid && tempid == blackkingid && initialPieces.contains(selected)){
                bkingimage.setImageResource(blackkingid);
                wkingimage.setImageResource(0);
                temp.setImageResource(selid);
                newPiece = makePiece(pieces.get(selindex));
                newPiece.setPosition(newPosition);
                temp.setTag(newPiece);
                boardSetup.put(newPosition, newPiece);
                if (!initialPieces.contains(selected)){
                    Position selPos = imagePositions.get(selected);
                    boardSetup.remove(selPos);
                    selected.setImageResource(0);
                }
                currSelected.setImageResource(0);
                selected = null;
                bking = false;
                wking = true;
                return;
            }
            if (tempid == whitekingid){
                wkingimage.setImageResource(whitekingid);
                temp.setImageResource(selid);
                newPiece = makePiece(pieces.get(selindex));
                newPiece.setPosition(newPosition);
                temp.setTag(newPiece);
                boardSetup.put(newPosition, newPiece);
                if (!initialPieces.contains(selected)){
                    Position selPos = imagePositions.get(selected);
                    boardSetup.remove(selPos);
                    selected.setImageResource(0);
                }
                selected = null;
                currSelected.setImageResource(0);
                wking = false;
                return;
            }
            if (tempid == blackkingid){
                bkingimage.setImageResource(blackkingid);
                temp.setImageResource(selid);
                newPiece = makePiece(pieces.get(selindex));
                newPiece.setPosition(newPosition);
                temp.setTag(newPiece);
                boardSetup.put(newPosition, newPiece);
                if (!initialPieces.contains(selected)){
                    Position selPos = imagePositions.get(selected);
                    boardSetup.remove(selPos);
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

        if (selected == null){
            selected = temp;
            tempindex = pieces.indexOf(temp.getTag());
            tempid = pieces.get(tempindex).getDrawableId();
            currSelected.setImageResource(tempid);
            currSelected.setTag(temp.getTag());
            return;
        }
        if (selected == wkingimage){
            wking = true;
            selindex = pieces.indexOf(selected.getTag());
            selid = pieces.get(selindex).getDrawableId();
            temp.setImageResource(selid);
            newPiece = makePiece(pieces.get(selindex));
            newPiece.setPosition(newPosition);
            temp.setTag(newPiece);
            boardSetup.put(newPosition, newPiece);
            selected.setImageResource(0);
            selected = null;
            wkingimage.setImageResource(0);
            currSelected.setImageResource(0);
            return;
        }
        if (selected == bkingimage){
            bking = true;
            selindex = pieces.indexOf(selected.getTag());
            selid = pieces.get(selindex).getDrawableId();
            temp.setImageResource(selid);
            newPiece = makePiece(pieces.get(selindex));
            newPiece.setPosition(newPosition);
            temp.setTag(newPiece);
            boardSetup.put(newPosition, newPiece);
            selected.setImageResource(0);
            selected = null;
            bkingimage.setImageResource(0);
            currSelected.setImageResource(0);
            return;
        }

        selindex = pieces.indexOf(selected.getTag());
        selid = pieces.get(selindex).getDrawableId();
        temp.setImageResource(selid);
        newPiece = makePiece(pieces.get(selindex));
        newPiece.setPosition(newPosition);
        temp.setTag(newPiece);
        boardSetup.put(newPosition, newPiece);
        if (initialPieces.contains(selected)){
            selected = null;
        }
        else{
            Position selPos = imagePositions.get(selected);
            boardSetup.remove(selPos);
            selected.setImageResource(0);
            selected = null;
        }
        currSelected.setImageResource(0);
        return;

    }

    private ImageView[][] setupImageViewArray(ImageView[][] setup){

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
        return setup;
    }

    private ImageView[][] boardPositions(ImageView[][] setup){

        Integer drawable;
        int dummy;
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if (setup[x][y].getDrawable() != null){
                    int index = pieces.indexOf(setup[x][y].getTag());
                    boardResources[x][y] = pieces.get(index).getDrawableId();
                }
            }
        }

        return setup;
    }

    private Piece makePiece(Piece piece){
        Piece save;
        String newPiece = pieceType.get(piece);
        switch (newPiece){

            case "wpawn":
                save = new Pawn(new Position(-1,-1), Color.WHITE);
                pieces.add(save);
                pieceType.put(save,"wpawn");
                return save;
            case "wbishop":
                save = new Bishop(new Position(-1,-1), Color.WHITE);
                pieces.add(save);
                pieceType.put(save,"wbishop");
                return save;
            case "wknight":
                save = new Knight(new Position(-1,-1), Color.WHITE);
                pieces.add(save);
                pieceType.put(save,"wknight");
                return save;
            case "wrook":
                save = new Rook(new Position(-1,-1), Color.WHITE);
                pieces.add(save);
                pieceType.put(save,"wrook");
                return save;
            case "wqueen":
                save = new Queen(new Position(-1,-1), Color.WHITE);
                pieces.add(save);
                pieceType.put(save,"wqueen");
                return save;
            case "wking":
                save = new King(new Position(-1,-1), Color.WHITE);
                pieces.add(save);
                pieceType.put(save,"wking");
                return save;
            case "bpawn":
                save = new Pawn(new Position(-1,-1), Color.BLACK);
                pieces.add(save);
                pieceType.put(save,"bpawn");
                return save;
            case "bbishop":
                save = new Bishop(new Position(-1,-1), Color.BLACK);
                pieces.add(save);
                pieceType.put(save,"bbishop");
                return save;
            case "bknight":
                save = new Knight(new Position(-1,-1), Color.BLACK);
                pieces.add(save);
                pieceType.put(save,"bknight");
                return save;
            case "brook":
                save = new Rook(new Position(-1,-1), Color.BLACK);
                pieces.add(save);
                pieceType.put(save,"brook");
                return save;
            case "bqueen":
                save = new Queen(new Position(-1,-1), Color.BLACK);
                pieces.add(save);
                pieceType.put(save,"bqueen");
                return save;
            case "bking":
                save = new King(new Position(-1,-1), Color.BLACK);
                pieces.add(save);
                pieceType.put(save,"bking");
                return save;
            default:
                break;
        }
        return piece;
    }
}
