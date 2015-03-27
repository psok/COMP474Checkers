package com.checkers.kingme.comp474checkers;

/**
 * Created by dalestoutjr on 2/20/15.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class SquareView extends View {

    public interface OnTouchLister { // interface between view and game
        void OnToggled(SquareView v, boolean touchOn);
    }

    boolean touchOn;
    boolean isHighlighted;
    boolean mDownTouch = false;
    private OnTouchLister touchLister;
    int idX = 0; //default
    int idY = 0; //default
    int squareID = 0; //default
    boolean isBlackPiece;
    boolean isRedPiece;
    boolean isKing;
    int index;

    public SquareView(Context context, int x, int y, int squareId) {
        super(context);
        idX = x;
        idY = y;
        squareID = squareId;

        init();
    }

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

    private void init() { //on creation defaults to false
        touchOn = false;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//  specs imposed SquareView by
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(isHighlighted) {
            canvas.drawColor(Color.YELLOW);
            isHighlighted = !isHighlighted;
        }
        else {

            if (getSquareID() > 0) {
                canvas.drawColor(Color.DKGRAY);

                Rect srcRect = new Rect(0, 0, getWidth(), getHeight());
                Rect dstRect = new Rect(srcRect);

                if (this.isBlackPiece) {
                    Bitmap blackPiece;
                    if (this.isKing) {
                        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
                    } else {
                        blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_piece);
                    }
                    blackPiece = Bitmap.createScaledBitmap(blackPiece, getWidth(), getHeight(), true);
                    canvas.drawBitmap(blackPiece, srcRect, dstRect, null);
                } else if (this.isRedPiece) {
                    Bitmap redPiece;
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
        }
    }

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
    }

    public int getIdX(){ // getter
        return idX;
    }

    public int getIdY(){ // getter
        return idY;
    }

    public int getSquareID(){ // getter
        return squareID;
    }

}
