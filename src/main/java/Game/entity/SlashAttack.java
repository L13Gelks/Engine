package Game.entity;

import Engine.Camera;
import Engine.GameObject;
import Engine.Window;
import components.Component;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;

public class SlashAttack extends Component {
    private transient boolean goingRight;
    private transient RigidBody2D rb;
    private transient float walkSpeed = 3f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(1f,1f);
    private transient boolean onGround = false;
    private transient boolean isDead = false;
    private transient StateMachine stateMachine;
    private transient float lifeTime = 0.5f;
    private transient float lifeTimeLeft = 0.0f;
    private transient float atk = 0.0f;
    public SlashAttack(boolean goingRight, float atk){
        this.goingRight = goingRight;
        this.atk = atk;
    }

    @Override
    public void start() {
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
    }

    @Override
    public void update(float dt) {
        lifeTimeLeft += dt;
        if(lifeTimeLeft >= lifeTime){
            isDead = true;
        }
        Camera camera = Window.getScene().camera();
        if (this.gameObject.transform.position.x >
                camera.position.x + camera.getProjectionSize().x * camera.getZoom()) {
            return;
        }

        if (isDead) {
            this.gameObject.destroy();
        }

        if (goingRight) {
            velocity.x = walkSpeed;
        } else {
            velocity.x = -walkSpeed;
        }

        checkOnGround();
        if (onGround) {
            this.acceleration.y = 0;
            this.velocity.y = 0;
        } else {
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        }

        this.velocity.y += this.acceleration.y * dt;
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -terminalVelocity.y);
        this.rb.setVelocity(velocity);
    }

    public void checkOnGround() {
        float innerPlayerWidth = 0.25f * 0.7f;
        float yVal = -0.14f;
        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

    @Override
    public void beginCollision(GameObject obj, Contact contact, Vector2f contactNormal) {
        if (isDead) {
            return;
        }

        Enemy entity = obj.getComponent(Enemy.class);

        if (entity != null) {
            Window.getScene().setEnemy(entity);
            entity.receiveDamage(this.atk);
            isDead = true;
        } else if (Math.abs(contactNormal.y) < 0.1f) {
            goingRight = contactNormal.x < 0;
        }
    }
}
