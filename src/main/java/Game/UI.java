package Game;

import Engine.Camera;
import Engine.Window;
import Game.entity.Player;
import components.Component;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;

public class UI extends Component {
    Vector3f red = new Vector3f(1.f ,0.0f, 0.0f);
    Vector3f green = new Vector3f(0.f ,1.0f, 0.0f);
    Vector3f blue = new Vector3f(0.f ,0.0f, 1.0f);
    Vector3f black = new Vector3f(0.f ,0.0f, 0.0f);
    Player player;

    public UI(Player player){
        this.player = player;
    }
//https://github.com/SilverTiger/lwjgl3-tutorial/wiki/Fonts
    @Override
    public void update(float dt){
        DebugDraw.reset();
        Camera camera = Window.getScene().camera();
        Vector2f cameraPos = camera.position;
        Vector2f projectionSize = camera.getProjectionSize();


        float width = 5.0f;
        DebugDraw.lineWidth(width);
        float posX = ((cameraPos.x) + 0.3f);
        float posX2 = posX + (1.5f * camera.getZoom());
        float posX3 = posX;
        float posX4 = posX;
        float posX5 = posX;
        float posY = (cameraPos.y + (2.75f * camera.getZoom()));
        float posY2 = (cameraPos.y + (2.70f * camera.getZoom()));
        float posY3 = (cameraPos.y + (2.65f * camera.getZoom()));

        DebugDraw.addLine2D(new Vector2f(posX, posY), new Vector2f(posX2, posY) , black);
        DebugDraw.addLine2D(new Vector2f(posX, posY2), new Vector2f(posX2, posY2) , black);
        DebugDraw.addLine2D(new Vector2f(posX, posY3), new Vector2f(posX2, posY3) , black);

        posX3 += ((1.5f * camera.getZoom()) * (player.getHealthPoints() / player.getMaxHealthPoints()));
        DebugDraw.addLine2D(new Vector2f(posX, posY), new Vector2f(posX3, posY) , red);
        posX4 += ((1.5f * camera.getZoom()) * (player.getStaminaPoints() / player.getMaxStaminaPoints()));
        DebugDraw.addLine2D(new Vector2f(posX, posY2), new Vector2f(posX4, posY2) , green);
        posX5 += ((1.5f * camera.getZoom()) * (player.getManaPoints() / player.getMaxManaPoints()));
        DebugDraw.addLine2D(new Vector2f(posX, posY3), new Vector2f(posX5, posY3) , blue);
    }
}
