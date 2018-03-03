/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dugout;

import java.util.ArrayList;

/**
 *
 * @author mpill
 */
public class Team {
    private ArrayList<Player> players;
    
    public Team() {
        players = new ArrayList<>();
    }
    
    public ArrayList<Player> getPlayers() {
        return players;
    }
    
    public void addPlayer(Player player) {
        players.add(player);
    }
}
