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
    
    void sort_pairing() {
        Collections.sort(pairings,new byBoard());
    }
    
    class byBoard implements Comparator<PairingClass> {
        @Override
        public int compare(PairingClass pairing1, PairingClass pairing2) {
            if (pairing1.getBoardNo() == 0) {
                return 1;
            }
            if (pairing2.getBoardNo() == 0) {
                return 1;
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
