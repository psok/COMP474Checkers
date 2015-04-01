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

    //boolean isHighlighted;
    private int squareID = 0; //default
    //boolean isBlackPiece;
    //boolean isRedPiece;
    //boolean isKing;
    //int index;

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

    /*
    public SquareView(Context context) {
        super(context);
        init();
    }

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SquareView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    */


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//  specs imposed SquareView by
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        square.draw(getResources(), canvas, getWidth(), getHeight());
        /*
        Rect srcRect = new Rect(0, 0, getWidth(), getHeight());
        Rect dstRect = new Rect(srcRect);
        Bitmap blackPiece;
        Bitmap redPiece;

        if(isHighlighted) {
            canvas.drawColor(Color.YELLOW);

            if (this.isBlackPiece) {
                if (this.isKing) {
                    blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
                } else {
                    blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_piece);
                }
                blackPiece = Bitmap.createScaledBitmap(blackPiece, getWidth(), getHeight(), true);
                canvas.drawBitmap(blackPiece, srcRect, dstRect, null);
            } else if (this.isRedPiece) {
                if (this.isKing) {
                    redPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_king);
                } else {
                    redPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_piece);
                }
                redPiece = Bitmap.createScaledBitmap(redPiece, getWidth(), getHeight(), true);
                canvas.drawBitmap(redPiece, srcRect, dstRect, null);
            }
            isHighlighted = !isHighlighted;
        }
        else {

            if (getSquareID() > 0) {
                canvas.drawColor(Color.DKGRAY);

                if (this.isBlackPiece) {
                    if (this.isKing) {
                        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
                    } else {
                        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_piece);
                    }
                    blackPiece = Bitmap.createScaledBitmap(blackPiece, getWidth(), getHeight(), true);
                    canvas.drawBitmap(blackPiece, srcRect, dstRect, null);
                } else if (this.isRedPiece) {
                    if (this.isKing) {
                        redPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_king);
                    } else {
                        redPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_piece);
                    }
                    redPiece = Bitmap.createScaledBitmap(redPiece, getWidth(), getHeight(), true);
                    canvas.drawBitmap(redPiece, srcRect, dstRect, null);
                } else {
                    canvas.drawColor(Color.DKGRAY);
                }

            } else {
                canvas.drawColor(Color.WHITE);
            }
        }*/
    }



    /*
    @Override
    public boolean onTouchEvent(MotionEvent event) { //event definition; toggles view
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                //touchOn = !touchOn;
                invalidate(); // invalidates the view

                if(touchLister != null){
                    touchLister.OnToggled(this, touchOn);
                }

                mDownTouch = true;
                return true;


            case MotionEvent.ACTION_UP:
               if (mDownTouch) {
                    mDownTouch = false;
                    performClick(); // returns true
                    return true;
                }
        }
        return false;
    }

    @Override
    public boolean performClick() { // click on view
        super.performClick();
        return true;
    }

    public void setOnToggledListener(OnTouchLister listener){ // setter

        touchLister = listener;
    }*/

}
