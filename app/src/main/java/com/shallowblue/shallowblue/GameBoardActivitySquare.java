package com.shallowblue.shallowblue;

import android.graphics.Point;
import android.graphics.Rect;
import android.media.Image;
import android.widget.ImageView;

/**
 * Created by Kyle on 4/4/2016.
 */
public class GameBoardActivitySquare {
    private Position boardPosition;
    private Position screenPosition;
    private ImageView squareImage;
    private ImageView outline;
    private Piece occupyingPiece;
    private ImageView pieceImage;

    public GameBoardActivitySquare(Position position, ImageView squareImage) {
        this.boardPosition = position;
        this.squareImage = squareImage;
        this.outline = null;
        this.screenPosition = null;
        this.occupyingPiece = null;
        this.pieceImage = null;
    }

    public Position getBoardPosition() { return this.boardPosition; }
    public void setBoardPosition(Position position) { this.boardPosition = position; }

    public Position getScreenPosition() { return this.screenPosition; }
    public void setScreenPosition(Position position) { this.screenPosition = position; }

    public ImageView getSquareImage() { return this.squareImage; }
    public void setSquareImage(ImageView squareImage) { this.squareImage = squareImage; }

    public ImageView getOutline() { return this.outline; }
    public void setOutline(ImageView outline) { this.outline = outline; }
    public void clearOutline() { this.outline = null; }

    public Piece getOccupyingPiece() { return this.occupyingPiece; }
    public void setOccupyingPiece(Piece piece) { this.occupyingPiece = piece; }
    public void clearOccupyingPiece() { this.occupyingPiece = null; }

    public ImageView getPieceImage() { return this.pieceImage; }
    public void setPieceImage(ImageView pieceImage) { this.pieceImage = pieceImage; }
    public void clearPieceImage() { this.pieceImage = null; }
}
