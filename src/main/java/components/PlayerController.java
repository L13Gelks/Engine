package components;

import Engine.GameObject;
import Engine.KeyboardListener;
import Engine.Window;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.Physics2D;
import physics2d.RaycastInfo;
import physics2d.components.RigidBody2D;
import renderer.DebugDraw;
import util.AssetPool;

import static org.lwjgl.glfw.GLFW.*;


public class PlayerController extends Component{
    private enum PlayerState {
        Normal,
        Invincible
    }
    public float walkSpeed = 1.9f;
    public float jumpBoost = 1.0f;
    public float jumpImpulse = 3.0f;
    public float slowDownForce = 0.05f;
    public Vector2f terminalVelocity = new Vector2f(2.1f, 3.1f);

    private PlayerState playerState = PlayerState.Normal;
    public transient boolean onGround = false;
    private transient float groundDebounce = 0.0f;
    private transient float groundDebounceTime = 0.1f;
    private transient RigidBody2D rb;
    private transient StateMachine stateMachine;
    private transient float bigJumpBoostFactor = 1.05f;
    private transient float playerWidth = 0.25f;
    private transient int jumpTime = 0;
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f velocity = new Vector2f();
    private transient boolean isDead = false;
    private transient int enemyBounce = 0;

    @Override
    public void start() {
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);
    }

    @Override
    public void update(float dt) {
        if (KeyboardListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyboardListener.isKeyPressed(GLFW_KEY_D)) {
            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = walkSpeed;

            if (this.velocity.x < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyboardListener.isKeyPressed(GLFW_KEY_LEFT) || KeyboardListener.isKeyPressed(GLFW_KEY_A)) {
            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -walkSpeed;

            if (this.velocity.x > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
                this.stateMachine.trigger("stopRunning");
            }
        }

        checkOnGround();

        if (KeyboardListener.isKeyPressed(GLFW_KEY_SPACE) && (jumpTime > 0 || onGround || groundDebounce > 0)) {
            if ((onGround || groundDebounce > 0) && jumpTime == 0) {
                //AssetPool.getSound("assets/sounds/jump-small.ogg").play();
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
            } else if (jumpTime > 0) {
                jumpTime--;
                this.velocity.y = ((jumpTime / 2.2f) * jumpBoost);
            } else {
                this.velocity.y = 0;
            }
            groundDebounce = 0;
            } else if (enemyBounce > 0) {
                enemyBounce--;
                this.velocity.y = ((enemyBounce / 2.2f) * jumpBoost);
        }else if (!onGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
        } else {
            this.velocity.y = 0;
            this.acceleration.y = 0;
            groundDebounce = groundDebounceTime;
        }

        this.velocity.x += this.acceleration.x * dt;
        this.velocity.y += this.acceleration.y * dt;
        this.velocity.x = Math.max(Math.min(this.velocity.x, this.terminalVelocity.x), -this.terminalVelocity.x);
        this.velocity.y = Math.max(Math.min(this.velocity.y, this.terminalVelocity.y), -this.terminalVelocity.y);
        this.rb.setVelocity(this.velocity);
        this.rb.setAngularVelocity(0);

        if (!onGround) {
            stateMachine.trigger("jump");
        } else {
            stateMachine.trigger("stopJumping");
        }
    }


    public void checkOnGround() {
        float innerPlayerWidth = this.playerWidth * 0.6f;
        float yVal = playerState == PlayerState.Normal ? -0.12f : -0.24f;

        onGround = Physics2D.checkOnGround(this.gameObject, innerPlayerWidth, yVal);
    }

//   public void setPosition(Vector2f newPos) {
//       this.gameObject.transform.position.set(newPos);
//       this.rb.setPosition(newPos);
//   }
    public boolean isNormal(){
        return this.playerState == PlayerState.Normal;
    }
    @Override
    public void beginCollision(GameObject collidingObject, Contact contact, Vector2f contactNormal) {
        if (isDead) return;

        if (collidingObject.getComponent(Ground.class) != null) {
            if (Math.abs(contactNormal.x) > 0.8f) {
                this.velocity.x = 0;
            } else if (contactNormal.y > 0.8f) {
                this.velocity.y = 0;
                this.acceleration.y = 0;
                this.jumpTime = 0;
            }
        }
    }
//
//    public void enemyBounce() {
//        this.enemyBounce = 8;
//    }
//
//    public boolean isDead() {
//        return this.isDead;
//    }
//
//    public boolean isHurtInvincible() {
//        return this.hurtInvincibilityTimeLeft > 0 || playWinAnimation;
//    }
//
//    public boolean isInvincible() {
//        return this.playerState == PlayerState.Invincible ||
//                this.hurtInvincibilityTimeLeft > 0 || playWinAnimation;
//    }
//
//    public void die() {
//        this.stateMachine.trigger("die");
//        if (this.playerState == PlayerState.Small) {
//            this.velocity.set(0, 0);
//            this.acceleration.set(0, 0);
//            this.rb.setVelocity(new Vector2f());
//            this.isDead = true;
//            this.rb.setIsSensor();
//            AssetPool.getSound("assets/sounds/mario_die.ogg").play();
//            deadMaxHeight = this.gameObject.transform.position.y + 0.3f;
//            this.rb.setBodyType(BodyType.Static);
//            if (gameObject.transform.position.y > 0) {
//                deadMinHeight = -0.25f;
//            }
//        } else if (this.playerState == PlayerState.Big) {
//            this.playerState = PlayerState.Small;
//            gameObject.transform.scale.y = 0.25f;
//            PillboxCollider pb = gameObject.getComponent(PillboxCollider.class);
//            if (pb != null) {
//                jumpBoost /= bigJumpBoostFactor;
//                walkSpeed /= bigJumpBoostFactor;
//                pb.setHeight(0.25f);
//            }
//            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
//            AssetPool.getSound("assets/sounds/pipe.ogg").play();
//        } else if (playerState == PlayerState.Fire) {
//            this.playerState = PlayerState.Big;
//            hurtInvincibilityTimeLeft = hurtInvincibilityTime;
//            AssetPool.getSound("assets/sounds/pipe.ogg").play();
//        }
//    }
//
    public boolean hasWon() {
        return false;
    }

}
