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
