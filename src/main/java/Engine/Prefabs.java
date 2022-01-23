package Engine;

import Game.entity.Player;
import Game.entity.SlashAttack;
import Game.entity.TestDummy;
import Game.objects.Chest;
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

    public static GameObject generatePlayer() {
        ////////////////////////////////////////////////////////////////////////
        //Textures
        ////////////////////////////////////////////////////////////////////////
        SpriteSheet sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/idle.png");
        GameObject player = generateSpriteObject(sprite.getSprite(0), 0.5f, 0.5f);
        float defaultFrameTime = 0.23f;
        //IDLE
        AnimationState idle = new AnimationState();
        idle.title = "Idle";
        for (int i = 0; i < 30; i++){
            idle.addFrame(sprite.getSprite(i), defaultFrameTime/3);
        }
        idle.setLoop(true);
        //WALK
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/movement.png");
        AnimationState walk = new AnimationState();
        walk.title = "Walk";
        for (int i = 0; i < 30; i++){
            walk.addFrame(sprite.getSprite(i), (defaultFrameTime / 3.5f));
        }
        walk.setLoop(true);
        //Jog
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/movement.png");
        AnimationState jog = new AnimationState();
        jog.title = "Jog";
        for (int i = 30; i < 60; i++){
            jog.addFrame(sprite.getSprite(i), (defaultFrameTime / 5.0f));
        }
        jog.setLoop(true);
        //jumpUP
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/jumpUp.png");
        AnimationState jumpUp = new AnimationState();
        jumpUp.title = "jumpUp";
        for (int i = 0; i < 16; i++){
            jumpUp.addFrame(sprite.getSprite(i), (defaultFrameTime / 7.0f));
        }
        jumpUp.setLoop(false);
       //jumpDown
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/jumpDown.png");
        AnimationState jumpDown = new AnimationState();
        jumpDown.title = "jumpDown";
        for (int i = 0; i < 6; i++){
            jumpDown.addFrame(sprite.getSprite(i), (defaultFrameTime / 2.5f));
        }
        jumpDown.setLoop(false);
        //jumpDown2
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/jumpDown.png");
        AnimationState jumpDown2 = new AnimationState();
        jumpDown2.title = "jumpDown2";
        for (int i = 6; i < 31; i++){
            jumpDown2.addFrame(sprite.getSprite(i), (defaultFrameTime / 2.0f));
        }
        jumpDown2.setLoop(true);
        //Heal
        float healSpeed = defaultFrameTime / 3.0f;
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/heal1.png");
        AnimationState heal = new AnimationState();
        heal.title = "startHealing";
        for (int i = 0; i < 91; i++){
            heal.addFrame(sprite.getSprite(i), healSpeed);
        }
        heal.setLoop(false);
        //Attack
        sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/attack.png");
        AnimationState attack = new AnimationState();
        attack.title = "startAttacking";
        for (int i = 0; i < 16; i++){
            attack.addFrame(sprite.getSprite(i), defaultFrameTime);
        }
        attack.setLoop(false);
        ////////////////////////////////////////////////////////////////////////
        //States
        ////////////////////////////////////////////////////////////////////////
        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(idle);
        stateMachine.addState(walk);
        stateMachine.addState(jog);

        stateMachine.addState(jumpUp);

        stateMachine.addState(jumpDown);
        stateMachine.addState(jumpDown2);

        stateMachine.addState(heal);
        stateMachine.addState(attack);

        stateMachine.setDefaultState(idle.title);
        stateMachine.addState(idle.title, walk.title, "startWalking");
        stateMachine.addState(walk.title, idle.title, "stopWalking");
        stateMachine.addState(walk.title, jog.title, "startJogging");
        stateMachine.addState(jog.title, walk.title, "stopJogging");
        stateMachine.addState(walk.title, jumpUp.title, "jumpUp");
        stateMachine.addState(idle.title, jumpUp.title, "jumpUp");
        stateMachine.addState(jumpUp.title, jumpDown.title, "jumpDown");
        stateMachine.addState(jumpDown.title, jumpDown2.title, "jumpDown2");
        stateMachine.addState(idle.title, jumpDown2.title, "jumpDown2");
        stateMachine.addState(walk.title, jumpDown2.title, "jumpDown2");
        stateMachine.addState(jumpDown.title, idle.title, "stopJumping");
        stateMachine.addState(jumpDown2.title, idle.title, "stopJumping");

        stateMachine.addState(idle.title, heal.title, "startHealing");
        stateMachine.addState(heal.title, idle.title, "stopHealing");

        stateMachine.addState(idle.title, attack.title, "startAttacking");
        stateMachine.addState(attack.title, idle.title, "stopAttacking");
        player.addComponent(stateMachine);
        ////////////////////////////////////////////////////////////////////////
        //Physics
        ////////////////////////////////////////////////////////////////////////
        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.42f;
        pb.height = 0.64f;
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

    public static GameObject generateWoodenChest(){
        ////////////////////////////////////////////////////////////////////////
        //Textures
        ////////////////////////////////////////////////////////////////////////
        SpriteSheet sprite = AssetPool.getSpriteSheet("assets/Sprites/Characters/chest.png");
        GameObject chest = generateSpriteObject(sprite.getSprite(10), 0.25f, 0.25f);
        float defaultFrameTime = 0.23f;
        ////////////////////////////////////////////////////////////////////////
        //State machine
        ////////////////////////////////////////////////////////////////////////
        //IDLE
        AnimationState idle = new AnimationState();
        idle.title = "Idle";
        for (int i = 0; i < 1; i++){
            idle.addFrame(sprite.getSprite(i), defaultFrameTime/3);
        }
        idle.setLoop(true);
        AnimationState open = new AnimationState();
        open.title = "Open";
        for (int i = 1; i < 30; i++){
            open.addFrame(sprite.getSprite(i), defaultFrameTime/3);
        }
        open.setLoop(false);
        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(idle);
        stateMachine.addState(open);
        stateMachine.setDefaultState(idle.title);
        stateMachine.addState(idle.title, open.title, "open");
        chest.addComponent(stateMachine);
        ////////////////////////////////////////////////////////////////////////
        //Physics
        ////////////////////////////////////////////////////////////////////////
        Box2DCollider bc = new Box2DCollider();
        bc.setHalfSize(new Vector2f(0.18f,0.16f));
        bc.setOffset(new Vector2f(0.0f, -0.03f));

        chest.addComponent(bc);

        RigidBody2D rb = new RigidBody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        chest.addComponent(rb);

        chest.addComponent(new Chest());

        chest.transform.zIndex = 0;


        return chest;
    }
}
