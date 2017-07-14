/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;

/**
 *
 * @author Shaun
 */
public class PairingClass {

    public static void setNoOfPairings(int noOfPairings) {
        PairingClass.noOfPairings = noOfPairings;
    }

    public void setBoardNo(int boardNo) {
        this.boardNo = boardNo;
    }
    
    public void setPlayer(PlayerClass player) {
        this.player = player;
    }

    public void setPlayerName(String playerName) {
        this.player.setPlayerName(playerName); 
    }

    public void setCurrent_score(float current_score) {
        this.current_score = current_score;
    }

    public void setMatch_points(float match_points) {
        this.match_points = match_points;
    }

    public void setVictory_points(int victory_points) {
        this.victory_points = victory_points;
    }

    public static int getNoOfPairings() {
        return noOfPairings;
    }

    public int getBoardNo() {
        return boardNo;
    }
    
    public String getPlayerName() {
        return player.getPlayerName();
    }

    public float getCurrent_score() {
        return current_score;
    }

    public float getMatch_points() {
        return match_points;
    }

    public int getVictory_points() {
        return victory_points;
    }
    
    public void update_total(int round_no) {
        float[] scores = player.get_player_score_n(round_no);
        current_score = scores[0];
    }
    
    private static int noOfPairings = 0;
    
    private int boardNo;
    private PlayerClass player;
    private float current_score;
    private float match_points;
    private int victory_points;
    
    public Object get_item(int col) {
        Object retValue = null;
        switch (col) {
            case 0: 
                retValue = getBoardNo();
                break;
            case 1: 
                retValue = getPlayerName();
                break;
            case 2: 
                retValue = getCurrent_score();
                break;
            case 3: 
                retValue = getMatch_points();
                break;
            case 4: 
                retValue = getVictory_points();
                break;
                
        }
        return retValue;
    }
    
    public void set_item(int col, Object value) {
        switch (col) {
            case 0:
                setBoardNo((int)value);
                break;
            case 1:
                setPlayerName((String)value);
                break;
            case 2:
                setCurrent_score((float)value);
                break;
            case 3:
                setMatch_points((float)value);
                break;
            case 4:
                setVictory_points((int)value);
                break;
        }
    }
}
