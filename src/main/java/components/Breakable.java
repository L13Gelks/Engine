package components;

public class Breakable extends  Block{

    @Override
    void playerHit(PlayerController playerController) {
        if(playerController.isNormal()){
            gameObject.destroy();
        }
    }
}
