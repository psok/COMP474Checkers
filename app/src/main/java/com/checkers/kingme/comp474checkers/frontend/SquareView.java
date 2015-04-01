package com.checkers.kingme.comp474checkers.frontend;

/**
 * Created by dalestoutjr on 2/20/15.
 */
import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.backend.Piece;
import com.checkers.kingme.comp474checkers.backend.Rank;

public class SquareView extends View {

    public interface OnTapListener { // interface between view and system
        void onTap(int squareId);
    }

    private int squareID = 0; //default
    private Square square;

    public SquareView(Context context, Square sqr, int squareId)
    {
        super(context);
        this.square = sqr;
        squareID = squareId;
    }

    public void setOnTapListener (OnTapListener listener) {
        final OnTapListener finalListener = listener;
        this.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                finalListener.onTap(squareID);
            }
        });
    }

    public void updateSquare(Piece[] board)
    {
        if (squareID > 0) {
            square = square.removeDecorations();

            if (board[squareID - 1] != null) {
                if (board[squareID - 1].getRank() == Rank.KING) {
                    if (board[squareID - 1].getColor() == Color.BLACK) {
                        this.square = new BlackKing(this.square);
                    } else {
                        this.square = new RedKing(this.square);
                    }
                } else {
                    if (board[squareID - 1].getColor() == Color.BLACK) {
                        this.square = new BlackPiece(this.square);
                    } else {
                        this.square = new RedPiece(this.square);
                    }
                }
            }
        }
    }

    public void highlight()
    {
        square = new Highlight(square);
    }

    public void unHighlight()
    {
        square = square.removeDecoration();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//  specs imposed SquareView by
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        square.draw(getResources(), canvas, getWidth(), getHeight());
    }

}
