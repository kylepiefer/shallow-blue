package com.shallowblue.shallowblue;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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
            Intent curr = getIntent();
            Bundle passedBundle = curr.getBundleExtra("Flip");
            if (passedBundle != null){
                int color = passedBundle.getInt("Color");
                if (color == 1){
                    LinearLayout layout = (LinearLayout) findViewById(R.id.pawnPromotionLayout);
                    layout.setRotation(180);
                }
            }
            queen.setImageResource(R.drawable.black_queen);
            rook.setImageResource(R.drawable.black_rook);
            knight.setImageResource(R.drawable.black_knight);
            bishop.setImageResource(R.drawable.black_bishop);
        }
    }

    public void queenClicked(View v){
        Intent result = new Intent();
        result.putExtra("Choice", "Queen");
        setResult(RESULT_OK, result);
        finish();
    }

    public void rookClicked(View v){
        Intent result = new Intent();
        result.putExtra("Choice", "Rook");
        setResult(RESULT_OK, result);
        finish();
    }

    public void knightClicked(View v){
        Intent result = new Intent();
        result.putExtra("Choice", "Knight");
        setResult(RESULT_OK, result);
        finish();
    }

    public void bishopClicked(View v){
        Intent result = new Intent();
        result.putExtra("Choice", "Bishop");
        setResult(RESULT_OK, result);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent result = new Intent();
        result.putExtra("Choice", "Cancel");
        setResult(RESULT_OK, result);
        finish();
    }
}
