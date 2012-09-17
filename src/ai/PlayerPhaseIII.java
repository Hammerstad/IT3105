/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ai;

import poker.Game;

/**
 *
 * @author Nicklas
 */
public class PlayerPhaseIII extends PlayerPhaseII {

    public PlayerPhaseIII() {
        this(PlayerPersonality.getRandom());
    }

    public PlayerPhaseIII(PlayerPersonality personality) {
        super(personality);
        this.name = "Phase III Player "+NO;
    }
    
    

    @Override
    public double bet(Game game, double toCall) {
        return 0;
    }
}
