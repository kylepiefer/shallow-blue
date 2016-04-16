package com.shallowblue.shallowblue;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;

public class PawnPromotion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pawn_promotion);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width * 0.7), (int) (height * 0.3));

        ImageButton queen =  (ImageButton) findViewById(R.id.queen);
        ImageButton rook =  (ImageButton) findViewById(R.id.rook);
        ImageButton knight =  (ImageButton) findViewById(R.id.knight);
        ImageButton bishop =  (ImageButton) findViewById(R.id.bishop);

        Color turn = GameBoard.activeGameBoard.playerToMove();
        if (turn == Color.WHITE){
            queen.setImageResource(R.drawable.white_queen);
            rook.setImageResource(R.drawable.white_rook);
            knight.setImageResource(R.drawable.white_knight);
            bishop.setImageResource(R.drawable.white_bishop);
        } else {
            queen.setImageResource(R.drawable.black_queen);
            rook.setImageResource(R.drawable.black_rook);
            knight.setImageResource(R.drawable.black_knight);
            bishop.setImageResource(R.drawable.black_bishop);
        }
    }

    public void queenClicked(View v){
        finish();
    }

    public void rookClicked(View v){
        finish();
    }

    public void knightClicked(View v){
        finish();
    }

    public void bishopClicked(View v){
        finish();
    }
}
