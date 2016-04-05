package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class GameBoardActivity extends AppCompatActivity {

    private GameBoard gameBoard;
    private Map<Position,Position> boardPositionToScreenPosition;
    private GameBoardActivitySquare[][] gameBoardActivitySquares;
    private GameBoardActivitySquare selectedSquare;
    private ArrayList<GameBoardActivitySquare> outlinedSquares;
    private Color color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // use intent extras
        String colorString = getIntent().getStringExtra("Color");
        if (colorString.equalsIgnoreCase("White"))
            this.color = Color.WHITE;
        else
            this.color = Color.BLACK;

        // this is needed to handle the game logic
        this.gameBoard = new GameBoard();

        // this is needed to map logical squares to images on the screen
        this.createGameBoardSquareGrid(this.color);
        this.selectedSquare = null;
        this.outlinedSquares = new ArrayList<GameBoardActivitySquare>();

        this.placePieces();
    }

    private void createGameBoardSquareGrid(Color color) {
        // allocate the array of objects
        this.gameBoardActivitySquares = new GameBoardActivitySquare[8][8];

        // get a reference to the grid layout
        GridLayout layout = (GridLayout)this.findViewById(R.id.game_board_root);

        // calculate the size of each image for later use
        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.dimen_game_board_square_width);

        // used to color squares
        boolean isBlack = true;

        // iterate through the array to make all our squares
        for (int r = 0; r < this.gameBoardActivitySquares.length; r++) {
            // we need to change this each row because each row starts with the color the last
            // row ended with
            isBlack = !isBlack;
            for (int c = 0; c < this.gameBoardActivitySquares[r].length; c++) {
                Position squarePosition = new Position(r,c);

                ImageView image = new ImageView(getApplicationContext());
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) { onBoardTouched(v); }
                });
                if (isBlack) image.setImageResource(R.drawable.darksquaretile);
                else image.setImageResource(R.drawable.graysquaretile);

                GridLayout.LayoutParams layoutParams =
                        new GridLayout.LayoutParams(GridLayout.spec(squarePosition.getRow()),
                                                    GridLayout.spec(squarePosition.getColumn()));
                layoutParams.width = imageWidth;
                layoutParams.height = imageWidth;
                layout.addView(image, layoutParams);

                GameBoardActivitySquare gbas = new GameBoardActivitySquare(squarePosition, image);
                int screenRow = 0;
                int screenColumn = 0;
                if (color == Color.WHITE) {
                    screenRow = imageWidth * 7 - imageWidth * r;
                    screenColumn = imageWidth * c;
                } else {
                    screenRow = imageWidth * r;
                    screenColumn = imageWidth * 7 - imageWidth * c;
                }
                gbas.setScreenPosition(new Position(screenRow, screenColumn));
                image.setTag(gbas);
                this.gameBoardActivitySquares[r][c] = gbas;

                // flip back color
                isBlack = !isBlack;
            }
        }
    }

    public void placePieces() {
        Map<Position, Piece> gameBoardMap = this.gameBoard.getGameBoard();
        for (Position position : gameBoardMap.keySet()) {
            GameBoardActivitySquare gbas =
                    this.gameBoardActivitySquares[position.getRow()][position.getColumn()];
            Piece piece = gameBoardMap.get(position);
            gbas.setOccupyingPiece(piece);
            this.placePieceInSquare(piece, gbas.getBoardPosition());
        }
    }

    private void placePieceInSquare(Piece piece, Position square) {
        GameBoardActivitySquare gbas = this.gameBoardActivitySquares[square.getRow()][square.getColumn()];
        RelativeLayout container = (RelativeLayout)this.findViewById(R.id.piece_container);
        ImageView pieceImage = new ImageView(getApplicationContext());

        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.dimen_game_board_square_width);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(imageWidth, imageWidth);
        layoutParams.leftMargin = gbas.getScreenPosition().getColumn();
        layoutParams.topMargin = gbas.getScreenPosition().getRow();
        pieceImage.setLayoutParams(layoutParams);

        if (piece.getColor() == Color.WHITE) {
            if (piece instanceof Pawn) {
                pieceImage.setImageResource(R.drawable.black_pawn);
            } else if (piece instanceof Bishop) {
                pieceImage.setImageResource(R.drawable.black_bishop);
            } else if (piece instanceof Knight) {
                pieceImage.setImageResource(R.drawable.black_knight);
            } else if (piece instanceof Rook) {
                pieceImage.setImageResource(R.drawable.black_rook);
            } else if (piece instanceof Queen) {
                pieceImage.setImageResource(R.drawable.black_queen);
            } else if (piece instanceof King) {
                pieceImage.setImageResource(R.drawable.black_king);
            }
        } else if (piece.getColor() == Color.BLACK) {
            if (piece instanceof Pawn) {
                pieceImage.setImageResource(R.drawable.white_pawn);
            } else if (piece instanceof Bishop) {
                pieceImage.setImageResource(R.drawable.white_bishop);
            } else if (piece instanceof Knight) {
                pieceImage.setImageResource(R.drawable.white_knight);
            } else if (piece instanceof Rook) {
                pieceImage.setImageResource(R.drawable.white_rook);
            } else if (piece instanceof Queen) {
                pieceImage.setImageResource(R.drawable.white_queen);
            } else if (piece instanceof King) {
                pieceImage.setImageResource(R.drawable.white_king);
            }
        }

        container.addView(pieceImage);
        this.gameBoardActivitySquares[square.getRow()][square.getColumn()].setImage(pieceImage);
    }

    public void drawBoardSquareOutline(GameBoardActivitySquare gbas) {
        if (gbas == null || gbas.getOutline() != null) return;

        FrameLayout container = (FrameLayout)this.findViewById(R.id.board_container);
        ImageView outlineImage = new ImageView(getApplicationContext());

        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.dimen_game_board_square_width);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageWidth, imageWidth);
        layoutParams.leftMargin = gbas.getScreenPosition().getColumn();
        layoutParams.topMargin = gbas.getScreenPosition().getRow();
        outlineImage.setLayoutParams(layoutParams);
        outlineImage.setImageResource(R.drawable.board_square_outline);
        container.addView(outlineImage);

        gbas.setOutline(outlineImage);
        synchronized(this.outlinedSquares) {
            this.outlinedSquares.add(gbas);
        }
    }

    public void removeBoardSquareOutline(GameBoardActivitySquare gbas) {
        if (gbas == null) return;
        ImageView outline = gbas.getOutline();
        if (outline != null) {
            FrameLayout container = (FrameLayout)this.findViewById(R.id.board_container);
            container.removeView(outline);
            gbas.clearOutline();
            synchronized(this.outlinedSquares) {
                this.outlinedSquares.remove(gbas);
            }
        }
    }

    public void onBoardTouched(View squareImage) {
        GameBoardActivitySquare gbas = (GameBoardActivitySquare)squareImage.getTag();

        if (this.selectedSquare == gbas) {
            for (int i = this.outlinedSquares.size() - 1; i >= 0; i--)
                removeBoardSquareOutline(this.outlinedSquares.get(i));
            this.selectedSquare = null;
        } else {
            for (int i = this.outlinedSquares.size() - 1; i >= 0; i--)
                removeBoardSquareOutline(this.outlinedSquares.get(i));
            drawBoardSquareOutline(gbas);
            this.selectedSquare = gbas;
        }

        if (gbas.getOccupyingPiece() != null) {
            Piece piece = gbas.getOccupyingPiece();
            String pieceString = "";
            if (piece instanceof Rook) pieceString = "Rook";
            if (piece instanceof Knight) pieceString = "Knight";
            if (piece instanceof Bishop) pieceString = "Bishop";
            if (piece instanceof King) pieceString = "King";
            if (piece instanceof Queen) pieceString = "Queen";
            if (piece instanceof Pawn) pieceString = "Pawn";
            Log.d("ShallowBlue", "Occupying piece is " + pieceString);

            ArrayList<Position> possibleMoves = piece.possibleMoves();
            Log.d("ShallowBlue", possibleMoves.toString());
            for (Position p : possibleMoves) {
                GameBoardActivitySquare possibleSquare =
                        this.gameBoardActivitySquares[p.getRow()][p.getColumn()];
                drawBoardSquareOutline(possibleSquare);
            }
        }
    }

    public void startingHelper(View v){

    }

    public void altMove(View v){

    }

    public void undoMove(View v){

    }

    public void redoMove(View v){

    }

    public void optionsScreen(View v){
        Intent openOptions = new Intent(getApplicationContext(),OptionsPopUpWindow.class);
        startActivity(openOptions);
    }

    public void onBackPressed(){
        Intent check = new Intent(getApplicationContext(),LeaveGameVerify.class);
        Bundle verify = new Bundle();
        verify.putString("activity","main");
        check.putExtra("next",verify);
        startActivity(check);
    }


}
