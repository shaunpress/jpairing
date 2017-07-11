/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 *
 * @author Shaun
 */
public class playerTableModel extends AbstractTableModel {
    private String[] columnNames = {"ID", "Name", "Rating", "DOB"};
    private ArrayList<PlayerClass> data = new ArrayList<PlayerClass>();
    boolean[] canEdit = new boolean [] {
        false, true, true, true
    };
    Class[] types = new Class [] {
        java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.String.class
    };
    
    @Override
    public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit [columnIndex];
    }
    
    @Override
    public int getColumnCount() {
        return columnNames.length;
    }
    
    @Override
    public int getRowCount() {
        return data.size();
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return data.get(row).get_item(col);
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        data.get(row).set_item(col,value);
        fireTableCellUpdated(row,col);
    }
    
    public void resetPlayerList() {
        int rowCount = getRowCount();
        fireTableRowsDeleted(0,rowCount);
        PlayerClass.setNoOfPlayers(0);
        data = new ArrayList<PlayerClass>();
    }
    
    public void addNewPlayer(String newData) {
        String[] fields = newData.split(";");
        PlayerClass newPlayer = new PlayerClass(0);
        newPlayer.setPlayerName(fields[0]);
        newPlayer.setRating(Integer.parseInt(fields[1]));
        int rowCount = getRowCount();
        data.add(newPlayer);
        fireTableRowsInserted(rowCount, rowCount);
    }
    
    public void addPlayerResult(int player_id, int round_no, String[] result) {
        PlayerClass found_player = null;
        
        for (PlayerClass player:data) {
            if (player.getPairingId() == player_id) {
                found_player = player;
                break;
            }
        }
        if (found_player != null) {
            found_player.enterResults(round_no, result);
        }
        
    }
    
    public void addBlankRow() {
        
        int rowCount = getRowCount();
        if ((rowCount == 0) || (null != getValueAt(rowCount-1,1))) {
            PlayerClass newRow = new PlayerClass(0);
            data.add(newRow);
            fireTableRowsInserted(rowCount, rowCount);
        }
    }
    
    
    public void deleteRow(int rowNo) {
        data.remove(rowNo);
        fireTableRowsDeleted(rowNo,rowNo);
    }
    
    public String player_crosstable_list(int round_no) {
        ArrayList<StandingDetail> standing_list = new ArrayList<StandingDetail>();
        String output_string = "";
        
        // todo: Sort arraylist call based on score
        for (PlayerClass player:data ) {
            float player_score[] = player.get_player_score_n(round_no);
            int player_wins = player.get_player_wins_n(round_no);
            
            StandingDetail standing_data = new StandingDetail(player.getPairingId(), player.getPlayerName(), player.getRating(), player_score[0], player_score[1], player_score[2], 0.0f, player_wins );
            
            
            standing_list.add(standing_data);
            
        }
        
        standing_list.sort((o1,o2)->o1.vp_total-o2.vp_total);
        
        return output_string;
    }
    
    public String player_standing_list(int round_no) {
        
        String output_string = "";
        
        for (PlayerClass player:data) {
            float player_percent = 0.0f;
            float player_score[] = player.get_player_score_n(round_no);
            int player_wins = player.get_player_wins_n(round_no);
            
            if (player_score[2] != 0.0f) {
                player_percent = player_score[1]/player_score[2];               
            }
            
            StandingDetail standing_data = new StandingDetail(player.getPairingId(), player.getPlayerName(), player.getRating(), player_score[0], player_score[1], player_score[2], player_percent, player_wins );
            
            
            output_string += player.getPlayerName()+"\t"+player.getRating()+"\t";
            output_string += Float.toString(player_score[0])+"\t"+Float.toString(player_score[1])+"\t";
            output_string += Float.toString(player_percent)+"\t";
            output_string += Integer.toString(player_wins)+"\n";
            
            
        }
        
        return output_string;        
    }
    
    private class StandingDetail {

        public StandingDetail(int player_id, String player_name, int player_rating, float score_total, float vp_total, float vp_total_total, float percent, float wins) {
            this.player_id = player_id;
            this.player_name = player_name;
            this.player_rating = player_rating;
            this.score_total = score_total;
            this.vp_total = vp_total;
            this.vp_total_total = vp_total_total;
            this.percent = percent;
            this.wins = wins;
        }
        int player_id;
        String player_name;
        int player_rating;
        float score_total;
        float vp_total;
        float vp_total_total;
        float percent;
        float wins;
    }
    
    
    
    
}
