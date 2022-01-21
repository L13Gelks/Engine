package components;

import Engine.Camera;
import Engine.GameObject;
import Engine.MouseListener;
import Engine.Window;
import Game.entity.Player;
import org.joml.Vector2f;
import org.joml.Vector4f;

import static org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_MIDDLE;

public class GameCamera extends  Component{
    private transient GameObject player;
    private transient Camera gameCamera;
    private transient float highestX = Float.MIN_VALUE;
    private transient float undergroundYLevel = 0.0f;
    private transient float cameraBuffer = 1.5f;
    private transient float playerBuffer = 0.25f;
    private transient float scrollSensitivity = 0.1f;

    private transient float cameraMin = 0.2f;
    private transient float cameraNormal = 0.4f;
    private transient float cameraMax = 0.65f;


    private float lerpTime = 0.0f;
    private boolean reset = false;

    private Vector4f skyColor = new Vector4f(0.0f / 255.0f, 0.0f / 255.0f, 0.0f / 255.0f, 1.0f);
    private Vector4f undergroundColor = new Vector4f(0, 0, 0, 1);

    public GameCamera(Camera gameCamera) {
        this.gameCamera = gameCamera;
    }

    @Override
    public void start() {
        this.player = Window.getScene().getGameObjectWith(Player.class);
        this.gameCamera.clearColor.set(skyColor);
        this.undergroundYLevel = this.gameCamera.position.y -
                this.gameCamera.getProjectionSize().y - this.cameraBuffer;
        this.gameCamera.setZoom(cameraNormal);
    }

    @Override
    public void update(float dt) {
        if (player != null && !player.getComponent(Player.class).hasWon()) {
            gameCamera.position.x = player.transform.position.x - (3 * gameCamera.getZoom());
            gameCamera.position.y = player.transform.position.y - (1 * gameCamera.getZoom());
//            if (player.transform.position.y < -playerBuffer) {
//                this.gameCamera.position.y = undergroundYLevel;
//                this.gameCamera.clearColor.set(undergroundColor);
//            } else if (player.transform.position.y >= 0.0f) {
//                this.gameCamera.position.y = 0.0f;
//                this.gameCamera.clearColor.set(skyColor);
//            }

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && !(gameCamera.getZoom() == cameraNormal)){
                reset = true;
            }

            if (MouseListener.getScrollY() != 0 && (gameCamera.getZoom() >= cameraMin &&  gameCamera.getZoom() <= cameraMax)) {
                float addValue = scrollSensitivity;

                addValue *= -Math.signum(MouseListener.getScrollY());
                gameCamera.addZoom(addValue);

                if(gameCamera.getZoom() <= cameraMin){
                    gameCamera.setZoom(cameraMin);
                }else if(gameCamera.getZoom() >= cameraMax){
                    gameCamera.setZoom(cameraMax);
                }
            }

            if (reset) {
                gameCamera.setZoom(cameraNormal);
                reset = false;
            }
        }
    }
}
