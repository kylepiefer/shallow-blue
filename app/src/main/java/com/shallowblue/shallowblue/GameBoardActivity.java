package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameBoardActivity extends AppCompatActivity {

    private static final int END_OF_GAME_REQUEST = 1;

    private GameBoard gameBoard;
    private GameBoardActivitySquare[][] gameBoardActivitySquares;
    private GameBoardActivitySquare selectedSquare;
    private ArrayList<GameBoardActivitySquare> highlightedSquares;
    private String gameMode;
    private Color playerColor;
    private int difficulty = 0;
    private Toast toast = null;
    private SavedGameManager savedGameManager;
    private int movesToUndo = 0;
    private int movesToRedo = 0;
    private List<Move> suggestedMoves = null;
    private int suggestedMoveIndex = 0;
    private boolean helperIsOn = false;
    private AsyncTask runningTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_board);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        this.savedGameManager = new SavedGameManager();

        Intent settings = getIntent();

        if (settings.hasExtra("Game Mode")) {
            this.gameMode = settings.getStringExtra("Game Mode");
        } else {
            this.gameMode = "PVC";
        }
        Log.d("GameBoardActivity", "Game mode is: " + this.gameMode);

        if (settings.hasExtra("Difficulty")) {
            this.difficulty = settings.getIntExtra("Difficulty", 1);
        } else {
            this.difficulty = 0;
        }

        String colorString = settings.getStringExtra("Color");
        if (colorString != null && colorString.equalsIgnoreCase("Black"))
            this.playerColor = Color.BLACK;
        else
            this.playerColor = Color.WHITE;

        String gameType = settings.getStringExtra("Type");
        if (gameType != null && gameType.equalsIgnoreCase("Custom")) {
            this.gameBoard = new GameBoard(GameBoard.customPositions);
        } else if (gameType != null && gameType.equalsIgnoreCase("Load Game")){
            this.gameBoard = new GameBoard(GameBoard.activeGameBoard);
        } else {
            this.gameBoard = new GameBoard();
        }
        GameBoard.customPositions = null;
        GameBoard.activeGameBoard = null;

        // this is needed to map logical squares to images on the screen
        createGameBoardActivitySquareArray(this.playerColor);
        this.selectedSquare = null;
        this.highlightedSquares = new ArrayList<GameBoardActivitySquare>();

        Map<Position, Piece> gameBoardPiecePositions = this.gameBoard.getGameBoard();
        refreshBoard(gameBoardPiecePositions);

        if (this.gameMode.equals("CVC") || this.playerColor == Color.BLACK) aiMove();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == END_OF_GAME_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("Action");
                if (action.equalsIgnoreCase("Undo")) {
                    undoMove(null);
                } else if (action.equalsIgnoreCase("Quit")) {
                    Intent quit = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(quit);
                    finish();
                }
            }
        }
    }

    public GameBoard getGameBoard() { return this.gameBoard; }

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

    private void refreshBoard(Map<Position, Piece> gameBoard) {
        synchronized (this.gameBoardActivitySquares) {
            // remove all pieces
            for (int r = 0; r < this.gameBoardActivitySquares.length; r++){
                for (int c = 0; c < this.gameBoardActivitySquares[r].length; c++) {
                    Position position = new Position(r, c);
                    GameBoardActivitySquare gbas = getGBASForPosition(position);
                    if (gbas.getOccupyingPiece() != null) {
                        gbas.removePiece();
                    }
                }
            }

            for (Position position : gameBoard.keySet()) {
                GameBoardActivitySquare gbas = getGBASForPosition(position);
                Piece piece = gameBoard.get(position);
                if (piece != null) this.placePieceInSquare(piece, gbas);
            }
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

    private void animateMove(final GameBoardActivitySquare from, final GameBoardActivitySquare to, final boolean advanceTurn) {
        // cache values for handler
        final GameBoardActivity gameBoardActivity = this;
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
                suggestedMoves = null;
                gameBoardActivity.refreshBoard(gameBoardActivity.gameBoard.getGameBoard());
                imageCopy.clearAnimation();
                animationLayer.removeView(imageCopy);

                if (selectedSquare != null) {
                    GameBoardActivitySquare temp = selectedSquare;
                    removeAllSquareHighlights();
                    onBoardTouched(temp.getSquareImage());
                }

                if (gameBoard.gameOver()) {
                    endGame();
                    return;
                }

                if (movesToUndo > 0) {
                    movesToUndo = movesToUndo - 1;
                    if (movesToUndo > 0) {
                        undoMove();
                        return;
                    }
                }

                if (movesToRedo > 0) {
                    movesToRedo = movesToRedo - 1;
                    if (movesToRedo > 0) {
                        redoMove();
                        return;
                    }
                }

                aiMove();
            }
        });

        imageCopy.setAnimation(animation);
        animation.start();
    }

    private void aiMove() {
        GameBoard[] gameBoards = new GameBoard[1];
        gameBoards[0] = getGameBoard();
        runningTask = new AIMoveTask();
        runningTask.execute(gameBoards);
    }

    private void updateGameboard(Move move, boolean advanceTurn) {
        synchronized (this.gameBoardActivitySquares) {
            //Log.d("GameBoardActivity", "Cache Hits: " + gameBoard.cacheHits);
            gameBoard.cacheHits = 0;
            endTask();
            suggestedMoves = null;

            GameBoardActivitySquare from = getGBASForPosition(move.getFrom());
            GameBoardActivitySquare to = getGBASForPosition(move.getTo());
            animateMove(from, to, advanceTurn);
        }
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

            boolean moveSucceeded = false;
            synchronized (gameBoard) {
                moveSucceeded = gameBoard.move(move);
            }

            if (moveSucceeded) {
                this.updateGameboard(move, true);
                removeAllSquareHighlights();
            } else {
                //Log.d("ShallowBlue", "Player move failed: " + move.toString() + " Reason: " + getGameBoard().getLastExplanation());
            }
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
        if (this.toast == null) this.toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        CharSequence text = (CharSequence)message;
        this.toast.setText(text);
        this.toast.setGravity(Gravity.BOTTOM, 0, 0);
        this.toast.setDuration(Toast.LENGTH_SHORT);
        TextView view = (TextView) this.toast.getView().findViewById(android.R.id.message);
        if (view != null) view.setGravity(Gravity.CENTER);
        toast.show();
    }

    public void startingHelper(View v) {
        if (gameMode.equals("CVC")) return;
        if (helperIsOn) {
            helperIsOn = false;
            removeAllSquareHighlights();
        } else {
            helperIsOn = true;
            if (playerColor == getGameBoard().playerToMove()) aiMove();
        }
    }

    public void altMove(View v ) {
        if (gameMode.equals("CVC")) return;

        if(suggestedMoves == null) {
            if (!helperIsOn) startingHelper(null);
            return;
        }

        removeAllSquareHighlights();
        this.selectedSquare = getGBASForPosition(suggestedMoves.get(suggestedMoveIndex).getFrom());
        drawBoardSquareHighlight(
                this.selectedSquare,
                R.drawable.board_square_outline);

        drawBoardSquareHighlight(
                getGBASForPosition(suggestedMoves.get(suggestedMoveIndex).getTo()),
                R.drawable.board_square_highlight_legal);
        suggestedMoveIndex = (suggestedMoveIndex+1)%suggestedMoves.size();
    }

    private boolean undoMove() {
        List<Move> gameHistory = this.gameBoard.getGameHistory();
        Move lastMove = null;
        if (!gameHistory.isEmpty()) lastMove = gameHistory.get(gameHistory.size() - 1);

        boolean undoSucceeded = false;
        synchronized (gameBoard) {
            undoSucceeded = gameBoard.undo();
        }

        if (undoSucceeded) {
            removeAllSquareHighlights();
            Move reversedMove = new Move(lastMove.getPieceMoved(), lastMove.getTo(), lastMove.getFrom());
            updateGameboard(reversedMove, false);
            return true;
        } else {
            showToast("There is no move to undo.");
            return false;
        }
    }

    public void undoMove(View v){
        if (movesToUndo > 0 || movesToRedo > 0) return;
        if (gameMode.equals("CVC")) return;
        movesToUndo = gameBoard.playerToMove() == playerColor ? 2 : 1;
        boolean undone = undoMove();
        if (!undone) {
            movesToUndo = 0;
        }
    }

    private boolean redoMove() {
        boolean redoSucceeded = false;
        synchronized (gameBoard) {
            redoSucceeded = gameBoard.redo();
        }

        if (redoSucceeded) {
            removeAllSquareHighlights();
            List<Move> gameHistory = this.gameBoard.getGameHistory();
            Move nextMove = gameHistory.get(gameHistory.size() - 1);
            updateGameboard(nextMove, false);
            return true;
        } else {
            showToast("There is no move to redo.");
            return false;
        }
    }

    public void redoMove(View v) {
        if (movesToUndo > 0 || movesToRedo > 0) return;
        if (gameMode.equals("CVC")) return;
        movesToRedo = gameBoard.playerToMove() == playerColor ? 2 : 1;
        boolean redone = redoMove();
        if (!redone) {
            movesToRedo = 0;
        }
    }

    public void optionsScreen(View v){
        if (gameMode.equals("CVC")) return;
        GameBoard.activeGameBoard = this.getGameBoard();
        Intent openOptions = new Intent(getApplicationContext(),OptionsPopUpWindow.class);
        openOptions.putExtra("Game Mode", "PVC");
        startActivity(openOptions);
    }

    public void onBackPressed(){
        Intent check = new Intent(getApplicationContext(),LeaveGameVerify.class);
        Bundle verify = new Bundle();
        verify.putString("activity","main");
        check.putExtra("next",verify);
        startActivity(check);
    }

    private void endTask() {
        if (runningTask != null) {
            runningTask.cancel(true);
            runningTask = null;
        }
    }

    private void endGame() {
        movesToRedo = 0;
        movesToUndo = 0;
        Intent endGame = new Intent(this, EndOfGameActivity.class);
        Bundle params = new Bundle();
        Color curr = gameBoard.playerToMove;
        int color = 0;
        if (curr == Color.WHITE){
            color = 0;
        }
        else {
            color = 1;
        }
        params.putInt("winner", color);
        endGame.putExtra("text",params);
        startActivityForResult(endGame, END_OF_GAME_REQUEST);
    }

    private class AIMoveTask extends AsyncTask<GameBoard, Integer, Move> {
        protected Move doInBackground(GameBoard... gameBoards) {
            GameBoard board = gameBoards[0];
            AIMove ai = AIMoveFactory.newAIMove();
            List<Move> moves = ai.move(board, difficulty);
            if (moves.isEmpty()) return null;

            if (!gameMode.equals("CVC") && board.playerToMove() == playerColor) {
                suggestedMoves = moves;
                return moves.get(0);
            }

            Move move = moves.get(0);
            move = new Move(gameBoard.getGameBoard().get(move.getFrom()), move.getFrom(), move.getTo());
            return move;
        }

        protected void onPostExecute(Move move) {
            if (move == null) {
                return;
            }

            if (!gameMode.equals("CVC") && playerColor == getGameBoard().playerToMove()) {
                if (helperIsOn) altMove(null);
                return;
            }

            boolean moveSucceeded = false;
            synchronized (gameBoard) {
                moveSucceeded = gameBoard.move(move);
            }

            if (moveSucceeded) {
                updateGameboard(move, false);
            } else {
                //Log.d("ShallowBlue", "AI move failed: " + move.toString() + " Reason: " + getGameBoard().getLastExplanation());
            }

            System.gc();
        }
    }
}
