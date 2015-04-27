package com.checkers.kingme.comp474checkers.model;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.backend.Piece;

/**
 * Created by Carlos
 */
public interface ViewUpdateListener
{
    public void initialize();

    public void highlight(int squareID);

    public void invalidateView(Piece[] board);

    public boolean changeTurn(Color turn);

    public void win(Color turn);
}
