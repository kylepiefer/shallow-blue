package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GameBoardActivity extends AppCompatActivity {

    private GameBoard gameBoard;
    private GameBoardActivitySquare[][] gameBoardActivitySquares;
    private GameBoardActivitySquare selectedSquare;
    private ArrayList<GameBoardActivitySquare> highlightedSquares;
    private Color playerColor;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        // initialize toast
        this.toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);

        // use intent extras
        String colorString = getIntent().getStringExtra("Color");
        if (colorString.equalsIgnoreCase("White"))
            this.playerColor = Color.WHITE;
        else
            this.playerColor = Color.BLACK;

        // this is needed to handle the game logic
        String gameType = getIntent().getStringExtra("Type");
        if (gameType != null && gameType.equalsIgnoreCase("Custom"))
            this.gameBoard = new GameBoard(GameBoard.activeGameBoard);

        GameBoard.activeGameBoard = null;
        this.gameBoard = new GameBoard();

        // this is needed to map logical squares to images on the screen
        createGameBoardActivitySquareArray(this.playerColor);
        this.selectedSquare = null;
        this.highlightedSquares = new ArrayList<GameBoardActivitySquare>();

        Map<Position, Piece> gameBoardPiecePositions = this.gameBoard.getGameBoard();
        placePiecesOnBoard(gameBoardPiecePositions);

        if (this.playerColor == Color.BLACK) mockAIMove();
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

    private void drawBoardSquareHighlight(GameBoardActivitySquare gbas, int highlight) {
        if (gbas == null) return;

        ImageView highlightImage = new ImageView(getApplicationContext());

        int imageWidth = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);
        int imageHeight = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_height);

        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(imageWidth, imageHeight);
        highlightImage.setLayoutParams(layoutParams);
        highlightImage.setImageResource(highlight);
        highlightImage.setTag(new Integer(highlight));
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
        this.selectedSquare = null;
    }

    private void animateMove(final GameBoardActivitySquare from, final GameBoardActivitySquare to) {
        // cache values for handler
        final GameBoard gameBoard = this.gameBoard;
        final Color playerColor = this.playerColor;

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

                // TODO: Make this work like it's supposed to
                if (gameBoard.playerToMove() != playerColor) mockAIMove();
            }
        });
        imageCopy.startAnimation(animation);
    }

    private void mockAIMove() {
        /*List<Move> legalAIMoves = this.gameBoard.getAllMoves();
        Random numberGenerator = new Random();
        int moveNumber = (int)Math.floor(numberGenerator.nextDouble() * legalAIMoves.size());
        Move move = legalAIMoves.get(moveNumber);*/

        AIMove ai = new AIMove();
        List<Move> moves = ai.move(this.gameBoard, 3);
        Move move = moves.get(0);
        this.gameBoard.move(move);
        updateGameboard(move);
    }

    private void updateGameboard(Move move) {
        GameBoardActivitySquare from = getGBASForPosition(move.getFrom());
        GameBoardActivitySquare to = getGBASForPosition(move.getTo());

        animateMove(from, to);
    }

    public void onBoardTouched(View squareImage) {
        GameBoardActivitySquare gbas = (GameBoardActivitySquare)squareImage.getTag();

        if (this.selectedSquare == gbas) {
            removeAllSquareHighlights();
        }

        else if (this.highlightedSquares.contains(gbas)){
            if ((int)gbas.getHighlightImage().getTag() == R.drawable.board_square_highlight_possible) {
                showToast("This possible move is not currently legal.");
                return;
            }

            Move move = new Move(this.selectedSquare.getOccupyingPiece(),
                                 this.selectedSquare.getBoardPosition(),
                                 gbas.getBoardPosition());

            if (this.gameBoard.move(move)) {
                this.updateGameboard(move);
                removeAllSquareHighlights();
            }
            else Log.d("ShallowBlue", "Move Failed.");
        }

        else {
            removeAllSquareHighlights();
            this.selectedSquare = gbas;
            drawBoardSquareHighlight(this.selectedSquare, R.drawable.board_square_outline);

            if (gbas.getOccupyingPiece() != null) {
                Piece piece = gbas.getOccupyingPiece();

                List<Position> possibleMoves = piece.possibleMoves();
                List<Position> legalMoves = new ArrayList<Position>();
                for (int p = possibleMoves.size() - 1; p >= 0; p--) {
                    Position curr = possibleMoves.get(p);
                    Move possible = new Move(piece, piece.getPosition(), curr);
                    if (this.gameBoard.legalMove(possible)) {
                        possibleMoves.remove(p);
                        legalMoves.add(curr);
                    }
                }

                for (Position p : possibleMoves) {
                    GameBoardActivitySquare possibleSquare = getGBASForPosition(p);
                    drawBoardSquareHighlight(possibleSquare, R.drawable.board_square_highlight_possible);
                }

                for (Position p : legalMoves) {
                    GameBoardActivitySquare legalSquare = getGBASForPosition(p);
                    if (gbas.getOccupyingPiece().getColor() == this.playerColor) {
                        drawBoardSquareHighlight(legalSquare, R.drawable.board_square_highlight_legal);
                    } else {
                        drawBoardSquareHighlight(legalSquare, R.drawable.board_square_highlight_possible);
                    }
                }
            }
        }
    }

    private void showToast(String message) {
        CharSequence text = (CharSequence)message;
        this.toast.setText(text);
        this.toast.setGravity(Gravity.BOTTOM, 0, 0);
        this.toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }

    public void startingHelper(View v) {
        showToast("The Helper is not currently available."); // TODO
        new UrlConnection().new Connection().execute("connect");
    }

    public void altMove(View v ) {
        showToast("An alternate move is not currently available.");// TODO
        new UrlConnection().new Request().execute(gameBoard.pack());

    }

    public void undoMove(View v){
        List<Move> gameHistory = this.gameBoard.getGameHistory();
        Move lastMove = null;
        if (!gameHistory.isEmpty()) lastMove = gameHistory.get(gameHistory.size() - 1);
        if (this.gameBoard.undo()) {
            removeAllSquareHighlights();
            Move reversedMove = new Move(lastMove.getPieceMoved(), lastMove.getTo(), lastMove.getFrom());
            this.updateGameboard(reversedMove);
        } else {
            showToast("There is no move to undo.");
        }
    }

    public void redoMove(View v) {
        if (this.gameBoard.redo()) {
            removeAllSquareHighlights();
            List<Move> gameHistory = this.gameBoard.getGameHistory();
            Move nextMove = gameHistory.get(gameHistory.size() - 1);
            this.updateGameboard(nextMove);
        } else {
            showToast("There is no move to redo.");
        }
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

