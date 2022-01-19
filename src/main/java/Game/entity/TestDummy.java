package Game.entity;

import Engine.Camera;
import Engine.GameObject;
import Engine.Window;
import components.StateMachine;
import org.jbox2d.dynamics.contacts.Contact;
import org.joml.Vector2f;
import physics2d.Physics2D;
import physics2d.components.RigidBody2D;

public class TestDummy extends Enemy{
    private transient boolean goingRight = false;
    private transient RigidBody2D rb;
    private transient float walkSpeed = 0.0f;
    private transient Vector2f velocity = new Vector2f();
    private transient Vector2f acceleration = new Vector2f();
    private transient Vector2f terminalVelocity = new Vector2f(1f,1f);
    private transient boolean onGround = false;
    private transient float timeToKill = 0.5f;
    private transient StateMachine stateMachine;

    public TestDummy(){
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
        this.stateMachine = gameObject.getComponent(StateMachine.class);
        this.rb = gameObject.getComponent(RigidBody2D.class);
        this.acceleration.y = Window.getPhysics().getGravity().y * 0.7f;
    }

    @Override
    public void update(float dt) {
        Camera camera = Window.getScene().camera();
        if (this.gameObject.transform.position.x >
                camera.position.x + camera.getProjectionSize().x * camera.getZoom()) {
            return;
        }

        if (super.isDead()) {
            timeToKill -= dt;
            if (timeToKill <= 0) {
                //this.gameObject.destroy();
            }
            this.rb.setVelocity(new Vector2f());
            return;
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
        if (super.isDead()) {
            return;
        }

        Player playerController = obj.getComponent(Player.class);

        if (playerController != null) {
            //!playerController.isHurtInvincible() &&
            if (!playerController.isDead() &&
                    contactNormal.y > 0.98f) {
                playerController.enemyBounce();
                //stomp();
            }
//            } else if (!playerController.isDead() && !playerController.isInvincible()) {
//                playerController.die();
//            }
        } else if (Math.abs(contactNormal.y) < 0.1f) {
            goingRight = contactNormal.x < 0;
        }
    }

    public void stomp() {
        stomp(true);
    }

    public void stomp(boolean playSound) {
        //this.isDead = true;
        this.velocity.zero();
        this.rb.setVelocity(new Vector2f());
        this.rb.setAngularVelocity(0.0f);
        this.rb.setGravityScale(0.0f);
        this.rb.setIsSensor();
    }
}
