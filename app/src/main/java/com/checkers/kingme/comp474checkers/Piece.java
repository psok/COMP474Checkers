package com.checkers.kingme.comp474checkers;


/**
 * Created by Richa on 2/21/2015.
 */
public class Piece {

    private Color color;
    private Rank rank;

    public Piece(Color color, Rank rank){
        this.color = color;
        this.rank = rank;
    }
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Rank getRank() {
        return rank;
    }

    public void setRank(Rank rank) {
        this.rank = rank;
    }

    public void Crown(){
        this.rank = rank.king;
    }
}
