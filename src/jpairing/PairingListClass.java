/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jpairing;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;

/**
 *
 * @author Shaun
 */
public class PairingListClass {

    int round = 0;
    ArrayList<PairingClass> pairings = new ArrayList<PairingClass>();
    ArrayList vp_total = new ArrayList();
    
    public PairingListClass(int new_round) {
        round = new_round;
    }
    
    void add_pairing(PairingClass pairing) {
        pairings.add(pairing);
    }
    
    void update_totals(int round_no) {
        for (PairingClass round_pairing:pairings) {
            round_pairing.update_total(round_no);
        }
    }
    
    int get_vp_total(int tableNo) {
        int total = 0;
        for (PairingClass data:pairings) {
            if (data.getBoardNo() == tableNo) {
                total += data.getVictory_points();
                
            }
        }
        
        return total;
    }
    
    void update_vp_total(int round, int tableNo) {
        int vpTotal = get_vp_total(tableNo);
        for (PairingClass data:pairings) {
            if (data.getBoardNo() == tableNo) {
                data.getPlayer().set_vp_total_total(round, vpTotal);
            }
        }
    }
    
    void make_vp_totals(int round) {
        // Sure this fails!
        int board = 0;
        vp_total.clear();
        for (PairingClass data:pairings) {
            board = data.getBoardNo();
            update_vp_total(round, board);           
        }
    }
    
    void sort_pairing() {
        Collections.sort(pairings,new byBoard());
    }
    
    public String getPairingsText() {
        String output_string = "";
        sort_pairing();
        int current_board = pairings.get(0).getBoardNo();
        for (PairingClass round_pairing:pairings) {
            if (round_pairing.getBoardNo() != current_board) {
                output_string += "=====================================================\n";
                current_board = round_pairing.getBoardNo();
            }
            output_string += round_pairing.get_string()+"...\t...\n";
        }
        
        return output_string;
    }
    
    class byBoard implements Comparator<PairingClass> {
        @Override
        public int compare(PairingClass pairing1, PairingClass pairing2) {
            if (pairing1.getBoardNo() == 0) {
                return 1;
            }
            if (pairing2.getBoardNo() == 0) {
                return -1;
            }
            if (pairing1.getBoardNo() > pairing2.getBoardNo()) {
                return 1;
            }
            if (pairing1.getBoardNo() < pairing2.getBoardNo()) {
                return -1;
            }
            
            if (pairing1.getCurrent_score() > pairing2.getCurrent_score()) {
                return -1;
            }
            if (pairing1.getCurrent_score() < pairing2.getCurrent_score()) {
                return 1;
            }
            return 0;
        }
    }
    
    
    
    
}
