package com.shallowblue.shallowblue;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
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
import android.widget.TextView;



//apache connection
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
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
        String gameType = getIntent().getStringExtra("Type");
        if (gameType != null && gameType.equalsIgnoreCase("Custom"));
            GameBoard.gameBoard = null;
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
                    screenRow = imageWidth * r;
                    screenColumn = imageWidth * c;

                } else {
                    screenRow = 7 * imageWidth - imageWidth * r;
                    screenColumn = 7 * imageWidth - imageWidth * c;
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
        gbas.setOccupyingPiece(piece);
        gbas.setPieceImage(pieceImage);
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

    public void updateGameboard(Move move) {
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

        // make sure the move is valid
        Piece piece = from.getOccupyingPiece();
        final ImageView pieceImage = from.getPieceImage();
        if (piece == null || pieceImage == null) return;

        // handle captures after animation
        final Piece takenPiece = to.getOccupyingPiece();
        final ImageView takenPieceImage = to.getPieceImage();
        final RelativeLayout container = (RelativeLayout)this.findViewById(R.id.piece_container);

        // swap pieces
        to.setOccupyingPiece(piece);
        from.clearOccupyingPiece();
        to.setPieceImage(pieceImage);
        from.clearPieceImage();

        final int deltaX = to.getScreenPosition().getColumn() - from.getScreenPosition().getColumn();
        final int deltaY = to.getScreenPosition().getRow() - from.getScreenPosition().getRow();
        long distance = (long)Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        TranslateAnimation animation =
                new TranslateAnimation(Animation.ABSOLUTE, (float)0,
                        Animation.ABSOLUTE, (float)deltaX,
                        Animation.ABSOLUTE, (float)0,
                        Animation.ABSOLUTE, (float)deltaY);
        animation.setDuration(distance);
        animation.setZAdjustment(Animation.ZORDER_TOP);
        pieceImage.bringToFront();
        animation.setFillAfter(true);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}
            @Override
            public void onAnimationRepeat(Animation animation) {}
            @Override
            public void onAnimationEnd(Animation animation) {
                RelativeLayout.LayoutParams layoutParams =
                        (RelativeLayout.LayoutParams)pieceImage.getLayoutParams();
                layoutParams.topMargin = layoutParams.topMargin + deltaY;
                layoutParams.leftMargin = layoutParams.leftMargin + deltaX;
                pieceImage.clearAnimation();
                pieceImage.setLayoutParams(layoutParams);

                if (takenPieceImage != null) container.removeView(takenPieceImage);
            }

        });
        pieceImage.startAnimation(animation);
    }

    public void onBoardTouched(View squareImage) {
        GameBoardActivitySquare gbas = (GameBoardActivitySquare)squareImage.getTag();

        if (this.selectedSquare == gbas) {
            for (int i = this.outlinedSquares.size() - 1; i >= 0; i--) {
                removeBoardSquareOutline(this.outlinedSquares.get(i));
            }
            this.selectedSquare = null;
        } else if (this.outlinedSquares.contains(gbas)){
            Move move = new Move(this.selectedSquare.getOccupyingPiece(),
                                this.selectedSquare.getBoardPosition(),
                                gbas.getBoardPosition());
            if (this.gameBoard.move(move)) {
                this.updateGameboard(move);

                for (int i = this.outlinedSquares.size() - 1; i >= 0; i--) {
                    removeBoardSquareOutline(this.outlinedSquares.get(i));
                }
                this.selectedSquare = null;
            }
            else Log.d("ShallowBlue", "Move Failed.");
        } else {
            for (int i = this.outlinedSquares.size() - 1; i >= 0; i--) {
                removeBoardSquareOutline(this.outlinedSquares.get(i));
            }
            drawBoardSquareOutline(gbas);
            this.selectedSquare = gbas;

            if (gbas.getOccupyingPiece() != null) {
                Piece piece = gbas.getOccupyingPiece();

                ArrayList<Position> possibleMoves = piece.possibleMoves();
                for (Position p : possibleMoves) {
                    GameBoardActivitySquare possibleSquare =
                            this.gameBoardActivitySquares[p.getRow()][p.getColumn()];
                    drawBoardSquareOutline(possibleSquare);
                }
            }
        }
    }

    public void startingHelper(View v){
        httpStore hS = new httpStore();
        hS.execute();
    }

    public void altMove(View v){
        httpQuery hQ = new httpQuery();
        hQ.execute();
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

    protected class httpStore extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strs) {
            String reply = null;
            String temp1="";
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost storeVal = new HttpPost("http://shallowBlue");
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("tag", "shallowBlue"));
            nameValuePairs.add(new BasicNameValuePair("GameBoard", gameBoard.pack()));

            try {
                UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs);
                storeVal.setEntity(httpEntity);
                HttpResponse response = httpclient.execute(storeVal);
                temp1 = EntityUtils.toString(response.getEntity());
            }
            catch (Exception e) {
                System.out.println("HTTP IO Exception.");
                e.printStackTrace();
            }

            try {
                JSONArray jsonArray = new JSONArray(temp1);
                reply = jsonArray.getString(0);
            } catch (JSONException e) {
                System.out.println("Error in JSON decoding");
                e.printStackTrace();
            }
            return reply;
        }

        @Override
        protected void onPostExecute(String res) {
            System.out.println("connected");
        }
    }

    protected class httpQuery extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... strs)
        {
            HttpClient client=new DefaultHttpClient();
            HttpPost val=new HttpPost("http://shallowBlue");
            String returnVal="";
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
            nameValuePairs.add(new BasicNameValuePair("tag","shallowBlue"));
            try
            {
                UrlEncodedFormEntity httpEntity = new UrlEncodedFormEntity(nameValuePairs);
                val.setEntity(httpEntity);
                HttpResponse response = client.execute(val);
                String temp = EntityUtils.toString(response.getEntity());
                JSONArray jsonArray = new JSONArray(temp);
                returnVal=jsonArray.getString(2);

            }catch(Exception e){
                e.printStackTrace();
            }
            return returnVal;
        }
        @Override
        protected void onPostExecute(String res)
        {
            System.out.println("get");
        }
    }


}
