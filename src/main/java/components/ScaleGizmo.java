package components;

import editor.PropertiesWindow;
import Engine.MouseListener;
import renderer.DebugDraw;

public class ScaleGizmo extends Gizmo {
    public ScaleGizmo(Sprite scaleSprite, PropertiesWindow propertiesWindow) {
        super(scaleSprite, propertiesWindow);
    }
    float previousX = 0.0f;
    float previousY = 0.0f;

    @Override
    public void editorUpdate(float dt) {
        float currentX = MouseListener.getWorldX();
        float currentY = MouseListener.getWorldY();
        if (activeGameObjects != null) {
            if (xAxisActive && !yAxisActive) {
                if(previousX > currentX){
                    activeGameObjects.get(0).transform.scale.x += currentX - previousX;
                }else if (previousX < currentX){
                    activeGameObjects.get(0).transform.scale.x += currentX - previousX;
                }
            } else if (yAxisActive && !xAxisActive) {
                if(previousY > currentY){
                    activeGameObjects.get(0).transform.scale.y += currentY - previousY;
                }else if (previousY < currentY){
                    activeGameObjects.get(0).transform.scale.y += currentY - previousY;
                }
            }
        }

        previousX = currentX;
        previousY = currentY;
        DebugDraw.lineWidth(2.0f);
        super.editorUpdate(dt);
    }
}
