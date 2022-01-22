package components;

import Engine.GameObject;
import Engine.KeyboardListener;
import Engine.Window;
import Game.entity.Player;
import editor.PropertiesWindow;
import scenes.LevelEditorSceneInitializer;
import util.Settings;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class KeyControls extends  Component{
    private float debounce = 0.0f;
    private float debounceTime = 0.2f;

    @Override
    public void  editorUpdate(float dt){
        PropertiesWindow propertiesWindow = Window.getImGuiLayer().getPropertiesWindow();
        GameObject activeGameObject = propertiesWindow.getActiveGameObject();
        List<GameObject> activeGameObjects = propertiesWindow.getActiveGameObjects();

        float multiplier = KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT) ? 0.05f : 1.0f;

        debounce -= dt;

        if(KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
                KeyboardListener.keyBeginPress(GLFW_KEY_D) && activeGameObject != null){
            GameObject newObject = activeGameObject.copy();
            Window.getScene().addGameObjectToScene(newObject);
            newObject.transform.position.add(Settings.GRID_WIDTH, 0.0f);
            propertiesWindow.setActiveGameObject(newObject);
            if(newObject.getComponent(StateMachine.class) != null){
                newObject.getComponent(StateMachine.class).refreshTextures();
            }
        } else if(KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) &&
        KeyboardListener.keyBeginPress(GLFW_KEY_D) && activeGameObjects.size() > 1) {
            List<GameObject> gos = new ArrayList<>(activeGameObjects);
            propertiesWindow.clearSelected();

            for (GameObject go : gos) {
                GameObject copy = go.copy();
                Window.getScene().addGameObjectToScene(copy);
                propertiesWindow.addActiveGameObject(go);
                if(copy.getComponent(StateMachine.class) != null){
                    copy.getComponent(StateMachine.class).refreshTextures();
                }
            }
        } else if (KeyboardListener.keyBeginPress(GLFW_KEY_DELETE)){
            for (GameObject go : activeGameObjects) {
                if(go.getComponent(Player.class) != null){
                    LevelEditorSceneInitializer.isPlayerGenerated = false;
                }
                go.destroy();
            }
            propertiesWindow.clearSelected();
        } else if (KeyboardListener.keyBeginPress(GLFW_KEY_PAGE_DOWN) && debounce < 0){
            debounce = debounceTime;
            for(GameObject go : activeGameObjects){
                go.transform.zIndex--;
            }
        } else if (KeyboardListener.keyBeginPress(GLFW_KEY_UP) && debounce < 0) {
            debounce = debounceTime;
            //Gradually increase USEFUL FOR MOUNTAINS, STEPS etc
            float defaultMultiplier = KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) ? multiplier : 0.0f;
            for (GameObject go : activeGameObjects) {
                go.transform.position.y += Settings.GRID_HEIGHT * multiplier;
                multiplier += defaultMultiplier;
            }
        } else if (KeyboardListener.keyBeginPress(GLFW_KEY_DOWN) && debounce < 0){
            debounce = debounceTime;
            //Gradually decrease USEFUL FOR MOUNTAINS, STEPS etc
            float defaultMultiplier = KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_CONTROL) ? multiplier : 0.0f;
            for(GameObject go : activeGameObjects){
                go.transform.position.y -= Settings.GRID_HEIGHT * multiplier;
                multiplier += defaultMultiplier;
            }
        } else if (KeyboardListener.keyBeginPress(GLFW_KEY_LEFT) && debounce < 0){
            debounce = debounceTime;
            for(GameObject go : activeGameObjects){
                go.transform.position.x -= Settings.GRID_WIDTH * multiplier;
            }
        } else if (KeyboardListener.keyBeginPress(GLFW_KEY_RIGHT) && debounce < 0){
            debounce = debounceTime;
            for(GameObject go : activeGameObjects){
                go.transform.position.x += Settings.GRID_WIDTH * multiplier;
            }
        }
    }
}
