package components;

import Engine.Window;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.Settings;

public class GridLines extends  Component{
    @Override
    public void update(float dt){
        Vector2f cameraPos = Window.getScene().camera().position;
        Vector2f projectionSize = Window.getScene().camera().getProjectionSize();

        int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH ) * Settings.GRID_WIDTH);
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT ) * Settings.GRID_HEIGHT);

        int numVLines = (int)(projectionSize.x / Settings.GRID_WIDTH);
        int numHLines= (int)(projectionSize.y / Settings.GRID_HEIGHT);

        int height = (int)projectionSize.y;
        int width = (int)projectionSize.x;

        /*
                int firstX = ((int)(cameraPos.x / Settings.GRID_WIDTH ) - 1) * Settings.GRID_WIDTH;
        int firstY = ((int)(cameraPos.y / Settings.GRID_HEIGHT ) - 1) * Settings.GRID_HEIGHT;

        int numVLines = (int)(projectionSize.x / Settings.GRID_WIDTH) + 2;
        int numHLines= (int)(projectionSize.y / Settings.GRID_HEIGHT) + 2;

        int height = (int)projectionSize.y +  Settings.GRID_HEIGHT * 2;
        int width = (int)projectionSize.x +  Settings.GRID_WIDTH * 2;
        */

        int maxLines = Math.max(numVLines, numHLines);
        Vector3f color = new Vector3f(0.2f,0.2f,0.2f);
        for(int i = 0; i < maxLines; i++){
            int x = firstX + (Settings.GRID_WIDTH * i);
            int y = firstY + (Settings.GRID_HEIGHT * i);

            if(i < numVLines){
                DebugDraw.addLine2D(new Vector2f(x, firstY), new Vector2f(x, firstY + height), color);
            }

            if(i < numHLines){
                DebugDraw.addLine2D(new Vector2f(firstX, y), new Vector2f(firstX + width, y), color);
            }
        }
    }
}
