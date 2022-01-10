package components;

import Engine.MouseListener;
import editor.PropertiesWindow;
import Engine.GameObject;
import Engine.Prefabs;
import Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class TranslateGizmo extends Gizmo {


    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if(activeGameObject != null){
            if(xAxisActive && !yAxisActive){
                activeGameObject.transform.position.x -= MouseListener.getWorldDx();
            } else if(yAxisActive && !xAxisActive){
                activeGameObject.transform.position.y -= MouseListener.getWorldDy();
            }
        }

        super.editorUpdate(dt);
    }
}