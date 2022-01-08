package Engine;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;
import renderer.DebugDraw;
import scenes.LevelEditorScene;
import scenes.LevelScene;
import scenes.Scene;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    private int width, height;
    private final String windowName;
    private long glfwWindow;

    private static Window window = null;

    private static Scene currentScene;

    private ImGuiLayer imGuiLayer;
    //private thus only one instance exists
    private Window(){
        this.height = 760;
        this.width = 1360;
        this.windowName = "Game";
    }

    public static void changeScene(int newScene){
        switch (newScene){
            case 0:
                currentScene = new LevelEditorScene();
                break;
            case 1:
                currentScene = new LevelScene();
                break;
            default:
                assert false : "Unknown scene: " + newScene ;
                break;
        }
        currentScene.load();
        currentScene.init();
        currentScene.start();
    }

    public static Window get(){
        if(Window.window == null){
            Window.window = new Window();

        }
        return Window.window;
    }

    public static Scene getScene(){
        return get().currentScene;
    }

    public void run(){
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
        //glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);
        //glfwWindowHint(GLFW_MAXIMIZED, GLFW_TRUE);
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
        glfwSetWindowSizeCallback(glfwWindow, (w, newWidth, newHeight) -> {
            Window.setWidth(newWidth);
            Window.setHeight(newHeight);
        });
        //OpenGL context
        glfwMakeContextCurrent(glfwWindow);
        //Enable V-Sync
        glfwSwapInterval(1);

        //Make window visible
        glfwShowWindow(glfwWindow);
        GL.createCapabilities();

        glEnable(GL_BLEND);
        glBlendFunc(GL_ONE, GL_ONE_MINUS_SRC_ALPHA);
        this.imGuiLayer = new ImGuiLayer(glfwWindow);
        this.imGuiLayer.initImGui();

        //Init Scene
        Window.changeScene(0);
    }


    public void loop(){
        //Init timers
        float beginTime = (float)glfwGetTime();
        float endTime = (float)glfwGetTime();
        float dt = -1.0f;

        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            DebugDraw.beginFrame();

            glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
            glClear(GL_COLOR_BUFFER_BIT);

            //
            if(dt >= 0){
                DebugDraw.draw();
                currentScene.update(dt);
            }
            //
            this.imGuiLayer.update(dt, currentScene);
            glfwSwapBuffers(glfwWindow);

            //Calculate delta time
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
        currentScene.saveExit();
    }
    public static int getWidth(){
        return get().width;
    }

    public static int getHeight(){
        return get().height;
    }

    public static void setWidth(int width){
        get().width = width;
    }

    public static void setHeight(int height){
        get().height = height;
    }
}
