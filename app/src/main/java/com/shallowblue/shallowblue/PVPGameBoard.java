package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PVPGameBoard extends AppCompatActivity {

    private static final int END_OF_GAME_REQUEST = 1;
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
    public List<Move> selLegal;
    public final int redHighlight = R.drawable.board_square_highlight_possible;
    public final int greenHighlight = R.drawable.board_square_highlight_legal;
    public final int yellowBoardSelection = R.drawable.board_square_outline;
    public Move castle;
    ImageView temp;
    boolean doneWithPrev;
    private List<Move> redoMoves;
    private final int checkImage = R.drawable.check_image2;
    private final int animationFadeIn = R.anim.fadein;
    private final int animationFadeOut = R.anim.fadeout;
    private ImageView checkWhite;
    private ImageView checkBlack;

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
        doneWithPrev = true;
        redoMoves = new ArrayList<>();
        checkWhite = (ImageView) findViewById(R.id.player1Check);
        checkBlack = (ImageView) findViewById(R.id.player2Check);

        GameBoard.activeGameBoard = new GameBoard();

        GameBoard.activeGameBoard.gameHistory = new ArrayList<Move>();

        pvpGameboard = new ImageView[8][8];
        selMoves = new ArrayList<>();
        turn = Color.WHITE;

        if (count == 1){
            boardSetup = new HashMap<Position, Piece>();
            availPos = new Position[8][8];
            createPositionArray();
            initializeBoard();
            startNewPvpGame();
        }

        if (count == 2){
            initializeBoard();
            addCustomSetup();
        }
        GameBoard.activeGameBoard.gameBoard = boardSetup;

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
            Pawn start = new Pawn(availPos[1][y], Color.WHITE);
            pvpGameboard[1][y].setImageResource(start.getDrawableId());
            boardSetup.put(availPos[1][y], start);

            Pawn begin = new Pawn(availPos[6][y], Color.BLACK);
            flipBlackPieces(begin);
            pvpGameboard[6][y].setImageResource(begin.getDrawableId());
            boardSetup.put(availPos[6][y], begin);
        }

        Rook setup = new Rook(availPos[0][0], Color.WHITE);
        pvpGameboard[0][0].setImageResource(setup.getDrawableId());
        Rook setup1 = new Rook(availPos[0][7], Color.WHITE);
        pvpGameboard[0][7].setImageResource(setup1.getDrawableId());
        Rook setup2 = new Rook(availPos[7][0], Color.BLACK);
        flipBlackPieces(setup2);
        pvpGameboard[7][0].setImageResource(setup2.getDrawableId());
        Rook setup3 = new Rook(availPos[7][7], Color.BLACK);
        flipBlackPieces(setup3);
        pvpGameboard[7][7].setImageResource(setup3.getDrawableId());

        boardSetup.put(availPos[0][0], setup);
        boardSetup.put(availPos[0][7], setup1);
        boardSetup.put(availPos[7][0], setup2);
        boardSetup.put(availPos[7][7], setup3);

        Knight setup4 = new Knight(availPos[0][1], Color.WHITE);
        pvpGameboard[0][1].setImageResource(setup4.getDrawableId());
        Knight setup5 = new Knight(availPos[0][6], Color.WHITE);
        pvpGameboard[0][6].setImageResource(setup5.getDrawableId());
        Knight setup6 = new Knight(availPos[7][1], Color.BLACK);
        flipBlackPieces(setup6);
        pvpGameboard[7][1].setImageResource(setup6.getDrawableId());
        Knight setup7 = new Knight(availPos[7][6], Color.BLACK);
        flipBlackPieces(setup7);
        pvpGameboard[7][6].setImageResource(setup7.getDrawableId());

        boardSetup.put(availPos[0][1], setup4);
        boardSetup.put(availPos[0][6], setup5);
        boardSetup.put(availPos[7][1], setup6);
        boardSetup.put(availPos[7][6], setup7);

        Bishop setup8 = new Bishop(availPos[0][2], Color.WHITE);
        pvpGameboard[0][2].setImageResource(setup8.getDrawableId());
        Bishop setup9 = new Bishop(availPos[0][5], Color.WHITE);
        pvpGameboard[0][5].setImageResource(setup9.getDrawableId());
        Bishop setup10 = new Bishop(availPos[7][2], Color.BLACK);
        flipBlackPieces(setup10);
        pvpGameboard[7][2].setImageResource(setup10.getDrawableId());
        Bishop setup11 = new Bishop(availPos[7][5], Color.BLACK);
        flipBlackPieces(setup11);
        pvpGameboard[7][5].setImageResource(setup11.getDrawableId());

        boardSetup.put(availPos[0][2], setup8);
        boardSetup.put(availPos[0][5], setup9);
        boardSetup.put(availPos[7][2], setup10);
        boardSetup.put(availPos[7][5], setup11);

        Queen setup12 = new Queen(availPos[0][3], Color.WHITE);
        pvpGameboard[0][3].setImageResource(setup12.getDrawableId());
        Queen setup13 = new Queen(availPos[7][3], Color.BLACK);
        flipBlackPieces(setup13);
        pvpGameboard[7][3].setImageResource(setup13.getDrawableId());

        boardSetup.put(availPos[0][3], setup12);
        boardSetup.put(availPos[7][3], setup13);

        King setup14 = new King(availPos[0][4], Color.WHITE);
        pvpGameboard[0][4].setImageResource(setup14.getDrawableId());
        King setup15 = new King(availPos[7][4], Color.BLACK);
        flipBlackPieces(setup15);
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
        if (!doneWithPrev){
            return;
        }
        temp = (ImageView) v;
        Position tempPos = imagePositions.get(temp);
        Piece tempPiece = boardSetup.get(tempPos);
        boolean foundMatch = false;
        if (selImage == null && tempPiece != null && tempPiece.getColor() != turn){
            return;
        }
        if (selImage == temp){
            selImage.setBackgroundResource(0);
            for (int i = 0; i < selMoves.size(); i++) {
                int selMoveX = selMoves.get(i).getRow();
                int selMoveY = selMoves.get(i).getColumn();
                pvpGameboard[selMoveX][selMoveY].setBackgroundResource(0);
            }
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
                selImage.setBackgroundResource(yellowBoardSelection);
                selPiece = tempPiece;
                selPosition = tempPos;
                selMoves = selPiece.possibleMoves();
                selLegal = GameBoard.activeGameBoard.getLegalMoves(selPosition);
                for (int i = 0; i < selMoves.size(); i++) {
                    int selMoveX = selMoves.get(i).getRow();
                    int selMoveY = selMoves.get(i).getColumn();
                    pvpGameboard[selMoveX][selMoveY].setBackgroundResource(redHighlight);
                    Position curr = selMoves.get(i);
                    Move possible = new Move(selPiece, selPiece.getPosition(), curr);
                    if (GameBoard.activeGameBoard.legalMove(possible)) {
                        pvpGameboard[selMoveX][selMoveY].setBackgroundResource(greenHighlight);
                    }
                }
                return;
            }
        } else {
            int tempX = tempPos.getRow();
            int tempY = tempPos.getColumn();
            for (int x = 0; x < selLegal.size(); x++){
                int selX = selLegal.get(x).getTo().getRow();
                int selY = selLegal.get(x).getTo().getColumn();
                if (selX == tempX && selY == tempY){
                    foundMatch = true;
                    break;
                }
            }
            if (!foundMatch){
                selImage.setBackgroundResource(0);
                for (int i = 0; i < selMoves.size(); i++) {
                    int selMoveX = selMoves.get(i).getRow();
                    int selMoveY = selMoves.get(i).getColumn();
                    pvpGameboard[selMoveX][selMoveY].setBackgroundResource(0);
                }
                selImage = null;
                return;
            }
        }
        if (turn == Color.BLACK){
            turn = Color.WHITE;
        } else { turn = Color.BLACK; }

        redoMoves = new ArrayList<>();
        doneWithPrev = false;
        selPiece.setPosition(tempPos);
        boardSetup.put(tempPos, selPiece);
        boardSetup.put(selPosition, null);

        int multiplier = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);

        int moveY = selPosition.getRow() - tempPos.getRow();
        int moveX = tempPos.getColumn() - selPosition.getColumn();

        TranslateAnimation mAnimation = new TranslateAnimation(0, moveX * multiplier, 0, moveY * multiplier);
        mAnimation.setDuration(1000);
        mAnimation.setFillAfter(false);
        selImage.setAnimation(mAnimation);
        selImage.setBackgroundResource(0);


        Move move = new Move(selPiece, selPosition, tempPos);

        if (GameBoard.activeGameBoard.isCastle(move)){
            castle = move;
        } else {
            castle = null;
        }

        move.setPieceCaptured(tempPiece);


        GameBoard.activeGameBoard.move(move);
        GameBoard.activeGameBoard.addMove(move);
        GameBoard.activeGameBoard.switchPlayerToMove();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                selImage.setImageResource(0);
                temp.setImageResource(selPiece.getDrawableId());
                selImage = null;
                doneWithPrev = true;
                if (checkBlack.getDrawable() != null || checkWhite.getDrawable() != null){
                    checkAnimationOut();
                }

                if (GameBoard.activeGameBoard.inCheckMate()){
                    gameOver();
                    return;
                }

                if (GameBoard.activeGameBoard.inCheck()){
                    checkAnimationIn();
                }

                if (castle != null){
                    if (castle.getTo().getColumn() > 4){
                        Position pastRook = new Position(castle.getTo().getRow(),7);
                        Position newRook = new Position(castle.getTo().getRow(),5);
                        Piece rook = boardSetup.get(pastRook);
                        castle.setPieceCaptured(rook);
                        rook.setPosition(newRook);
                        boardSetup.put(pastRook,null);
                        boardSetup.put(newRook,rook);
                        pvpGameboard[castle.getTo().getRow()][7].setImageResource(0);
                        pvpGameboard[castle.getTo().getRow()][5].
                                setImageResource(rook.getDrawableId());
                    } else {
                        Position pastRook = new Position(castle.getTo().getRow(),0);
                        Position newRook = new Position(castle.getTo().getRow(),3);
                        Piece rook = boardSetup.get(pastRook);
                        castle.setPieceCaptured(rook);
                        rook.setPosition(newRook);
                        boardSetup.put(pastRook,null);
                        boardSetup.put(newRook,rook);
                        pvpGameboard[castle.getTo().getRow()][0].setImageResource(0);
                        pvpGameboard[castle.getTo().getRow()][3].
                                setImageResource(rook.getDrawableId());
                    }
                }
                return;

            }
        }, 1000);



        for (int i = 0; i < selMoves.size(); i++) {
            int selMoveX = selMoves.get(i).getRow();
            int selMoveY = selMoves.get(i).getColumn();
            pvpGameboard[selMoveX][selMoveY].setBackgroundResource(0);
        }


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
        undoWhite();
    }

    public void undoWhite(){
        List<Move> history = GameBoard.activeGameBoard.getGameHistory();
        Move lastMove = null;
        Color currTurn = GameBoard.activeGameBoard.playerToMove();

        if (history.isEmpty()){
            Toast.makeText(PVPGameBoard.this, "Sorry, you can't undo a move when one doesn't" +
                    " exist.", Toast.LENGTH_SHORT).show();
            return;
        }
        int last = history.size() - 1;
        Move prev = history.get(last);
        if (prev.getPieceMoved().getColor() == Color.WHITE){
            clearBackgrounds();
            Position fromPos = prev.getFrom();
            Position toPos = prev.getTo();
            ImageView from = pvpGameboard[fromPos.getRow()][fromPos.getColumn()];
            ImageView to = pvpGameboard[toPos.getRow()][toPos.getColumn()];
            Piece moved = prev.getPieceMoved();
            Piece taken = prev.getPieceCaptured();
            from.setImageResource(moved.getDrawableId());
            moved.setPosition(fromPos);
            boardSetup.put(fromPos,moved);
            if (taken != null) {
                to.setImageResource(taken.getDrawableId());
                taken.setPosition(toPos);
                boardSetup.put(toPos, taken);
            }
            else {
                to.setImageResource(0);
                boardSetup.put(toPos,null);
            }
            turn = Color.WHITE;
            GameBoard.activeGameBoard.switchPlayerToMove();
            redoMoves.add(prev);
            history.remove(last);
        } else {
            Toast.makeText(PVPGameBoard.this, "You can't undo your opponents last move.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkBlack.getDrawable() != null || checkWhite.getDrawable() != null){
            checkAnimationOut();
        }

        if (GameBoard.activeGameBoard.inCheck()){
            checkAnimationIn();
        }
    }

    public void pvpundo2(View v){
        undoBlack();
    }

    public void undoBlack(){
        List<Move> history = GameBoard.activeGameBoard.getGameHistory();
        if (history.isEmpty()){
            Toast.makeText(PVPGameBoard.this, "Sorry, you can't undo a move when one doesn't" +
                    " exist.", Toast.LENGTH_SHORT).show();
            return;
        }
        int last = history.size() - 1;
        Move prev = history.get(last);
        if (prev.getPieceMoved().getColor() == Color.BLACK){
            clearBackgrounds();
            Position fromPos = prev.getFrom();
            Position toPos = prev.getTo();
            ImageView from = pvpGameboard[fromPos.getRow()][fromPos.getColumn()];
            ImageView to = pvpGameboard[toPos.getRow()][toPos.getColumn()];
            Piece moved = prev.getPieceMoved();
            Piece taken = prev.getPieceCaptured();
            from.setImageResource(moved.getDrawableId());
            moved.setPosition(fromPos);
            boardSetup.put(fromPos, moved);
            if (taken != null) {
                to.setImageResource(taken.getDrawableId());
                taken.setPosition(toPos);
                boardSetup.put(toPos, taken);
            }
            else {
                to.setImageResource(0);
                boardSetup.put(toPos, null);
            }
            turn = Color.BLACK;
            GameBoard.activeGameBoard.switchPlayerToMove();
            redoMoves.add(prev);
            history.remove(last);
        } else {
            Toast.makeText(PVPGameBoard.this, "You can't undo your opponents last move.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkBlack.getDrawable() != null || checkWhite.getDrawable() != null){
            checkAnimationOut();
        }

        if (GameBoard.activeGameBoard.inCheck()){
            checkAnimationIn();
        }
    }

    public void pvpredo1(View v){

        if (redoMoves.isEmpty()){
            Toast.makeText(PVPGameBoard.this, "Sorry, you can't redo a move when one doesn't" +
                    " exist.", Toast.LENGTH_SHORT).show();
            return;
        }
        int last = redoMoves.size() - 1;
        Move prev = redoMoves.get(last);
        if (prev.getPieceMoved().getColor() == Color.WHITE){
            clearBackgrounds();
            Position fromPos = prev.getFrom();
            Position toPos = prev.getTo();
            ImageView to = pvpGameboard[fromPos.getRow()][fromPos.getColumn()];
            ImageView from = pvpGameboard[toPos.getRow()][toPos.getColumn()];
            Piece moved = prev.getPieceMoved();
            Piece taken = prev.getPieceCaptured();
            from.setImageResource(moved.getDrawableId());
            moved.setPosition(toPos);
            boardSetup.put(toPos,moved);
            to.setImageResource(0);

            boardSetup.put(fromPos,null);
            turn = Color.BLACK;
            GameBoard.activeGameBoard.addMove(prev);
            GameBoard.activeGameBoard.switchPlayerToMove();
            redoMoves.remove(last);
        } else {
            Toast.makeText(PVPGameBoard.this, "You can't redo your opponents last move.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkBlack.getDrawable() != null || checkWhite.getDrawable() != null){
            checkAnimationOut();
        }

        if (GameBoard.activeGameBoard.inCheck()){
            checkAnimationIn();
        }
    }

    public void pvpredo2(View v){
        if (redoMoves.isEmpty()){
            Toast.makeText(PVPGameBoard.this, "Sorry, you can't redo a move when one doesn't" +
                    " exist.", Toast.LENGTH_SHORT).show();
            return;
        }
        int last = redoMoves.size() - 1;
        Move prev = redoMoves.get(last);
        if (prev.getPieceMoved().getColor() == Color.BLACK){
            clearBackgrounds();
            Position fromPos = prev.getFrom();
            Position toPos = prev.getTo();
            ImageView to = pvpGameboard[fromPos.getRow()][fromPos.getColumn()];
            ImageView from = pvpGameboard[toPos.getRow()][toPos.getColumn()];
            Piece moved = prev.getPieceMoved();
            Piece taken = prev.getPieceCaptured();
            from.setImageResource(moved.getDrawableId());
            moved.setPosition(toPos);
            boardSetup.put(toPos,moved);
            to.setImageResource(0);

            boardSetup.put(fromPos,null);
            turn = Color.WHITE;
            GameBoard.activeGameBoard.addMove(prev);
            GameBoard.activeGameBoard.switchPlayerToMove();
            redoMoves.remove(last);
        } else {
            Toast.makeText(PVPGameBoard.this, "You can't redo your opponents last move.",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (checkBlack.getDrawable() != null || checkWhite.getDrawable() != null){
            checkAnimationOut();
        }

        if (GameBoard.activeGameBoard.inCheck()){
            checkAnimationIn();
        }
    }

    public void animate(Position tempPos){
        int multiplier = this.getResources().getDimensionPixelSize(R.dimen.game_board_activity_square_width);

        int moveY = selPosition.getRow() - tempPos.getRow();
        int moveX = tempPos.getColumn() - selPosition.getColumn();

        TranslateAnimation mAnimation = new TranslateAnimation(0, moveX * multiplier, 0, moveY * multiplier);
        mAnimation.setDuration(1000);
        mAnimation.setFillAfter(false);
        selImage.setAnimation(mAnimation);
        selImage.setBackgroundResource(0);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                selImage.setImageResource(0);
                temp.setImageResource(selPiece.getDrawableId());
                selImage = null;
                doneWithPrev = true;
                if (checkBlack.getDrawable() != null || checkWhite.getDrawable() != null){
                    checkAnimationOut();
                }

                if (GameBoard.activeGameBoard.inCheckMate()){
                    gameOver();
                    return;
                }

                if (GameBoard.activeGameBoard.inCheck()){
                    checkAnimationIn();
                }
                return;
            }
        }, 1000);
    }

    public void pvpsuggalt1(View v){
        if (GameBoard.activeGameBoard.inCheck(Color.BLACK,true)){
            Toast.makeText(PVPGameBoard.this, "Yay!! It works!",
                    Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(PVPGameBoard.this, "Damn...",
                    Toast.LENGTH_SHORT).show();
        }
        //Intent openPawnPromotion = new Intent(getApplicationContext(),PawnPromotion.class);
        //startActivity(openPawnPromotion);
        //new UrlConnection().new Request().execute(GameBoard.activeGameBoard.pack());
        //Toast.makeText(PVPGameBoard.this, "Sorry, this function is still being worked on.",
        //        Toast.LENGTH_SHORT).show();
        //return;
    }

    public void pvpsuggalt2(View v){
        new UrlConnection().new Request().execute(GameBoard.activeGameBoard.pack());
        Toast.makeText(PVPGameBoard.this, "Sorry, this function is still being worked on.",
                Toast.LENGTH_SHORT).show();
        return;
    }

    public void pvpstarthelp1(View v){
        new UrlConnection().new Connection().execute("connect");
        Toast.makeText(PVPGameBoard.this, "Sorry, this function is still being worked on.",
                Toast.LENGTH_SHORT).show();

        return;
    }

    public void pvpstarthelp2(View v){
        new UrlConnection().new Connection().execute("connect");
        Toast.makeText(PVPGameBoard.this, "Sorry, this function is still being worked on.",
                Toast.LENGTH_SHORT).show();
        return;
    }

    public void onBackPressed(){
        Intent check = new Intent(getApplicationContext(),LeaveGameVerify.class);
        Bundle verify = new Bundle();
        verify.putString("activity","main");
        check.putExtra("next",verify);
        startActivity(check);
    }

    public void clearBackgrounds(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                selImage = null;
                selPiece = null;
                selPosition = null;
                pvpGameboard[x][y].setBackgroundResource(0);
            }
        }
    }

    public void initializeBoard(){
        pvpGameboard[7][0] = (ImageView) findViewById(R.id.pvpgame00);
        pvpGameboard[7][1] = (ImageView) findViewById(R.id.pvpgame01);
        pvpGameboard[7][2] = (ImageView) findViewById(R.id.pvpgame02);
        pvpGameboard[7][3] = (ImageView) findViewById(R.id.pvpgame03);
        pvpGameboard[7][4] = (ImageView) findViewById(R.id.pvpgame04);
        pvpGameboard[7][5] = (ImageView) findViewById(R.id.pvpgame05);
        pvpGameboard[7][6] = (ImageView) findViewById(R.id.pvpgame06);
        pvpGameboard[7][7] = (ImageView) findViewById(R.id.pvpgame07);

        pvpGameboard[6][0] = (ImageView) findViewById(R.id.pvpgame10);
        pvpGameboard[6][1] = (ImageView) findViewById(R.id.pvpgame11);
        pvpGameboard[6][2] = (ImageView) findViewById(R.id.pvpgame12);
        pvpGameboard[6][3] = (ImageView) findViewById(R.id.pvpgame13);
        pvpGameboard[6][4] = (ImageView) findViewById(R.id.pvpgame14);
        pvpGameboard[6][5] = (ImageView) findViewById(R.id.pvpgame15);
        pvpGameboard[6][6] = (ImageView) findViewById(R.id.pvpgame16);
        pvpGameboard[6][7] = (ImageView) findViewById(R.id.pvpgame17);

        pvpGameboard[5][0] = (ImageView) findViewById(R.id.pvpgame20);
        pvpGameboard[5][1] = (ImageView) findViewById(R.id.pvpgame21);
        pvpGameboard[5][2] = (ImageView) findViewById(R.id.pvpgame22);
        pvpGameboard[5][3] = (ImageView) findViewById(R.id.pvpgame23);
        pvpGameboard[5][4] = (ImageView) findViewById(R.id.pvpgame24);
        pvpGameboard[5][5] = (ImageView) findViewById(R.id.pvpgame25);
        pvpGameboard[5][6] = (ImageView) findViewById(R.id.pvpgame26);
        pvpGameboard[5][7] = (ImageView) findViewById(R.id.pvpgame27);

        pvpGameboard[4][0] = (ImageView) findViewById(R.id.pvpgame30);
        pvpGameboard[4][1] = (ImageView) findViewById(R.id.pvpgame31);
        pvpGameboard[4][2] = (ImageView) findViewById(R.id.pvpgame32);
        pvpGameboard[4][3] = (ImageView) findViewById(R.id.pvpgame33);
        pvpGameboard[4][4] = (ImageView) findViewById(R.id.pvpgame34);
        pvpGameboard[4][5] = (ImageView) findViewById(R.id.pvpgame35);
        pvpGameboard[4][6] = (ImageView) findViewById(R.id.pvpgame36);
        pvpGameboard[4][7] = (ImageView) findViewById(R.id.pvpgame37);

        pvpGameboard[3][0] = (ImageView) findViewById(R.id.pvpgame40);
        pvpGameboard[3][1] = (ImageView) findViewById(R.id.pvpgame41);
        pvpGameboard[3][2] = (ImageView) findViewById(R.id.pvpgame42);
        pvpGameboard[3][3] = (ImageView) findViewById(R.id.pvpgame43);
        pvpGameboard[3][4] = (ImageView) findViewById(R.id.pvpgame44);
        pvpGameboard[3][5] = (ImageView) findViewById(R.id.pvpgame45);
        pvpGameboard[3][6] = (ImageView) findViewById(R.id.pvpgame46);
        pvpGameboard[3][7] = (ImageView) findViewById(R.id.pvpgame47);

        pvpGameboard[2][0] = (ImageView) findViewById(R.id.pvpgame50);
        pvpGameboard[2][1] = (ImageView) findViewById(R.id.pvpgame51);
        pvpGameboard[2][2] = (ImageView) findViewById(R.id.pvpgame52);
        pvpGameboard[2][3] = (ImageView) findViewById(R.id.pvpgame53);
        pvpGameboard[2][4] = (ImageView) findViewById(R.id.pvpgame54);
        pvpGameboard[2][5] = (ImageView) findViewById(R.id.pvpgame55);
        pvpGameboard[2][6] = (ImageView) findViewById(R.id.pvpgame56);
        pvpGameboard[2][7] = (ImageView) findViewById(R.id.pvpgame57);

        pvpGameboard[1][0] = (ImageView) findViewById(R.id.pvpgame60);
        pvpGameboard[1][1] = (ImageView) findViewById(R.id.pvpgame61);
        pvpGameboard[1][2] = (ImageView) findViewById(R.id.pvpgame62);
        pvpGameboard[1][3] = (ImageView) findViewById(R.id.pvpgame63);
        pvpGameboard[1][4] = (ImageView) findViewById(R.id.pvpgame64);
        pvpGameboard[1][5] = (ImageView) findViewById(R.id.pvpgame65);
        pvpGameboard[1][6] = (ImageView) findViewById(R.id.pvpgame66);
        pvpGameboard[1][7] = (ImageView) findViewById(R.id.pvpgame67);

        pvpGameboard[0][0] = (ImageView) findViewById(R.id.pvpgame70);
        pvpGameboard[0][1] = (ImageView) findViewById(R.id.pvpgame71);
        pvpGameboard[0][2] = (ImageView) findViewById(R.id.pvpgame72);
        pvpGameboard[0][3] = (ImageView) findViewById(R.id.pvpgame73);
        pvpGameboard[0][4] = (ImageView) findViewById(R.id.pvpgame74);
        pvpGameboard[0][5] = (ImageView) findViewById(R.id.pvpgame75);
        pvpGameboard[0][6] = (ImageView) findViewById(R.id.pvpgame76);
        pvpGameboard[0][7] = (ImageView) findViewById(R.id.pvpgame77);

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

    public void checkAnimationIn(){
        Color curr = GameBoard.activeGameBoard.playerToMove();
        if (curr == Color.BLACK){
            checkBlack.startAnimation(AnimationUtils.loadAnimation(this, animationFadeIn));

            checkBlack.setImageResource(checkImage);
        } else {
            checkWhite.startAnimation(AnimationUtils.loadAnimation(this, animationFadeIn));
            checkWhite.setImageResource(checkImage);
        }
    }

    public void checkAnimationOut(){
        if (checkBlack.getDrawable() != null){
            checkBlack.startAnimation(AnimationUtils.loadAnimation(this, animationFadeOut));
        } else {
            checkWhite.startAnimation(AnimationUtils.loadAnimation(this, animationFadeOut));
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                checkBlack.setImageResource(0);
                checkWhite.setImageResource(0);
            }
        }, 2000);


    }

    private void gameOver(){
        Intent endGame = new Intent(getApplicationContext(), EndOfGameActivity.class);
        Bundle params = new Bundle();
        Color curr = GameBoard.activeGameBoard.playerToMove;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        if (requestCode == END_OF_GAME_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                String action = data.getStringExtra("Action");
                if (action.equalsIgnoreCase("Undo")) {
                    if (GameBoard.activeGameBoard.playerToMove == Color.WHITE){
                        undoBlack();
                    } else {
                        undoWhite();
                    }
                } else if (action.equalsIgnoreCase("Quit")) {
                    Intent quit = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(quit);
                    finish();
                }
            }
        }
    }
}
