package com.shallowblue.shallowblue;

import android.app.ActionBar;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.HashMap;
import java.util.Map;

public class GameBoardActivity extends AppCompatActivity {

    private GameBoard gameBoard;
    private Map<Position,Position> boardPositionToScreenPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.gameBoard = new GameBoard();
        this.boardPositionToScreenPosition = new HashMap<Position,Position>();

        // We can't place the pieces until we know where the squares are.
        // Set up a listener that will cache the board square positions
        // when they are available and handle setting up the pieces there.
        this.setViewTreeObserver();
    }

    private void setViewTreeObserver() {
        final GameBoardActivity gameBoardActivity = this;
        ViewTreeObserver viewTreeObserver
                = this.findViewById(R.id.game_board_view_root).getViewTreeObserver();
        viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                // Stop listening to prevent being overwhelmed with work.
                ViewTreeObserver viewTreeObserver
                        = gameBoardActivity.findViewById(R.id.game_board_view_root).getViewTreeObserver();
                viewTreeObserver.removeGlobalOnLayoutListener(this);

                // Clear out the square GameBoard positions.
                gameBoardActivity.boardPositionToScreenPosition = new HashMap<Position, Position>();

                // Get a handle to the gameboard root.
                ViewGroup gameBoardRoot =
                        (ViewGroup)gameBoardActivity.findViewById(R.id.game_board_root);

                // Find each square by getting each child of the gameboard root.
                for (int i = 0; i < gameBoardRoot.getChildCount(); i++) {
                    int[] location = new int[2];
                    View square = gameBoardRoot.getChildAt(i);

                    square.getLocationInWindow(location);
                    Position screenPosition = new Position(location[0], location[1]);

                    location[0] = (i / 8) + 1;
                    location[1] = (i % 8) + 1;
                    Position boardPosition = new Position(location[0], location[1]);

                    gameBoardActivity.boardPositionToScreenPosition.put(boardPosition, screenPosition);
                }

                // arrange the pieces
                gameBoardActivity.placePieces();
            }
        });
    }

    public void placePieces() {
        Map<Position, Piece> map = this.gameBoard.getGameBoard();
        for (Position position : map.keySet()) {
            Position screenPosition = this.boardPositionToScreenPosition.get(position);
            Piece piece = map.get(position);
            this.placePieceInSquare(piece, screenPosition);
        }
    }

    private void placePieceInSquare(Piece piece, Position square) {
        FrameLayout parent = (FrameLayout)this.findViewById(R.id.game_board_view_root);
        ImageView pieceImage = new ImageView(this);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = square.getRow() + 15;
        layoutParams.leftMargin = square.getColumn() - 185; // TODO: This is a hack. Fix it.
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

        parent.addView(pieceImage);
        pieceImage.bringToFront();
        setContentView(parent);
    }

    public void startinghelper(View v){

    }

    public void altmove(View v){

    }

    public void undomove(View v){

    }

    public void redomove(View v){

    }

    public void optionsScreen(View v){

    }
}
