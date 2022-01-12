package components;

import Engine.Camera;
import Engine.GameObject;
import Engine.Window;
import org.joml.Vector4f;

public class GameCamera extends  Component{
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float undergroundYLevel = 0.0f;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = 0.25f;

    private Vector4f skyColor = new Vector4f(92.0f / 255.0f, 148.0f / 255.0f, 252.0f / 255.0f, 1.0f);
    private Vector4f undergroundColor = new Vector4f(0, 0, 0, 1);

    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void start() {
        this.player = Window.getScene().getGameObjectWith(PlayerController.class);
        this.gameCamera.clearColor.set(skyColor);
        this.undergroundYLevel = this.gameCamera.position.y -
                this.gameCamera.getProjectionSize().y - this.cameraBuffer;
    }

    @Override
    public void update(float dt) {
        if (player != null && !player.getComponent(PlayerController.class).hasWon()) {
            System.out.println(Window.getWidth());
            gameCamera.position.x = player.transform.position.x - 3;

            if (player.transform.position.y < -playerBuffer) {
                this.gameCamera.position.y = undergroundYLevel;
                this.gameCamera.clearColor.set(undergroundColor);
            } else if (player.transform.position.y >= 0.0f) {
                this.gameCamera.position.y = 0.0f;
                this.gameCamera.clearColor.set(skyColor);
            }

            gameCamera.position.y = player.transform.position.y - 1;
        }
    }
}
