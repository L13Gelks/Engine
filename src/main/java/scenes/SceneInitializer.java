package scenes;

import Game.entity.Player;

public abstract class SceneInitializer {
    Player player = null;
    public  Player getPlayer() {
        return player;
    }
    public abstract void init(Scene scene);
    public abstract void loadResources(Scene scene);
    public abstract void imgui();
}
