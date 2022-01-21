package Game.objects;

import Engine.Camera;
import Engine.GameObject;
import Engine.Window;
import Game.entity.Player;
import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;

public class Chest extends Component {
    private transient boolean onGround = false;
    private transient float timeToKill = 2.5f;
    private transient StateMachine stateMachine;
    private transient boolean  isDead = false;


    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
    }

    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.14f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        Player playerController = obj.getComponent(Player.class);
        if (playerController != null) {
            stateMachine.trigger("open");
            isDead = true;
        }
    }

    @Override
    public void update(float dt){
        Camera camera = Window.getScene().camera();
        if (this.gameObject.transform.position.x >
                camera.position.x + camera.getProjectionSize().x * camera.getZoom()) {
            return;
        }

        if (isDead) {
            timeToKill -= dt;
            if (timeToKill <= 0) {
                this.gameObject.destroy();
            }
            return;
        }


        checkOnGround();
    }
}
