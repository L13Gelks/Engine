package components;

import Engine.KeyboardListener;
import Engine.Window;

import static org.lwjgl.glfw.GLFW.*;

public class GizmoSystem extends Component {
    private SpriteSheet gizmos;
    private int usingGizmo = 0;

    public GizmoSystem(SpriteSheet gizmoSprites) {
        gizmos = gizmoSprites;
    }

    @Override
    public void start() {
        gameObject.addComponent(new TranslateGizmo(gizmos.getSprite(1),
                Window.getImGuiLayer().getPropertiesWindow()));
        gameObject.addComponent(new ScaleGizmo(gizmos.getSprite(2),
                Window.getImGuiLayer().getPropertiesWindow()));
    }

    @Override
    public void editorUpdate(float dt) {
        if (usingGizmo == 0) {
            gameObject.getComponent(TranslateGizmo.class).setUsing();
            gameObject.getComponent(ScaleGizmo.class).setNotUsing();
        } else if (usingGizmo == 1) {
            gameObject.getComponent(TranslateGizmo.class).setNotUsing();
            gameObject.getComponent(ScaleGizmo.class).setUsing();
        }

        if (KeyboardListener.isKeyPressed(GLFW_KEY_G)) {
            usingGizmo = 0;
        } else if (KeyboardListener.isKeyPressed(GLFW_KEY_S)) {
            usingGizmo = 1;
        }
    }
}