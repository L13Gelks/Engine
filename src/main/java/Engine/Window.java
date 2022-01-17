package Engine;

import observers.EventSystem;
import observers.Observer;
import observers.events.Event;
import observers.events.EventType;
import org.joml.Vector4f;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.opengl.GL;
import physics2d.Physics2D;
import renderer.*;
import scenes.LevelEditorSceneInitializer;
import scenes.LevelSceneInitializer;
import scenes.Scene;
import scenes.SceneInitializer;
import util.AssetPool;

import static org.lwjgl.glfw.Callbacks.glfwFreeCallbacks;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.stbi_image_free;
import static org.lwjgl.stb.STBImage.stbi_load;
import static org.lwjgl.system.MemoryUtil.NULL;
import static org.lwjgl.system.windows.User32.*;

public class Window implements Observer {
    private final int currentResX = 1600;
    private final int currentResY = 900;

    private int width, height;
    private final String windowName;
    private long glfwWindow;

    private static Window window = null;
    private static Scene currentScene;
    private boolean runtimePlaying = false;

    private ImGuiLayer imGuiLayer;
    private FrameBuffer frameBuffer;
    private PickingTexture pickingTexture;
    //private thus only one instance exists
    private Window(){
        this.height = currentResY;
        this.width = currentResX;
        this.windowName = "Game";

        EventSystem.addObserver(this);
    }

    public static void changeScene(SceneInitializer sceneInitializer){
        if(currentScene != null){
            currentScene.destroy();
        }

        getImGuiLayer().getPropertiesWindow().setActiveGameObject(null);
        currentScene = new Scene(sceneInitializer);
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


        //Center widnow
        int max_width  = GetSystemMetrics(SM_CXSCREEN);
        int max_hieght = GetSystemMetrics(SM_CYSCREEN);
        glfwSetWindowMonitor(glfwWindow, NULL, (max_width/2)-(width/2), (max_hieght/2) - (height/2), width, height, GLFW_DONT_CARE);

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

        this.frameBuffer = new FrameBuffer(currentResX, currentResY);
        this.pickingTexture = new PickingTexture(currentResX, currentResY);
        glViewport(0,0, currentResX, currentResY);

        this.imGuiLayer = new ImGuiLayer(glfwWindow, pickingTexture);
        this.imGuiLayer.initImGui();

        //Init Scene
        Window.changeScene(new LevelEditorSceneInitializer());
    }

    public static Physics2D getPhysics() { return currentScene.getPhysics(); }

    public void loop(){
        //Init timers
        float beginTime = (float)glfwGetTime();
        float endTime = (float)glfwGetTime();
        float dt = -1.0f;

        Shader defaultShader = AssetPool.getShader("assets/shaders/default.glsl");
        Shader pickingShader = AssetPool.getShader("assets/shaders/pickingShader.glsl");
        while(!glfwWindowShouldClose(glfwWindow)){
            //Poll Events
            glfwPollEvents();

            //Render pass1. Render to picking texture
            glDisable(GL_BLEND);
            pickingTexture.enableWriting();
            glViewport(0,0 ,currentResX, currentResY);
            glClearColor(0,0,0,0);
            glClear(GL_COLOR_BUFFER_BIT | GL_COLOR_BUFFER_BIT);

            Renderer.bindShader(pickingShader);
            currentScene.render();

            pickingTexture.disableWriting();
            glEnable(GL_BLEND);
            //Render pass1. Render game

            DebugDraw.beginFrame();

            this.frameBuffer.bind();
            Vector4f clearColor = currentScene.camera().clearColor;
            glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w);
            glClear(GL_COLOR_BUFFER_BIT);

            //
            if(dt >= 0){
                Renderer.bindShader(defaultShader);
                if(runtimePlaying){
                    currentScene.update(dt);
                }else{
                    currentScene.editorUpdate(dt);
                }

                currentScene.render();
                DebugDraw.draw();
            }
            this.frameBuffer.unbind();
            //
            this.imGuiLayer.update(dt, currentScene);
            KeyboardListener.endFrame();
            MouseListener.endFrame();
            glfwSwapBuffers(glfwWindow);

            //Calculate delta time
            endTime = (float)glfwGetTime();
            dt = endTime - beginTime;
            beginTime = endTime;
        }
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

    public static FrameBuffer getFramebuffer(){
        return get().frameBuffer;
    }

    public static float getTargetAspectRatio(){
        return 16.0f / 9.0f;
    }

    public static ImGuiLayer getImGuiLayer(){
        return get().imGuiLayer;
    }

    @Override
    public void onNotify(GameObject object, Event event) {
        switch (event.type){
            case GameEngineStartPlay:
                this.runtimePlaying = true;
                currentScene.save();
                Window.changeScene(new LevelSceneInitializer());
                break;
            case GameEngineStopPlay:
                this.runtimePlaying = false;
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case LoadLevel:
                Window.changeScene(new LevelEditorSceneInitializer());
                break;
            case SaveLevel:
                currentScene.save();
                break;
        }
    }
}
