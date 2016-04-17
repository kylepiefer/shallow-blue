package com.shallowblue.shallowblue;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class SaveGame extends AppCompatActivity {

    EditText saveText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_game);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.8), (int) (height * 0.3));
        saveText = (EditText) findViewById(R.id.savename);
    }

    public void sgSaveClicked (View v){
        String filename = saveText.getText().toString();
        File path = getFilesDir();
        if (filename.length() < 5){
            Toast.makeText(SaveGame.this, "Please enter a name more than 5 characters.",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        File[] allfiles = path.listFiles();
        for (int i = 0; i < allfiles.length; i++){
            if (allfiles[i].getName().equals(filename)){
                Toast.makeText(SaveGame.this, "That filename already exists, please enter another.",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        }
        File file = new File(path, filename);
        FileOutputStream stream;
        try {
            stream = new FileOutputStream(file);

            Map<Position, Piece> boardToSave = GameBoard.activeGameBoard.getGameBoard();
            List<Move> moveHistory = GameBoard.activeGameBoard.gameHistory;

            Piece wking = null;
            Piece bking = null;

            for (int x = 0; x < 8; x++){
                for (int y = 0; y < 8; x++){
                    Position curr = new Position(x,y);
                    Piece currPiece = boardToSave.get(curr);
                    String pieceType;
                    String pieceColor;
                    if (currPiece != null){
                        pieceType = currPiece.toString();
                        Color color = currPiece.getColor();
                        if (pieceType.equals("k") && color == Color.WHITE){
                            wking = boardToSave.get(curr);
                        }
                        if (pieceType.equals("k") && color == Color.BLACK){
                            bking = boardToSave.get(curr);
                        }
                        if (color == Color.WHITE){

                            pieceColor = "white";
                        } else{
                            pieceColor = "black";
                        }
                    }
                    else{
                        pieceType = "blank";
                        pieceColor = "blank";
                    }

                    String line = ""+x+","+y+":"+pieceType+":"+pieceColor;
                    stream.write(line.getBytes());
                }
            }

            for (int i = 0; i < moveHistory.size(); i++){
                Position from = moveHistory.get(i).getFrom();
                Position to = moveHistory.get(i).getTo();
                Piece pMoved = moveHistory.get(i).getPieceMoved();
                Piece pTaken = moveHistory.get(i).getPieceCaptured();

                String pMovedString = pMoved.toString();
                String pTakenString;
                if (pTaken != null){
                    pTakenString = pTaken.toString();
                } else {
                    pTakenString = "blank";
                }
                int fromX = from.getRow();
                int fromY = from.getColumn();
                int toX = to.getRow();
                int toY = to.getColumn();
                String movedColor;
                String takenColor;
                if (pMoved.getColor() == Color.WHITE){
                    movedColor = "white";
                } else {
                    movedColor = "black";
                }
                if (pTaken.getColor() == Color.WHITE){
                    takenColor = "white";
                } else {
                    takenColor = "black";
                }

                String line = "##"+pMovedString+":"+movedColor+":"+pTakenString+":"+takenColor+
                        ":"+fromX+","+fromY+":"+toX+","+toY;
                stream.write(line.getBytes());
            }

            Color currPlayer = GameBoard.activeGameBoard.playerToMove();
            String nextPlayer;
            if (currPlayer == Color.BLACK){
                nextPlayer = "black";
            } else {
                nextPlayer = "white";
            }

            String line = "%%"+nextPlayer;
            stream.write(line.getBytes());

            String wkingmoved;
            String bkingmoved;
            if (wking.hasMoved()){
                wkingmoved = "y";
            } else {
                wkingmoved = "n";
            }

            if (bking.hasMoved()){
                bkingmoved = "y";
            } else {
                bkingmoved = "n";
            }

            String kings = "$$"+wkingmoved+":"+bkingmoved;
            stream.write(kings.getBytes());

            String save = "blah";
            stream.write(save.getBytes());
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        finish();
    }

    public void sgCancelClicked (View v){
        finish();
    }

    public void onBackPressed(){
        finish();
    }
}
