package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import java.util.ArrayList;
import java.util.Map;

public class GameBoardActivity extends AppCompatActivity {

    private GameBoard gameBoard;
    private GameBoardActivitySquare[][] gameBoardActivitySquares;
    private GameBoardActivitySquare selectedSquare;
    private ArrayList<GameBoardActivitySquare> highlightedSquares;
    private Color playerColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // use intent extras
        String colorString = getIntent().getStringExtra("Color");
        if (colorString.equalsIgnoreCase("White"))
            this.playerColor = Color.WHITE;
        else
            this.playerColor = Color.BLACK;

        // this is needed to handle the game logic
        String gameType = getIntent().getStringExtra("Type");
        if (gameType != null && gameType.equalsIgnoreCase("Custom"));
            GameBoard.gameBoard = null;
        this.gameBoard = new GameBoard();

        // this is needed to map logical squares to images on the screen
        createGameBoardActivitySquareArray(this.playerColor);
        this.selectedSquare = null;
        this.highlightedSquares = new ArrayList<GameBoardActivitySquare>();

        Map<Position, Piece> gameBoardPiecePositions = this.gameBoard.getGameBoard();
        placePiecesOnBoard(gameBoardPiecePositions);
    }

    private void createGameBoardActivitySquareArray(Color playerColor) {
        // allocate the array of objects
        this.gameBoardActivitySquares = new GameBoardActivitySquare[8][8];

        // get a reference to the grid layout
        GridLayout grid = (GridLayout)this.findViewById(R.id.game_board_grid);

        // calculate the size of each image for later use
        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);
        int imageHeight = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_height);

        // used to color squares
        boolean isBlack = false;

        // iterate through the array to make all our squares
        for (int r = 0; r < this.gameBoardActivitySquares.length; r++) {
            // we need to change this each row because each row starts with the color the last
            // row ended with
            isBlack = !isBlack;
            for (int c = 0; c < this.gameBoardActivitySquares[r].length; c++) {
                int screenRow = 7 - r;
                int screenColumn = c;
                int boardRow = r;
                int boardColumn = c;
                if (playerColor == Color.BLACK) {
                    boardRow = 7 - r;
                    boardColumn = 7 - c;
                }

                // set the positions
                Position boardPosition = new Position(boardRow, boardColumn);
                Position screenPosition = new Position(screenRow, screenColumn);

                // make the associated frame to hold images
                FrameLayout squareLayout = new FrameLayout(getApplicationContext());
                GridLayout.LayoutParams layoutParams =
                        new GridLayout.LayoutParams(GridLayout.spec(screenRow), GridLayout.spec(screenColumn));
                layoutParams.width = imageWidth;
                layoutParams.height = imageHeight;
                squareLayout.setLayoutParams(layoutParams);
                grid.addView(squareLayout, layoutParams);

                // make the GBAS
                GameBoardActivitySquare gbas = new GameBoardActivitySquare(boardPosition, squareLayout);
                gbas.setScreenPosition(screenPosition);
                this.gameBoardActivitySquares[boardRow][boardColumn] = gbas;

                // add the square tile image
                ImageView image = new ImageView(getApplicationContext());
                FrameLayout.LayoutParams frameLayoutParams = new FrameLayout.LayoutParams(imageWidth, imageHeight);
                image.setLayoutParams(frameLayoutParams);
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBoardTouched(v);
                    }
                });
                if (isBlack) image.setImageResource(R.drawable.darksquaretile);
                else image.setImageResource(R.drawable.graysquaretile);
                image.setTag(gbas);
                gbas.setSquareImage(image);

                // flip back color
                isBlack = !isBlack;
            }
        }
    }

    private GameBoardActivitySquare getGBASForPosition(Position position) {
        GameBoardActivitySquare gbas = this.gameBoardActivitySquares[position.getRow()][position.getColumn()];
        return gbas;
    }

    private void placePiecesOnBoard(Map<Position, Piece> gameBoard) {
        for (Position position : gameBoard.keySet()) {
            GameBoardActivitySquare gbas = getGBASForPosition(position);
            Piece piece = gameBoard.get(position);
            this.placePieceInSquare(piece, gbas);
        }
    }

    private void placePieceInSquare(Piece piece, GameBoardActivitySquare gbas) {
        // make the image
        ImageView pieceImage = new ImageView(getApplicationContext());
        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);
        int imageHeight = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_height);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageWidth, imageWidth);
        pieceImage.setLayoutParams(layoutParams);

        // get piece color
        if (piece.getColor() == Color.BLACK) {
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
        } else if (piece.getColor() == Color.WHITE) {
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

        gbas.placePiece(piece, pieceImage);
    }

    private void drawBoardSquareHighlight(GameBoardActivitySquare gbas) {
        if (gbas == null) return;

        // make the image
        ImageView highlightImage = new ImageView(getApplicationContext());

        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);
        int imageHeight = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_height);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageWidth, imageHeight);
        highlightImage.setLayoutParams(layoutParams);
        highlightImage.setImageResource(R.drawable.board_square_outline);
        gbas.addHighlight(highlightImage);

        synchronized(this.highlightedSquares) {
            this.highlightedSquares.add(gbas);
        }
    }

    private void removeBoardSquareOutline(GameBoardActivitySquare gbas) {
        if (gbas == null) return;
        gbas.removeHighlight();
        synchronized(this.highlightedSquares) {
            this.highlightedSquares.remove(gbas);
        }
    }

    private void removeAllSquareHighlights() {
        for (int i = this.highlightedSquares.size() - 1; i >= 0; i--) {
            removeBoardSquareOutline(this.highlightedSquares.get(i));
        }
    }

    private void animateMove(final GameBoardActivitySquare from, final GameBoardActivitySquare to) {
        // make sure the move is valid
        final Piece piece = from.getOccupyingPiece();
        final ImageView pieceImage = from.getPieceImage();
        if (piece == null || pieceImage == null) return;

        final int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);
        final int imageHeight = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_height);

        final int deltaScreenRows = to.getScreenPosition().getRow() - from.getScreenPosition().getRow();
        final int deltaScreenColumns = to.getScreenPosition().getColumn() - from.getScreenPosition().getColumn();
        final int deltaX = imageWidth * deltaScreenColumns;
        final int deltaY = imageHeight * deltaScreenRows;
        final long distance = (long)Math.sqrt(deltaScreenRows * deltaScreenRows + deltaScreenColumns * deltaScreenColumns);

        final ImageView imageCopy = new ImageView(this);
        final FrameLayout.LayoutParams copyParams = new FrameLayout.LayoutParams(imageWidth, imageHeight);
        copyParams.topMargin = from.getScreenPosition().getRow() * imageHeight;
        copyParams.leftMargin = from.getScreenPosition().getColumn() * imageWidth;
        imageCopy.setLayoutParams(copyParams);
        imageCopy.setImageDrawable(pieceImage.getDrawable());

        final FrameLayout animationLayer = (FrameLayout)this.findViewById(R.id.game_board_animation_layer);
        animationLayer.addView(imageCopy);
        pieceImage.setVisibility(View.INVISIBLE);

        TranslateAnimation animation = new TranslateAnimation(Animation.ABSOLUTE, (float)0,
                Animation.ABSOLUTE, (float)deltaX,
                Animation.ABSOLUTE, (float)0,
                Animation.ABSOLUTE, (float)deltaY);
        animation.setDuration(200 * distance);
        animation.setZAdjustment(Animation.ZORDER_TOP);
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                from.removePiece();
                to.placePiece(piece, pieceImage);
                pieceImage.setVisibility(View.VISIBLE);
                imageCopy.clearAnimation();
                animationLayer.removeView(imageCopy);
            }
        });
        imageCopy.startAnimation(animation);
    }

    private void updateGameboard(Move move) {
        // find which gbas has the squares
        GameBoardActivitySquare from = null;
        GameBoardActivitySquare to = null;
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                GameBoardActivitySquare gbas = this.gameBoardActivitySquares[r][c];
                if (gbas.getBoardPosition() == move.getFrom()) from = gbas;
                else if (gbas.getBoardPosition() == move.getTo()) to = gbas;

                if (from != null && to != null) break;
            }
            if (from != null && to != null) break;
        }

        animateMove(from, to);
    }

    public void onBoardTouched(View squareImage) {
        GameBoardActivitySquare gbas = (GameBoardActivitySquare)squareImage.getTag();

        if (this.selectedSquare == gbas) {
            removeAllSquareHighlights();
            this.selectedSquare = null;
        }

        else if (this.highlightedSquares.contains(gbas)){
            Move move = new Move(this.selectedSquare.getOccupyingPiece(),
                                this.selectedSquare.getBoardPosition(),
                                gbas.getBoardPosition());
            if (this.gameBoard.move(move)) {
                this.updateGameboard(move);
                removeAllSquareHighlights();
                this.selectedSquare = null;
            }
            else Log.d("ShallowBlue", "Move Failed.");
        }

        else {
            removeAllSquareHighlights();
            drawBoardSquareHighlight(gbas);
            this.selectedSquare = gbas;

            if (gbas.getOccupyingPiece() != null) {
                Piece piece = gbas.getOccupyingPiece();

                ArrayList<Position> possibleMoves = piece.possibleMoves();
                for (Position p : possibleMoves) {
                    GameBoardActivitySquare possibleSquare = getGBASForPosition(p);
                    drawBoardSquareHighlight(possibleSquare);
                }
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

