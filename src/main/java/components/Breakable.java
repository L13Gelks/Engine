package components;

import Game.entity.Player;

public class Breakable extends  Block{

    @Override
    void playerHit(Player player) {
        if(player.isNormal()){
            gameObject.destroy();
        }
    }
}
