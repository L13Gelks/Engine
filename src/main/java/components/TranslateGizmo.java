package components;

import Engine.MouseListener;
import editor.PropertiesWindow;
import renderer.DebugDraw;

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