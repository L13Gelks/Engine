package Engine;

import Game.entity.Player;
import Game.entity.SlashAttack;
import Game.entity.TestDummy;
import components.*;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.PillboxCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

public class Prefabs {
    public static GameObject generateSpriteObject(Sprite sprite, float sizeX, float sizeY){
        GameObject block =  Window.getScene().createGameObject("Sprite_Object_Gen");
        block.transform.scale.x = sizeX;
        block.transform.scale.y = sizeY;
        SpriteRenderer renderer = new SpriteRenderer();
        renderer.setSprite(sprite);
        block.addComponent(renderer);

        return block;
    }
    public static GameObject generatePlayer(){
        ////////////////////////////////////////////////////////////////////////
        //Textures
        ////////////////////////////////////////////////////////////////////////
        SpriteSheet sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/waifuIdle.png");
        GameObject player = generateSpriteObject(sprite.getSprite(0), 0.25f, 0.25f);
        float defaultFrameTime = 0.23f;
        //IDLE
        AnimationState idle = new AnimationState();
        idle.title = "Idle";
        for (int i = 0; i < 16; i++){
            idle.addFrame(sprite.getSprite(i), defaultFrameTime);
        }
        idle.setLoop(true);
        //RUN
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/waifuRun.png");
        AnimationState run = new AnimationState();
        run.title = "Run";
        for (int i = 0; i < 20; i++){
            run.addFrame(sprite.getSprite(i), (defaultFrameTime / 3.0f));
        }
        run.setLoop(true);
        //jumpUp
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/waifuJump.png");
        AnimationState jumpUp = new AnimationState();
        jumpUp.title = "JumpUp";
        for (int i = 0; i < 10; i++){
            jumpUp.addFrame(sprite.getSprite(i), (defaultFrameTime / 2.0f));
        }
        jumpUp.setLoop(false);
        //jumpDown
        AnimationState jumpDown = new AnimationState();
        jumpDown.title = "jumpDown";
        for (int i = 11; i < 26; i++){
            jumpDown.addFrame(sprite.getSprite(i), (defaultFrameTime / 2.0f));
        }
        jumpDown.setLoop(false);
        ////////////////////////////////////////////////////////////////////////
        //States
        ////////////////////////////////////////////////////////////////////////
        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(idle);
        stateMachine.addState(run);
        stateMachine.addState(jumpUp);
        stateMachine.addState(jumpDown);

        stateMachine.setDefaultState(idle.title);
        stateMachine.addState(idle.title, run.title, "startRunning");
        stateMachine.addState(run.title, idle.title, "stopRunning");
        stateMachine.addState(run.title, jumpUp.title, "jumpUp");
        stateMachine.addState(idle.title, jumpUp.title, "jumpUp");
        stateMachine.addState(jumpUp.title, jumpDown.title, "jumpDown");
        stateMachine.addState(idle.title, jumpDown.title, "jumpDown");
        stateMachine.addState(run.title, jumpDown.title, "jumpDown");
        //stateMachine.addState(jumpDown.title, stopJumping.title, "stopJumping");
        stateMachine.addState(jumpDown.title, idle.title, "stopJumping");
        player.addComponent(stateMachine);
        ////////////////////////////////////////////////////////////////////////
        //Physics
        ////////////////////////////////////////////////////////////////////////
        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.21f;
        pb.height = 0.32f;
        player.addComponent(pb);

        RigidBody2D rb = new RigidBody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        player.addComponent(rb);

        player.addComponent(new Player());

        player.transform.zIndex = 10;


        return player;
    }

    public static GameObject testDummy(){
        ////////////////////////////////////////////////////////////////////////
        //Textures
        ////////////////////////////////////////////////////////////////////////
        SpriteSheet sprite = AssetPool.getSpriteSheet("assets/images/testDummy.png");
        GameObject testDummy = generateSpriteObject(sprite.getSprite(0), 0.5f, 0.730f);

        ////////////////////////////////////////////////////////////////////////
        //Physics
        ////////////////////////////////////////////////////////////////////////
        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.41f;
        pb.height = 0.94f;
        testDummy.addComponent(pb);

        RigidBody2D rb = new RigidBody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        testDummy.addComponent(rb);

        testDummy.addComponent(new TestDummy());

        testDummy.transform.zIndex = 10;


        return testDummy;
    }

    public static GameObject generateSlashAttack(boolean right, float atk){
        ////////////////////////////////////////////////////////////////////////
        //Textures
        ////////////////////////////////////////////////////////////////////////
        SpriteSheet sprite = AssetPool.getSpriteSheet("assets/images/sword.png");
        GameObject slashAttack = generateSpriteObject(sprite.getSprite(0), 0.25f, 0.25f);

        ////////////////////////////////////////////////////////////////////////
        //Physics
        ////////////////////////////////////////////////////////////////////////
        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.25f;
        pb.height = 0.25f;
        slashAttack.addComponent(pb);

        RigidBody2D rb = new RigidBody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        slashAttack.addComponent(rb);

        slashAttack.addComponent(new SlashAttack(right, atk));

        slashAttack.transform.zIndex = 10;


        return slashAttack;
    }
}
