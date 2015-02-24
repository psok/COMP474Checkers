package com.checkers.kingme.comp474checkers;

/**
 * Created by alshaymaaalhazzaa on 2/23/15.
 */
public class CheckersSystem {
    private CheckersGame stateOfGame;
    private ComputerPlayer computerPlayer;
    private RemotePlayer remotePlayer;
    private HumanPlayer humanPlayer;



    //default constructor
    public CheckersSystem() {
        stateOfGame = new CheckersGame();
    }

    /**
     * Determine is Computer Player Win
     *
     * @return true if the game has ended and computer player win; and vise versa
     */
    public boolean isComputerPlayerWin() {
        return computerPlayer.getIsWinnerResult(stateOfGame.getBoard());
    }

    /**
     * Determine is Human Player Win
     *
     * @return true if the game has ended and human player win; and vise versa
     */
    public boolean isHumanPlayerWin() {
        return humanPlayer.getIsWinnerResult(stateOfGame.getBoard());
    }

    /**
     * Determine is Remote Player Win
     *
     * @return true if the game has ended and remote player win; and vise versa
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

}

