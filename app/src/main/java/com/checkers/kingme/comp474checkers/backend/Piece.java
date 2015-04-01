package com.checkers.kingme.comp474checkers.backend;


public class Piece
{
    private Rank rank;
    private Color color;

    public Color getColor()
    {
        return color;
    }

    public Rank getRank()
    {
        return rank;
    }

    protected void crown()
    {
        rank = Rank.KING;
    }

    protected Piece(Color color)
    {
        this.color = color;
        rank = Rank.CHECKER;
    }
}