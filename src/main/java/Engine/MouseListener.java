package Engine;

import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

public class MouseListener {
    private static MouseListener instance;
    private double scrollX, scrollY;
    private double xPos, yPos, lastX, lastY;
    private boolean mouseButtonPressed[] = new boolean[3];
    private boolean isDragging;

    private MouseListener(){
        this.scrollX = 0.0;
        this.scrollY = 0.0;
        this.xPos = 0.0;
        this.yPos = 0.0;
        this.lastX = 0.0;
        this.lastY = 0.0;
    }

    public static MouseListener get() {
        if(MouseListener.instance == null){
            MouseListener.instance = new MouseListener();
        }
        return MouseListener.instance;
    }

    public static void  mousePositionCallback(long window, double xPos, double yPos){
        MouseListener.get().lastX = MouseListener.get().xPos;
        MouseListener.get().lastY = MouseListener.get().yPos;
        MouseListener.get().xPos = xPos;
        MouseListener.get().yPos = yPos;
        MouseListener.get().isDragging = MouseListener.get().mouseButtonPressed[0] ||
                MouseListener.get().mouseButtonPressed[1] || MouseListener.get().mouseButtonPressed[2];
    }

    public static void  mouseButtonCallback(long window, int button, int action, int modifier){
        if(action == GLFW_PRESS){
            if(button < MouseListener.get().mouseButtonPressed.length){
                MouseListener.get().mouseButtonPressed[button] = true;
            }
        }else if(action == GLFW_RELEASE){
            if(button < MouseListener.get().mouseButtonPressed.length) {
                MouseListener.get().mouseButtonPressed[button] = false;
                MouseListener.get().isDragging = false;
            }
        }
    }

    public static void  mouseScrollCallback(long window, double xOffset, double yOffset){
        MouseListener.get().scrollX = xOffset;
        MouseListener.get().scrollY = yOffset;
    }

    public static void endFrame(){
        MouseListener.get().scrollX = 0;
        MouseListener.get().scrollY = 0;
        MouseListener.get().lastX = MouseListener.get().xPos;
        MouseListener.get().lastY = MouseListener.get().yPos;;
    }

    public static float getX(){
        return (float)MouseListener.get().xPos;
    }

    public static float getY(){
        return (float)MouseListener.get().yPos;
    }

    public static float getOrthoX(){
        float currentX = getX();
        currentX = (currentX / (float)Window.getWidth()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(currentX, 0, 0, 1);
        temp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
        currentX = temp.x;
        return currentX;
    }

    public static float getOrthoY(){
        float currentY = Window.getHeight() - getY();
        //
        currentY = (currentY / (float)Window.getHeight()) * 2.0f - 1.0f;
        Vector4f temp = new Vector4f(0, currentY, 0, 1);
        temp.mul(Window.getScene().camera().getInverseProjection()).mul(Window.getScene().camera().getInverseView());
        currentY = temp.y;
        return currentY;
    }

    public static float getDx(){
        return (float)(MouseListener.get().lastX - MouseListener.get().xPos);
    }

    public static float getDy(){
        return (float)(MouseListener.get().lastY - MouseListener.get().yPos);
    }

    public static float getScrollX(){
        return (float)MouseListener.get().scrollX;
    }

    public static float getScrollY(){
        return (float)MouseListener.get().scrollY;
    }

    public static boolean isDragging(){
        return MouseListener.get().isDragging;
    }

    public static boolean mouseButtonDown(int button){
        if(button < MouseListener.get().mouseButtonPressed.length){
            return MouseListener.get().mouseButtonPressed[button];
        }
        return false;
    }
}
