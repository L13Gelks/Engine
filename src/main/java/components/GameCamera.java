package components;

import Engine.Camera;
import Engine.GameObject;
import Engine.MouseListener;
import Engine.Window;
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
    private float scrollSensitivity = 0.2f;

    private float lerpTime = 0.0f;
    private boolean reset = false;

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
            gameCamera.position.x = player.transform.position.x - (3 * gameCamera.getZoom());
            gameCamera.position.y = player.transform.position.y - (1 * gameCamera.getZoom());
//            if (player.transform.position.y < -playerBuffer) {
//                this.gameCamera.position.y = undergroundYLevel;
//                this.gameCamera.clearColor.set(undergroundColor);
//            } else if (player.transform.position.y >= 0.0f) {
//                this.gameCamera.position.y = 0.0f;
//                this.gameCamera.clearColor.set(skyColor);
//            }

            if(MouseListener.mouseButtonDown(GLFW_MOUSE_BUTTON_MIDDLE) && !(gameCamera.getZoom() == 1.0f)){
                reset = true;
            }

            if (MouseListener.getScrollY() != 0 && (gameCamera.getZoom() >= 0.4f &&  gameCamera.getZoom() <= 1.3f)) {
                float addValue = (float)Math.pow(Math.abs(MouseListener.getScrollY() * scrollSensitivity),
                        1 / gameCamera.getZoom());
                addValue *= -Math.signum(MouseListener.getScrollY());
                gameCamera.addZoom(addValue);

                if(gameCamera.getZoom() <= 0.4f){
                    gameCamera.setZoom(0.4f);
                }else if(gameCamera.getZoom() >= 1.3f){
                    gameCamera.setZoom(1.3f);
                }
            }

            if (reset) {
                gameCamera.position.lerp(new Vector2f(), lerpTime);
                gameCamera.setZoom(this.gameCamera.getZoom() +
                        ((1.0f - gameCamera.getZoom()) * lerpTime));
                this.lerpTime += 0.1f * dt;
                if (Math.abs(gameCamera.position.x) <= 1.0f ||
                        Math.abs(gameCamera.position.y) <= 1.0f) {
                    this.lerpTime = 0.0f;
                    gameCamera.position.set(0f, 0f);
                    this.gameCamera.setZoom(1.0f);
                    reset = false;
                }
            }
        }
    }
}
