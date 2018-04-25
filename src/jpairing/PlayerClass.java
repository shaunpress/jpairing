/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;

import java.util.ArrayList;

/**
 *
 * @author Shaun
 */
public class PlayerClass {
    private static int noOfPlayers = 0;
    
    private int pairingId;
    private String playerName;
    private int rating;
    private ArrayList<ResultClass> results;
    private ArrayList<Boolean> availability;
    
    public PlayerClass(int newID) {
        pairingId = ++noOfPlayers;
        results = new ArrayList<ResultClass>();
        availability = new ArrayList<Boolean>();
    }
    
    public void update_player(String name, int newRating, int rounds) {
        playerName = name;
        rating = newRating;
        init_availability(rounds);
    }
    
    public void delete_last_result() {
        int list_length = results.size();
        if (list_length > 0) {
            results.remove(list_length-1);
        }
    }
    
    public void init_availability(int total_rounds) {
        // Set player availability values
        availability = new ArrayList<Boolean>();
        for (int i=0; i< total_rounds; i++) {
            availability.add(Boolean.TRUE);
        }
    }
    
    public void change_availability(int round, Boolean state) {
        availability.set(round,state);
    }
    
    public int get_availability_size() {
        return availability.size();
    }
    
    public Boolean get_availability(int round) {
        return availability.get(round);
    }
    
    public String get_availability_string() {
        String result = "";
        for (Boolean available:availability) {
            if (available) {
                result += "1";
            } else {
                result += "0";
            }
        }
        return result;
    }
    
    public Object get_item(int col) {
        Object retValue = null;
        switch (col) {
            case 0:
                retValue = getPairingId();
                break;
            case 1:
                retValue = getPlayerName();
                break;
            case 2:
                retValue = getRating();
                break;
            case 3:
                retValue = "";
                break;
             
        }
        
        return retValue;
        
    }
    
    public void set_item(int col, Object value) {
        switch (col) {
            case 0:
                setPairingId((int)value);
                break;
            case 1:
                setPlayerName((String)value);
                break;
            case 2:
                setRating((int)value);
                break;
        }
    }

    public static int getNoOfPlayers() {
        return noOfPlayers;
    }

    public static void setNoOfPlayers(int noOfPlayers) {
        PlayerClass.noOfPlayers = noOfPlayers;
    }

    public int getPairingId() {
        return pairingId;
    }

    public void setPairingId(int pairingId) {
        this.pairingId = pairingId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
    
    public float[] get_player_score_n(int round_no) {
        /* Returns the players score up to round n */
        float score_total = 0.0f;
        float vp_total = 0.0f;
        float vp_total_total = 0.0f;
        
        if (round_no > results.size()) {
            round_no = results.size();
        }
        
        for (int i=0; i<round_no;i++) {
            ResultClass result = results.get(i);
            score_total = score_total + result.score;
            vp_total = vp_total + result.vpScore;
            vp_total_total = vp_total_total + result.vpTotal;
        }
        
        float return_array[] = {score_total, vp_total, vp_total_total};
        return return_array;
    }
        
    public int get_player_wins_n(int round_no) {
        int wins = 0;
        if (round_no > results.size()) {
            round_no = results.size();
        }
        for (int i=0; i<round_no;i++) {
            ResultClass result = results.get(i);
            if (result.score > 3.0f) {
                wins++;
            }
        }
        return wins;
    }
    
    public boolean played_on(int round, int board) {
        for (ResultClass result:results) {
            if (result.getRoundNo() == round && result.getTableNo() == board) {
                return true;
            }
        }       
        return false;
    }
    
    public float get_vp(int round) {
        ResultClass result = results.get(round);
        return result.getVpScore();
    }
    
    public void set_vp_total_total(int round, float vp_total) {
        if (round >= results.size())
            return;
        ResultClass result = results.get(round);
        result.setVpTotal(vp_total);
    }
    
    public String get_round_result(int round_no) {
        String output_string = "";
        if (results.size() == 0)
            return output_string;
        
        ResultClass result = results.get(round_no); 
        
        output_string += Integer.toString(result.tableNo)+" ";
        output_string += Float.toString(result.score)+"-"+Float.toString(result.vpScore)+" ";
        output_string += Float.toString(result.vpTotal)+"\t";
        
        
        return output_string;
    }
    
    public String getResultsString() {
        // Returns all the results for a player as an output string
        String outputString = "";
        for (ResultClass result:results) {
            outputString += result.tableNo+";";
            outputString += result.score+";";
            outputString += result.vpScore+" ";
        }
        outputString += "\n";
        return outputString;
    }
    
    public String getCSVString() {
        String outputString = Integer.toString(this.pairingId)+","+this.playerName+","+Integer.toString(this.rating);
        for (ResultClass result:results) {
            outputString += ","+result.tableNo+","+result.score+","+result.vpScore;
        }
        outputString +="\n";
        return outputString;
    }
    
    public void createResultEntry(int round_no, int board_no) {
        if (round_no > results.size()) {
            // Only do this if round is a new one
            ResultClass newResult = new ResultClass();
            newResult.setRoundNo(round_no);
            newResult.setTableNo(board_no);
            
            results.add(newResult);
            
        }
        
    }
    
    public void enterResults(int round_no, String[] score_info) {
        
        if (round_no > results.size()) {
            results.add(new ResultClass());   
        }
        String score_data[] = {Integer.toString(round_no), score_info[0], score_info[1], score_info[2], "0"};
        ResultClass current_round = results.get(round_no-1);
        current_round.UpdateScores(score_data);
    }
    
    public void updateResults(int round_no, int table, float matchPoints, float victoryPoints) {
        if (round_no >= results.size())
            results.add(new ResultClass());
        ResultClass result = results.get(round_no);
        result.setRoundNo(round_no);
        result.setTableNo(table);
        result.setScore(matchPoints);
        result.setVpScore(victoryPoints);
    }
    private class ResultClass {
        // Inner class that contains round by round results
        int roundNo;
        int tableNo;
        float score;
        float vpScore;
        float vpTotal;
        
        public ResultClass() {
            roundNo = 0;
            tableNo = 0;
            score = 0.0f;
            vpScore = 0.0f;
            vpTotal = 0.0f;
        }

        public int getRoundNo() {
            return roundNo;
        }

        public void setRoundNo(int roundNo) {
            this.roundNo = roundNo;
        }

        public int getTableNo() {
            return tableNo;
        }

        public void setTableNo(int tableNo) {
            this.tableNo = tableNo;
        }

        public float getScore() {
            return score;
        }

        public void setScore(float score) {
            this.score = score;
        }

        public float getVpScore() {
            return vpScore;
        }

        public void setVpScore(float vpScore) {
            this.vpScore = vpScore;
        }

        public float getVpTotal() {
            return vpTotal;
        }

        public void setVpTotal(float vpTotal) {
            this.vpTotal = vpTotal;
        }
        
        public void UpdateScores(String[] score_info) {
            roundNo = Integer.parseInt(score_info[0]);
            if ("X".equals(score_info[1])) {
                // 'X' means not paired
                tableNo = 0;
            } else {
                tableNo = Integer.parseInt(score_info[1]);
            }
            score = Float.parseFloat(score_info[2]);
            vpScore = Float.parseFloat(score_info[3]);
            // vpTotal = Float.parseFloat(score_info[4]);
            
        }
    }
}
