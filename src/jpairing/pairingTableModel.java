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
public class pairingTableModel extends AbstractTableModel {
    private String[] columnNames = {"Board", "Player", "Score", "Match Points", "Victory Points"};
    private ArrayList<PairingListClass> round_pairings = new ArrayList<PairingListClass>();
    int current_round = 0;
    boolean[] canEdit = new boolean [] {
        false, false, false, true, true
    };
    
    Class[] types = new Class [] {
        java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Float.class, java.lang.Integer.class
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
        if (round_pairings.size() == 0) {
            return 0;
        }
        return round_pairings.get(current_round).pairings.size();
    }
    
    @Override
    public String getColumnName(int col) {
        return columnNames[col];
    }

    @Override
    public Object getValueAt(int row, int col) {
        return round_pairings.get(current_round).pairings.get(row).get_item(col);
    }
    
    @Override
    public void setValueAt(Object value, int row, int col) {
        round_pairings.get(current_round).pairings.get(row).set_item(col,value);
        fireTableCellUpdated(row,col);
    }
    
    public void setCurrentRound(int current_round) {
        this.current_round = current_round;
    }
    
    public int getRounds() {
        return round_pairings.size();
    }
    
    public void update_display(int round_no) {
        if (round_no == 0) {
            return;
        }
        setCurrentRound(round_no-1);
        round_pairings.get(round_no-1).update_totals(round_no-1);
        round_pairings.get(round_no-1).sort_pairing();        
        int no_of_pairings = round_pairings.get(round_no-1).pairings.size();
        fireTableRowsInserted(0, no_of_pairings);
    }
    
    public void loadPairing(PlayerClass player, int round_no, String[] round_data) {
        PairingClass pairing = new PairingClass();
        
        if (round_no > round_pairings.size()) {
            round_pairings.add(new PairingListClass(round_no));
            System.out.println("New round created:"+round_no);
        }
        if ("X".equals(round_data[0])) {
            pairing.setBoardNo(0); 
        } else {
            pairing.setBoardNo(Integer.parseInt(round_data[0]));
        }
        pairing.setPlayer(player);
        pairing.setMatch_points(Float.parseFloat(round_data[1]));
        pairing.setVictory_points(Integer.parseInt(round_data[2]));
        
        PairingListClass current_round_list = round_pairings.get(round_no-1);
        int rowCount = getRowCount();
        current_round_list.add_pairing(pairing);
        fireTableRowsInserted(rowCount, rowCount);
        
    }
    public void addRow() {
        
        
    }
    
}
