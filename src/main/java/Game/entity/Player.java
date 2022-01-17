package Game.entity;

import Engine.GameObject;
import Engine.KeyboardListener;
import Engine.Window;
import components.Component;
import components.Ground;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;

import static org.lwjgl.glfw.GLFW.*;


public class Player extends Entity {
    private enum PlayerState {
        Normal,
        Invincible
    }

    public float walkSpeed = 0.3f;
    public float runSpeed = 1.9f;
    public float currentSpeed = 0.0f;
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

    private void InitializePlayerStatistics(){
        //INITIALIZE MAIN STATS
        this.setMaxHealthPoints(90.0f + (10.0f * this.getHealth()));
        this.setHealthPoints(this.getMaxHealthPoints());
        this.setMaxStaminaPoints(58.0f + (2.0f * this.getStamina()));
        this.setStaminaPoints(this.getMaxStaminaPoints());
        this.setMaxManaPoints(18.0f + (3.0f * this.getMagic()) + (9.0f * this.getIntelligence()));
        this.setManaPoints(this.getMaxManaPoints());
        this.setSpeedX(1.0f);
        this.setSpeedY(1.0f);
        this.setInvisibleFrames(0.2f + (0.01f * this.getDexterity()) + (0.005f * this.getIntelligence()));
        this.setInvisibleFramesLeft(0.0f);
        this.setCarryCapacity(30.0f + (5.0f * this.getStrength()) + (3.0f * this.getStamina()));

        this.setPhysAttack(10.0f + (1.0f * this.getStrength()) + (0.5f * this.getDexterity()));
        this.setMagicAttack(0.0f + (1.5f * this.getMagic()) + (1.1f * this.getIntelligence()));
        this.setPhysDefense(10.0f + (0.5f * this.getHealth()) + (0.5f * this.getStrength()) + (1.5f * this.getDefense()) + (0.2f * this.getResistance()));
        this.setMagicDefense(10.0f + (1.0f * this.getMagic()) + (0.2f * this.getIntelligence()) + (0.5f * this.getDefense()) + (0.1f * this.getResistance()));
        this.setPhysShieldStrength(0.0f);
        this.setMagicShieldStrength(0.0f);
        this.setPhysShieldPoints(0.0f);
        this.setMagicShieldPoints(0.0f);
        this.setAttackSpeed(1.0f + (0.03f * this.getStrength()) + (0.09f * this.getDexterity()));

        this.setSlashResistance(0.0f);
        this.setBluntResistance(0.0f);
        this.setPierceResistance(0.0f);
        this.setMagicResistance(0.0f);

        this.setFireResistance(0.0f);
        this.setWaterResistance(0.0f);
        this.setElectricityResistance(0.0f);
        this.setAirResistance(0.0f);
        this.setEarthResistance(0.0f);
        this.setDarkResistance(0.0f);
        this.setLightResistance(0.0f);

        this.setBleedingResistance(1.0f);
        this.setPoisonResistance(1.0f);
    }

    @Override
    public void start() {
        InitializePlayerStatistics();
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb.setGravityScale(0.0f);

    }

    @Override
    public void update(float dt) {
        if (KeyboardListener.isKeyPressed(GLFW_KEY_RIGHT) || KeyboardListener.isKeyPressed(GLFW_KEY_D)) {
            boolean isRunning = KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT);
            this.currentSpeed = isRunning ? runSpeed : walkSpeed;
            this.setStaminaPoints(this.getStaminaPoints() - (((velocity.x < 0.0f ? -velocity.x : velocity.x) * 5) * dt));
            this.currentSpeed = this.getStaminaPoints() > 0 ? this.currentSpeed : 0.0f;

            this.gameObject.transform.scale.x = playerWidth;
            this.acceleration.x = currentSpeed;

            if (this.velocity.x < 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x += slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyboardListener.isKeyPressed(GLFW_KEY_LEFT) || KeyboardListener.isKeyPressed(GLFW_KEY_A)) {
            boolean isRunning = KeyboardListener.isKeyPressed(GLFW_KEY_LEFT_SHIFT);
            this.currentSpeed = isRunning ? runSpeed : walkSpeed;
            this.setStaminaPoints(this.getStaminaPoints() - (((velocity.x < 0.0f ? -velocity.x : velocity.x) * 5) * dt));
            this.currentSpeed = this.getStaminaPoints() > 0 ? this.currentSpeed : 0.0f;

            this.gameObject.transform.scale.x = -playerWidth;
            this.acceleration.x = -currentSpeed;

            if (this.velocity.x > 0) {
                this.stateMachine.trigger("switchDirection");
                this.velocity.x -= slowDownForce;
            } else {
                this.stateMachine.trigger("startRunning");
            }
        } else if (KeyboardListener.isKeyPressed(GLFW_KEY_ESCAPE)) {
            InitializePlayerStatistics();
        } else {
            this.acceleration.x = 0;
            if (this.velocity.x > 0) {
                this.velocity.x = Math.max(0, this.velocity.x - slowDownForce);
            } else if (this.velocity.x < 0) {
                this.velocity.x = Math.min(0, this.velocity.x + slowDownForce);
            }

            if (this.velocity.x == 0) {
                this.stateMachine.trigger("stopRunning");
                if(this.getStaminaPoints() > this.getMaxStaminaPoints()){
                    this.setStaminaPoints(this.getMaxStaminaPoints());
                }else if(this.getStaminaPoints() < this.getMaxStaminaPoints()){
                    this.setStaminaPoints(this.getStaminaPoints() + (10.0f * dt));
                }
            }
        }

        checkOnGround();

        if (KeyboardListener.isKeyPressed(GLFW_KEY_SPACE) && (jumpTime > 0 || onGround || groundDebounce > 0)) {
            if ((onGround || groundDebounce > 0) && jumpTime == 0 && this.getStaminaPoints() >= 5.f) {
                //AssetPool.getSound("assets/sounds/jump-small.ogg").play();
                jumpTime = 28;
                this.velocity.y = jumpImpulse;
                this.setStaminaPoints(this.getStaminaPoints() - 5.0f);
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
        } else if (!onGround) {
            if (this.jumpTime > 0) {
                this.velocity.y *= 0.35f;
                this.jumpTime = 0;
            }
            groundDebounce -= dt;
            this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
            //this.setStaminaPoints(this.getStaminaPoints() + (5.0f * dt));
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

        if (!onGround ) {
            if(this.velocity.y > 0){
                stateMachine.trigger("jumpUp");
            }else {
                stateMachine.trigger("jumpDown");

            }
        } else {
            stateMachine.trigger("stopJumping");
        }
        this.setStaminaPoints(this.getStaminaPoints() <= 0.0f ? 0.0f : this.getStaminaPoints());
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