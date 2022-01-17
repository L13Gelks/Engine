package components;

import Engine.MouseListener;
import editor.PropertiesWindow;
import Engine.GameObject;
import Engine.Prefabs;
import Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector4f;
import renderer.DebugDraw;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_LEFT;

public class TranslateGizmo extends Gizmo {


    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    @Override
    public void editorUpdate(float dt) {
        if(activeGameObject != null){
            if(xAxisActive && !yAxisActive){
                activeGameObject.transform.position.x -= MouseListener.getWorldX();
            } else if(yAxisActive && !xAxisActive){
                activeGameObject.transform.position.y -= MouseListener.getWorldY();
            }
        }

        DebugDraw.lineWidth(2.0f);
        super.editorUpdate(dt);
    }
}