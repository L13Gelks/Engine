package Engine;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class KeyboardListener {
    private static KeyboardListener instance;
    private boolean keyPressed[] = new boolean[350];

    private KeyboardListener(){

    }

    public static KeyboardListener get(){
        if(KeyboardListener.instance == null){
            KeyboardListener.instance = new KeyboardListener();
        }
        return KeyboardListener.instance;
    }

    public static void KeyboardCallback(long window, int key, int scancode, int action, int modifiers){
        if(action == GLFW_PRESS){
            KeyboardListener.get().keyPressed[key] = true;
        }else if(action == GLFW_RELEASE){
            KeyboardListener.get().keyPressed[key] = false;
        }
    }

    public static  boolean isKeyPressed(int keyCode){
        return KeyboardListener.get().keyPressed[keyCode];
    }
}
