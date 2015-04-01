package com.checkers.kingme.comp474checkers.model;

import com.checkers.kingme.comp474checkers.backend.Color;
import com.checkers.kingme.comp474checkers.backend.Piece;

public interface GameListener
{
    public void onBegin(Piece[] board);

    public void onPick(int from);

    public void onMove(Piece[] board);

    public void onDefeat(Color turn);

    public void onNewTurn(Color turn);
}
