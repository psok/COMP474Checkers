package com.checkers.kingme.comp474checkers;

/**
 * Created by dalestoutjr on 2/20/15.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyView extends View {

    public interface OnToggledListener { // interface between view and game
        void OnToggled(MyView v, boolean touchOn);
    }

    boolean touchOn;
    boolean mDownTouch = false;
    private OnToggledListener toggledListener;
    int idX = 0; //default
    int idY = 0; //default
    int squareID = 0; //default
    boolean isBlackPiece;
    boolean isRedPiece;

    public MyView(Context context, int x, int y, int squareId) {
        super(context);
        idX = x;
        idY = y;
        squareID = squareId;

        init();
    }

    public MyView(Context context) {
        super(context);
        init();
    }

    public MyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() { //on creation defaults to false
        touchOn = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {//  specs imposed MyView by
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec),
                MeasureSpec.getSize(heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getSquareID()>0) {
            canvas.drawColor(Color.DKGRAY);

            // 02/23/2015 Jessie: place black or red pieces on the board when start the game
            Bitmap blackPiece = BitmapFactory.decodeResource(getResources(), R.drawable.black_piece);
            Bitmap redPiece = BitmapFactory.decodeResource(getResources(), R.drawable.red_piece);

            blackPiece = Bitmap.createScaledBitmap(blackPiece, getWidth(), getHeight(), true);
            redPiece = Bitmap.createScaledBitmap(redPiece, getWidth(), getHeight(), true);

            Rect srcRect = new Rect(0, 0, getWidth(), getHeight());
            Rect dstRect = new Rect(srcRect);

            if(isBlackPiece) {
                canvas.drawBitmap(blackPiece, srcRect, dstRect, null);
            }
            else if(isRedPiece) {
                canvas.drawBitmap(redPiece, srcRect, dstRect, null);
            }
            // Jessie ==END==

        } else {
            canvas.drawColor(Color.WHITE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) { //event definition; toggles view
        super.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                touchOn = !touchOn;
                invalidate(); // invalidates the view

                if(toggledListener != null){
                    toggledListener.OnToggled(this, touchOn);
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

    public void setOnToggledListener(OnToggledListener listener){ // setter

        toggledListener = listener;
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
