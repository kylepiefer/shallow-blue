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
    private ImageView image;
    private ImageView outline;
    private Piece occupyingPiece;

    public GameBoardActivitySquare(Position position, ImageView image) {
        this.boardPosition = position;
        this.image = image;
        this.outline = null;
        this.screenPosition = null;
        this.occupyingPiece = null;
    }

    public Position getBoardPosition() { return this.boardPosition; }
    public void setBoardPosition(Position position) { this.boardPosition = position; }

    public Position getScreenPosition() { return this.screenPosition; }
    public void setScreenPosition(Position position) { this.screenPosition = position; }

    public ImageView getImage() { return this.image; }
    public void setImage(ImageView image) { this.image = image; }

    public ImageView getOutline() { return this.outline; }
    public void setOutline(ImageView outline) { this.outline = outline; }
    public void clearOutline() { this.outline = null; }

    public Piece getOccupyingPiece() { return this.occupyingPiece; }
    public void setOccupyingPiece(Piece piece) { this.occupyingPiece = piece; }
    public void clearOccupyingPiece() { this.setOccupyingPiece(null); }
}
