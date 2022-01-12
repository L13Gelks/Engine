package Engine;

import components.*;
import org.joml.Vector2f;
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
            run.addFrame(sprite.getSprite(i), defaultFrameTime);
        }
        run.setLoop(true);
        ////////////////////////////////////////////////////////////////////////
        //States
        ////////////////////////////////////////////////////////////////////////
        StateMachine stateMachine = new StateMachine();
        stateMachine.addState(idle);
        stateMachine.addState(run);
        stateMachine.setDefaultState(idle.title);
        stateMachine.addState(idle.title, run.title, "startRunning");
        stateMachine.addState(run.title, idle.title, "stopRunning");
        player.addComponent(stateMachine);
        ////////////////////////////////////////////////////////////////////////
        //Physics
        ////////////////////////////////////////////////////////////////////////
        PillboxCollider pb = new PillboxCollider();
        pb.width = 0.21f;
        pb.height = 0.25f;
        player.addComponent(pb);

        RigidBody2D rb = new RigidBody2D();
        rb.setBodyType(BodyType.Dynamic);
        rb.setContinuousCollision(false);
        rb.setFixedRotation(true);
        rb.setMass(25.0f);
        player.addComponent(rb);

        player.addComponent(new PlayerController());

        player.transform.zIndex = 10;


        return player;
    }
}
