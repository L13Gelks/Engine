package Engine;

import org.lwjgl.Version;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import util.Time;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private final String windowName;
    private long glfwWindow;

    public float r;
    public float g;
    public float b;
    public float a;

    private static Window window = null;

    private static Scene currentScene;
    //private thus only one instance exists
    private Window(){
        this.height = 760;
        this.width = 1360;
        this.windowName = "Game";
        r = 0.0f;
        g = 0.0f;
        b = 0.0f;
        a = 0.0f;
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                currentScene.init();
                break;
            case 1:
                currentScene = new LevelScene();
                currentScene.init();
                break;
            default:
                assert false : "Unknown scene: " + newScene ;
                break;
        }
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();
        }
        return Window.window;
    }

    public void run(){
        System.out.println(Version.getVersion());
        init();
        loop();
        //Free memory
        glfwFreeCallbacks(glfwWindow);
        glfwDestroyWindow(glfwWindow);
        //Terminate GLFW and free error callback
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    public void init(){
        //Error callback
        GLFWErrorCallback.createPrint(System.err).set();

        //Init GLFW
        if(!glfwInit()){
            throw new IllegalStateException("Unable to initialize GLFW");
        }
        //Configure GLFW
        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
        //Create Window
        glfwWindow = glfwCreateWindow(this.width, this.height, this.windowName, NULL, NULL);
        if(glfwWindow == NULL){
            throw new IllegalStateException("Failed to create GLFW WINDOW");
        }
        //Set callback
        glfwSetCursorPosCallback(glfwWindow, MouseListener::mousePositionCallback);
        glfwSetMouseButtonCallback(glfwWindow, MouseListener::mouseButtonCallback);
        glfwSetScrollCallback(glfwWindow, MouseListener::mouseScrollCallback);
        glfwSetKeyCallback(glfwWindow, KeyboardListener::KeyboardCallback);
        //OpenGL context
        glfwMakeContextCurrent(glfwWindow);
        //Enable V-Sync
        glfwSwapInterval(1);

        //Make window visible
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        //Init Scene
        Window.changeScene(0);
    }


    public void loop(){
        //Init timers
        float beginTime = Time.getTime();
        float endTime = Time.getTime();
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            glClearColor(r, g, b, a);
            glClear(GL_COLOR_BUFFER_BIT);

            if(KeyboardListener.isKeyPressed(GLFW_KEY_SPACE)){
                System.out.println("Space key is pressed");
            }

            //
            if(dt >= 0){
                currentScene.update(dt);
            }
            //

            glfwSwapBuffers(glfwWindow);

            //Calculate delta time
            endTime = Time.getTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
    }
}
