package scenes;

import Engine.GameObject;
import Game.UI;
import Game.entity.Player;
import components.*;
import util.AssetPool;

public class LevelSceneInitializer extends SceneInitializer {

    public LevelSceneInitializer() {

    }

    @Override
    public void init(Scene scene) {
        GameObject cameraObject = scene.createGameObject("GameCamera");
        cameraObject.addComponent(new GameCamera(scene.camera()));
        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(Player.class) != null) {
                cameraObject.addComponent(new UI(g.getComponent(Player.class)));
                player = g.getComponent(Player.class);
            }else{
                //TODO: ASSERTION
            }
        }

        cameraObject.start();
        scene.addGameObjectToScene(cameraObject);
    }

    @Override
    public void loadResources(Scene scene) {
        AssetPool.getShader("assets/shaders/default.glsl");

 //       AssetPool.addSound("assets/sounds/main-theme-overworld.ogg", true);

        for (GameObject g : scene.getGameObjects()) {
            if (g.getComponent(SpriteRenderer.class) != null) {
                SpriteRenderer spr = g.getComponent(SpriteRenderer.class);
                if (spr.getTexture() != null) {
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if (g.getComponent(StateMachine.class) != null) {
                StateMachine stateMachine = g.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }

    @Override
    public void imGui() {

    }
}
