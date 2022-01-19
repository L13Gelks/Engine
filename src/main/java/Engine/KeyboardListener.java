package Engine;

import java.lang.reflect.Array;
import java.util.Arrays;

import static org.lwjgl.glfw.GLFW.*;

public class KeyboardListener {
    private static KeyboardListener instance;
    private boolean keyPressed[] = new boolean[350];
    private boolean keyBeginPress[] = new boolean[350];

    public static void clear() {
        Arrays.fill(KeyboardListener.get().keyPressed, false);
        Arrays.fill(KeyboardListener.get().keyBeginPress, false);
    }

    private KeyboardListener(){

    }

    public static void endFrame() {
        Arrays.fill(get().keyBeginPress, false);
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
            KeyboardListener.get().keyBeginPress[key] = true;
        }else if(action == GLFW_RELEASE){
            KeyboardListener.get().keyPressed[key] = false;
            KeyboardListener.get().keyBeginPress[key] = false;
        }
    }

    public static  boolean isKeyPressed(int keyCode){
        return KeyboardListener.get().keyPressed[keyCode];
    }

    public static boolean keyBeginPress(int keyCode){
        return get().keyBeginPress[keyCode];
    }
}
