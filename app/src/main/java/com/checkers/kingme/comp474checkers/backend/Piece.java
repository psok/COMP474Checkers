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

    protected void checker() {
        rank = Rank.CHECKER;
    }

    protected void setColor(Color color) {
        this.color = color;
    }

    protected void setRank(Rank rank) {
        this.rank = rank;
    }

    protected Piece(Color color)
    {
        this.color = color;
        rank = Rank.CHECKER;
    }

    protected Piece(Color color, Rank rank)
    {
        this.color = color;
        this.rank = rank;
    }
}