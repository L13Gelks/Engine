package components;

import Engine.GameObject;
import Engine.MouseListener;
import editor.PropertiesWindow;
import renderer.DebugDraw;

public class TranslateGizmo extends Gizmo {


    public TranslateGizmo(Sprite arrowSprite, PropertiesWindow propertiesWindow) {
        super(arrowSprite, propertiesWindow);
    }

    float lastMousePosX = -60.0f;
    float lastMousePosY = 10.0f;
    int lastGameObject = -1;

    public void resetMousePosition(){
        this.lastMousePosX = -60.0f;
        this.lastMousePosY = 10.0f;
    }

    private void initMousePos(){
        float[] numbers = {0.0f, 0.125f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f,0.875f, 1f};
        int idx = 0;
        int idy = 0;
        //Set a legit world coordinates value for first time
        float fixedLengthX = MouseListener.getWorldX() - (int)MouseListener.getWorldX();
        float distanceX = Math.abs(numbers[0] - fixedLengthX);
        float fixedLengthY = MouseListener.getWorldY() - (int)MouseListener.getWorldY();
        float distanceY = Math.abs(numbers[0] - fixedLengthY);

        for(int c = 1; c < numbers.length; c++) {
            float cdistance = Math.abs(numbers[c] - fixedLengthX);
            if(cdistance < distanceX){
                idx = c;
                distanceX = cdistance;
            }

            cdistance = Math.abs(numbers[c] - fixedLengthY);
            if(cdistance < distanceY){
                idy = c;
                distanceY = cdistance;
            }
        }
        lastMousePosX = (int)MouseListener.getWorldX() + numbers[idx];
        lastMousePosY = (int)MouseListener.getWorldY() + numbers[idy];
    }

    @Override
    public void editorUpdate(float dt) {
        if(activeGameObjects != null && activeGameObjects.size() > 0) {
            if(xAxisActive || yAxisActive){
                if(lastGameObject != activeGameObjects.get(0).getUid()){
                    resetMousePosition();
                }
                lastGameObject = activeGameObjects.get(0).getUid();
                float[] numbers = {0.0f, 0.125f, 0.25f, 0.375f, 0.5f, 0.625f, 0.75f,0.875f, 1f};
                int idx = 0;
                int idy = 0;
                if(lastMousePosX == -60.0f && lastMousePosY == 10.0f) {
                    initMousePos();
                }
                if(Math.abs(lastMousePosX - MouseListener.getWorldX()) >= 0.125f || Math.abs(lastMousePosY - MouseListener.getWorldY()) >= 0.125f) {
                    for(GameObject go : activeGameObjects){
                        float fixedLengthX = MouseListener.getWorldX() - (int)MouseListener.getWorldX();
                        float distanceX = Math.abs(numbers[0] - fixedLengthX);
                        float fixedLengthY = MouseListener.getWorldY() - (int)MouseListener.getWorldY();
                        float distanceY = Math.abs(numbers[0] - fixedLengthY);

                        for(int c = 1; c < numbers.length; c++) {
                            float cdistance = Math.abs(numbers[c] - fixedLengthX);
                            if(cdistance < distanceX) {
                                idx = c;
                                distanceX = cdistance;
                            }
                        }
                        for(int c = 1; c < numbers.length; c++) {
                            float cdistance = Math.abs(numbers[c] - fixedLengthY);
                            if(cdistance < distanceY) {
                                idy = c;
                                distanceY = cdistance;
                            }
                        }

                        if(Math.abs(lastMousePosX - MouseListener.getWorldX()) >= 0.125f) {
                            float diff = ((int)MouseListener.getWorldX() + numbers[idx]) - lastMousePosX;
                            go.transform.position.x += diff;
                        }

                        if(Math.abs(lastMousePosY - MouseListener.getWorldY()) >= 0.125f) {
                            float diff = ((int)MouseListener.getWorldY() + numbers[idy]) - lastMousePosY;
                            go.transform.position.y += diff;
                        }

                    }
                    if(Math.abs(lastMousePosX - MouseListener.getWorldX()) >= 0.125f){
                        lastMousePosX = (int)MouseListener.getWorldX() + numbers[idx];
                    }
                    if(Math.abs(lastMousePosY - MouseListener.getWorldY()) >= 0.125f){
                        lastMousePosY = (int)MouseListener.getWorldY() + numbers[idy];
                    }
                }
            }
        }

        DebugDraw.lineWidth(2.0f);
        super.editorUpdate(dt);
    }
}