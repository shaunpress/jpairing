/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
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
        if (null != data) {
            int rowCount = getRowCount();
            if (rowCount > 0)
                fireTableRowsDeleted(0,rowCount);
            PlayerClass.setNoOfPlayers(0);
            data = new ArrayList<PlayerClass>();
        }
    }
    
    public void delete_last_round() {
        for (PlayerClass player:data) {
            player.delete_last_result();
        }
    }
    
    public void add_blank_results(int round_no, PairingListClass pair_list) {
        for (PairingClass pair:pair_list.pairings) {
            PlayerClass player = pair.getPlayer();
            player.createResultEntry(round_no,pair.getBoardNo());
        }
    } 
    
    public ArrayList<PlayerClass> getPlayers()
    {
        return data;
    }
    public PlayerClass get_player(int player_id) {
        return data.get(player_id);
    }
    
    public void addNewPlayer(String newData) {
        String[] fields = newData.split(";");
        PlayerClass newPlayer = new PlayerClass(0);
        newPlayer.setPlayerName(fields[0]);
        newPlayer.setRating(Integer.parseInt(fields[1]));
        if (fields.length > 2) {
            String availability_string = fields[2];
            int total_rounds  = availability_string.length();
            newPlayer.init_availability(total_rounds);           
            for (int i = 0; i < total_rounds; i++) {
                if (availability_string.charAt(i) == '1') {
                    newPlayer.change_availability(i, Boolean.TRUE);
                } else {
                    newPlayer.change_availability(i, Boolean.FALSE);
                }
            }
        }
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
    
    public void update_vp_total_total(int round, int board) {
        // Recalculates the vivtory point totals for a board in the
        // specified round
        
        int vp_total_total = 0;
        for (PlayerClass player:data) {
            if (player.played_on(round,board)) {
                vp_total_total += player.get_vp(round);
            }
        }
        for (PlayerClass player:data) {
            if (player.played_on(round,board)) {
                player.set_vp_total_total(round,vp_total_total);
            }
        }
    }
    
    public void addBlankRow() {
        
        int rowCount = getRowCount();
        if ((rowCount == 0) || (null != getValueAt(rowCount-1,1))) {
            PlayerClass newRow = new PlayerClass(0);
            // Assume no more than 24 rounds!
            newRow.init_availability(24);
            data.add(newRow);
            fireTableRowsInserted(rowCount, rowCount);
        }
    }
    
    
    public void deleteRow(int rowNo) {
        if (rowNo < data.size()) {
            fireTableRowsDeleted(rowNo,rowNo);
            data.remove(rowNo);
            
        }
    }
    
    public String fileOutput() {
        // Player data in string format for saving
        String outputString = "Name;Rating\n";
        for (PlayerClass player:data) {
            outputString += player.getPlayerName()+";"+Integer.toString(player.getRating())+";";
            outputString += player.get_availability_string();
            outputString +="\n";
            // TODO: Availability string
        }
        outputString += "\n";
        
        return outputString;
    }
    
    public String filePairingOutput() {
        String outputString = "Player_id     Rd1_board;Rd1_mp;Rd1_vp; ....\n";
        for (PlayerClass player:data) {
            outputString += Integer.toString(player.getPairingId())+" ";
            outputString += player.getResultsString();
        }
        outputString += "\n";
        
        return outputString;
    }
    
    public String csvOutput(int total_rounds) {
        String outputString = "Pairing ID,Name,Rating";
        for (int i=0; i<total_rounds; i++) {
            outputString += ",Round "+Integer.toString(i+1)+" Table,Score,VP Score";
        }
        outputString += "\n";
        for (PlayerClass player:data) {
            outputString += player.getCSVString();
        }
        
        return outputString;
    }
    
    public String player_crosstable_list(int round_no) {
        ArrayList<StandingDetail> standing_list = new ArrayList<StandingDetail>();
        String output_string = "";
        
        // todo: Sort arraylist call based on score
        for (PlayerClass player:data ) {
            float player_score[] = player.get_player_score_n(round_no);
            int player_wins = player.get_player_wins_n(round_no);
            float vp_percent;
            if (player_score[2] == 0) {
                vp_percent = 0.0f;
            } else {
                vp_percent = 100 * player_score[1] / player_score[2];
            }
            StandingDetail standing_data = new StandingDetail(player, player_score[0], (int)player_score[1], (int)player_score[2], vp_percent, player_wins );
            
            
            standing_list.add(standing_data);
            
        }
        
        Collections.sort(standing_list,new byStanding());
        
        int rank = 1;
        for (StandingDetail standing_info:standing_list) {
            
            output_string += Integer.toString(rank)+"\t"+standing_info.player.getPlayerName()+"\t"+standing_info.player.getRating()+"\t";
            for (int i = 0; i<round_no; i++) {
                output_string += standing_info.player.get_round_result(i);
            }
            output_string += Float.toString(standing_info.score_total)+"\t"+Float.toString(standing_info.vp_total)+"\t";
            output_string += String.format("%.2f",standing_info.percent)+"\t";
            output_string += Integer.toString(standing_info.wins)+"\n";
            rank++;
            
        }
        
        return output_string;
    }
    
    public String player_standing_list(int round_no) {
        ArrayList<StandingDetail> standing_list = new ArrayList<StandingDetail>();
        String output_string = "";
        
        for (PlayerClass player:data) {
            float player_percent = 0.0f;
            float player_score[] = player.get_player_score_n(round_no);
            int player_wins = player.get_player_wins_n(round_no);
            
            if (player_score[2] != 0.0f) {
                player_percent = 100*player_score[1]/player_score[2];               
            }
            
            StandingDetail standing_data = new StandingDetail(player, player_score[0], (int)player_score[1], (int)player_score[2], player_percent, player_wins );
            standing_list.add(standing_data);
        }
        Collections.sort(standing_list,new byStanding());
        
        int rank = 0;
        for (StandingDetail standing_info:standing_list) {
            rank++;
            output_string += Integer.toString(rank)+"\t"+standing_info.player.getPlayerName()+"\t"+standing_info.player.getRating()+"\t";
            output_string += Float.toString(standing_info.score_total)+"\t"+Float.toString(standing_info.vp_total)+"\t";
            output_string += String.format("%.2f",standing_info.percent)+"\t";
            output_string += Integer.toString(standing_info.wins)+"\n";
        }
        
        return output_string;        
    }
    
    private class StandingDetail {

        public StandingDetail(PlayerClass player, float score_total, int vp_total, int vp_total_total, float percent, int wins) {
            this.player= player;
            this.score_total = score_total;
            this.vp_total = vp_total;
            this.vp_total_total = vp_total_total;
            this.percent = percent;
            this.wins = wins;
        }
        PlayerClass player;
        float score_total;
        int vp_total;
        int vp_total_total;
        float percent;
        int wins;
        
    }
    
    // Sort functions
    class byStanding implements Comparator<StandingDetail> {
        @Override
        public int compare(StandingDetail player1, StandingDetail player2) {
            if (player1.score_total > player2.score_total) {
                return -1;
            }
            if (player2.score_total > player1.score_total) {
                return 1;
            }
            if (player1.vp_total > player2.vp_total) {
                return -1;
            }
            if (player2.vp_total > player1.vp_total) {
                return 1;
            }
            
            if (player1.percent > player2.percent) {
                return -1;
            }
            if (player2.percent > player1.percent) {
                return 1;
            }
            

            return 0;
        }
    };
    
    
    
    
}
