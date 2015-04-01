package com.checkers.kingme.comp474checkers.frontend;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;

import com.checkers.kingme.comp474checkers.R;

/**
 * Created by Carlos
 */
interface Square
{
    public void draw(Resources res, Canvas canvas, int width, int height);

    public Square removeDecoration();

    public Square removeDecorations();
}

// 3. "Core" class with "is a" relationship
class LightSquare implements Square
{
    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        canvas.drawColor(Color.WHITE);
    }

    public Square removeDecoration()
    {
        return this;
    }

    public Square removeDecorations()
    {
        return this;
    }
}

class DarkSquare implements Square
{
    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        canvas.drawColor(Color.DKGRAY);
    }

    public Square removeDecoration()
    {
        return this;
    }

    public Square removeDecorations()
    {
        return this;
    }
}

abstract class SomethingOnSquare implements Square
{
    private Square sqr;

    protected void resourceToBitmapOnCanvas(Resources res, int resource, int width, int height, Canvas canvas)
    {
        canvas.drawBitmap(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(res, resource), width, height, true),
        new Rect(0, 0, width, height),
                new Rect(0, 0, width, height),
                null);
    }

    public SomethingOnSquare(Square s)
    {
        sqr = s;
    }

    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        sqr.draw(res, canvas, width, height);
    }

    public Square removeDecoration()
    {
        return sqr;
    }

    public Square removeDecorations()
    {
        return sqr.removeDecorations();
    }
}

class Highlight extends SomethingOnSquare
{
    public Highlight(Square s)
    {
        super(s);
    }

    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        super.draw(res, canvas, width, height);
        canvas.drawColor(Color.YELLOW - 0xAA000000); // transparent!
    }
}

class BlackPiece extends SomethingOnSquare {
    public BlackPiece(Square s)
    {
        super(s);
    }

    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        super.draw(res, canvas, width, height);
        resourceToBitmapOnCanvas(res, R.drawable.black_piece, width, height, canvas);
    }
}

class BlackKing extends SomethingOnSquare {
    public BlackKing(Square s)
    {
        super(s);
    }

    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        super.draw(res, canvas, width, height);
        resourceToBitmapOnCanvas(res, R.drawable.black_king, width, height, canvas);
    }
}

class RedPiece extends SomethingOnSquare {
    public RedPiece(Square s)
    {
        super(s);
    }

    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        super.draw(res, canvas, width, height);
        resourceToBitmapOnCanvas(res, R.drawable.red_piece, width, height, canvas);
    }
}

class RedKing extends SomethingOnSquare {
    public RedKing(Square s)
    {
        super(s);
    }

    public void draw(Resources res, Canvas canvas, int width, int height)
    {
        super.draw(res, canvas, width, height);
        resourceToBitmapOnCanvas(res, R.drawable.red_king, width, height, canvas);
    }
}
