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
public class SimplePairingClass {
    private ArrayList<PlayerClass> player_list;
    private int roundNo;

    public SimplePairingClass(ArrayList<PlayerClass> player_list, int roundNo) {
        this.player_list = player_list;
        this.roundNo = roundNo;
    }
    
    
    int availableCount() {
        int total = 0;
        for (PlayerClass player:player_list) {
            if (player.get_availability(roundNo)) {
                total += 1;
            }
        }
        return total;
    }
    
    PairingListClass make_pairings() {
        
        if (roundNo == 1) {
            return make_first_round_pairings();
        }
        PairingListClass pairing_list = new PairingListClass(roundNo);
        int total_players = availableCount();
        int threePlayerBoards = 4-(total_players%4);
        if (threePlayerBoards == 4) {
            threePlayerBoards = 0;
        }
        int fourPlayerBoards = ((total_players+3) / 4) - threePlayerBoards;
        
        //Sort player list here
        ArrayList<PairingData> sorted_players = new ArrayList<PairingData>();
        for (PlayerClass player:player_list) {
            float player_percent = 0.0f;
            float player_score[] = player.get_player_score_n(roundNo-1);
            
            if (player_score[2] != 0.0f) {
                player_percent = 100*player_score[1]/player_score[2];
            }
            PairingData player_data = new PairingData(player,player_score[0],(int)player_score[1],player_percent);
            sorted_players.add(player_data);
        }
        Collections.sort(sorted_players,new byStanding());
        
        int pair_count = 1;
        int boardNo = 1;
        int player_limit = 3;
        if (fourPlayerBoards > 0) {
            player_limit = 4;
        }
        for (PairingData player:sorted_players) {
            if (player.player.get_availability(roundNo)) {
                pairing_list.add_pairing(new PairingClass(boardNo,player.player,player.player.get_player_score_n(roundNo)[0],0,0));
                pair_count += 1;
                if (pair_count > player_limit) {
                    boardNo += 1;
                    if (boardNo > fourPlayerBoards) {
                        player_limit += 3;
                    } else {
                        player_limit += 4;
                    }
                }
            } else {
                pairing_list.add_pairing(new PairingClass(0,player.player,player.player.get_player_score_n(roundNo)[0],0,0));
            }
            
        }
        
        return pairing_list;
    }
    
    PairingListClass make_first_round_pairings() {
        PairingListClass pairing_list = new PairingListClass(roundNo);
        int total_players = availableCount();
        int threePlayerBoards = 4-(total_players%4);
        if (threePlayerBoards == 4) {
            threePlayerBoards = 0;
        }
        int fourPlayerBoards = ((total_players+3) / 4) - threePlayerBoards;

        //Sort here
        int totalBoards = fourPlayerBoards+threePlayerBoards;
        int pairCount = 0;
        for (PlayerClass player:player_list) {
            if (player.get_availability(roundNo)) {
                int boardNo = (pairCount%totalBoards)+1;
                pairing_list.add_pairing(new PairingClass(boardNo,player,player.get_player_score_n(roundNo)[0],0,0));
                pairCount +=1;
            } else {
                pairing_list.add_pairing(new PairingClass(0,player,player.get_player_score_n(roundNo)[0],0,0));
            }
        }


        return pairing_list;
    }    
    class PairingData {
        PlayerClass player;
        float match_points;
        int vp_total;
        float vp_percent;
    
        public PairingData(PlayerClass player, float mp, int vp, float vpp) {
            this.player = player;
            this.match_points = mp;
            this.vp_total = vp;
            this.vp_percent = vpp;
        }
        
        
    }
    
    class byStanding implements Comparator<PairingData> {
        @Override
        public int compare(PairingData player1, PairingData player2) {
            if (player1.match_points > player2.match_points) {
                return -1;
            }
            if (player2.match_points > player1.match_points) {
                return 1;
            }
            if (player1.vp_total > player2.vp_total) {
                return -1;
            }
            if (player2.vp_total > player1.vp_total) {
                return 1;
            }
            if (player1.vp_percent > player2.vp_percent) {
                return -1;
            }
            if (player2.vp_percent > player1.vp_percent) {
                return 1;
            }
            

            return 0;
        }
    };
    
    class byRating implements Comparator<PlayerClass> {
        @Override
        public int compare(PlayerClass player1, PlayerClass player2) {
            
            if (player1.getRating() > player2.getRating()) {
                return -1;
            }
            
            if (player2.getRating() > player1.getRating()) {
                return 1;
            }
            
            return player1.getPlayerName().compareTo(player2.getPlayerName());
            
        }
    }
        


}
