package com.checkers.kingme.comp474checkers;

/**
 * Created by alshaymaaalhazzaa on 2/23/15.
 */
public class CheckersSystem {
    private CheckersGame stateOfGame;
    private ComputerPlayer computerPlayer;
    private RemotePlayer remotePlayer;
    private HumanPlayer humanPlayer;
    //private CurrentBoard board;


    //Default constructor
    public CheckersSystem() {
        stateOfGame = new CheckersGame();
    }

    /**
     * Determine is Computer Player Win
     *
     * @return true if the game has ended and computer player win
     */
    public boolean isComputerPlayerWin() {
        return computerPlayer.getIsWinnerResult(stateOfGame.getBoard());
    }

    /**
     * Determine is Human Player Win
     *
     * @return true if the game has ended and human player win
     */
    public boolean isHumanPlayerWin() {
        return humanPlayer.getIsWinnerResult(stateOfGame.getBoard());
    }

    /**
     * Determine is Remote Player Win
     *
     * @return true if the game has ended and remote player win
     */
    public boolean isRemotePlayerWin() {
        return remotePlayer.getIsWinnerResult(stateOfGame.getBoard());
    }


    public void setComputerPlayer(ComputerPlayer computerPlayer) {
        this.computerPlayer = computerPlayer;
    }


    public RemotePlayer getRemotePlayer() {
        return remotePlayer;
    }

    public void setRemotePlayer(RemotePlayer remotePlayer) {
        this.remotePlayer = remotePlayer;
    }

    public HumanPlayer getHumanPlayer() {
        return humanPlayer;
    }

    public void setHumanPlayer(HumanPlayer humanPlayer) {
        this.humanPlayer = humanPlayer;
    }

    /**
     * Human player perform moving from a square to a square
     *
     * @param fromSquare a square id that the player is moving from
     * @param toSquare   a square id that the player is moving to
     * @return true if the player can move from the square to the square
     */
    public boolean move(int fromSquare, int toSquare) {
        if (!isGameEnds()) {
            if (stateOfGame.pickUp(fromSquare)) {
                stateOfGame.moveTo(toSquare);
            }
            return true;
        }
        return false;
    }

    /**
     * Determine the game has ended or not
     *
     * @return true if the game has ended
     */
    public boolean isGameEnds() {
        return (!isHumanPlayerWin());


    }
    /**
     *
     *
     * @return  the current board
     */
    public CurrentBoard getTheBoard() {
        return stateOfGame.getBoard();
    }
}

