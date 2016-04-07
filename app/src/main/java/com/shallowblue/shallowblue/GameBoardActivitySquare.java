package com.shallowblue.shallowblue;

import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by Kyle on 4/4/2016.
 */
public class GameBoardActivitySquare {

    // constants
    final public static int SQUARE_IMAGE_LIGHT = 0;
    final public static int SQUARE_IMAGE_DARK = 1;

    // the universal row and column this GBAS represents on the board
    private Position boardPosition;

    // the position on the screen relative to the parent frame
    private Position screenPosition;

    // the parent FrameLayout associated with the GBAS
    private FrameLayout layoutFrame;

    // the image of the tile associated with this GBAS
    private ImageView squareImage;

    // the piece object that exists in this GBAS, if any
    private Piece occupyingPiece;

    // the image associated with the piece in the GBAS, if any
    private ImageView pieceImage;

    // the highlight associated with this GBAS, if any
    private ImageView highlightImage;

    public GameBoardActivitySquare(Position boardPosition, FrameLayout layoutFrame) {
        this.boardPosition = boardPosition;
        this.layoutFrame = layoutFrame;
        this.squareImage = null;
        this.occupyingPiece = null;
        this.pieceImage = null;
        this.highlightImage = null;
    }

    public Position getBoardPosition() { return this.boardPosition; }

    public void setScreenPosition(Position screenPosition) { this.screenPosition = screenPosition; }
    public Position getScreenPosition() { return this.screenPosition; }

    public ImageView getSquareImage() { return this.squareImage; }
    public void setSquareImage(ImageView squareImage) {
        this.squareImage = squareImage;
        this.layoutFrame.addView(this.squareImage);
    }

    public Piece getOccupyingPiece() { return this.occupyingPiece; }
    public ImageView getPieceImage() { return this.pieceImage; }
    public void placePiece(Piece piece, ImageView pieceImage) {
        if (this.occupyingPiece != null) this.removePiece();
        this.occupyingPiece = piece;
        this.pieceImage = pieceImage;
        this.layoutFrame.addView(this.pieceImage);
    }

    public void removePiece() {
        this.occupyingPiece = null;
        if (this.pieceImage != null) {
            this.layoutFrame.removeView(this.pieceImage);
            this.pieceImage = null;
        }
    }

    public ImageView getHighlightImage() { return this.highlightImage; }

    public void addHighlight(ImageView highlight) {
        if (this.highlightImage != null) removeHighlight();
        this.highlightImage = highlight;
        this.layoutFrame.addView(this.highlightImage);
        if (this.pieceImage != null) this.pieceImage.bringToFront();
    }

    public void removeHighlight() {
        if (this.highlightImage != null) {
            this.layoutFrame.removeView(this.highlightImage);
            this.highlightImage = null;
        }
    }
}
