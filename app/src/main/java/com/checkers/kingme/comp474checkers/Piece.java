package com.checkers.kingme.comp474checkers;


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

    public void crown()
    {
        rank = Rank.KING;
    }

    public Piece(Color color)
    {
        this.color = color;
        rank = Rank.CHECKER;
    }
}