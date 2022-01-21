package scenes;

import Engine.*;
import Game.UI;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;

import physics2d.components.Box2DCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

public class LevelEditorSceneInitializer extends SceneInitializer {
    SpriteSheet spriteSheet;
    private GameObject levelEditorStuff;
    public static transient boolean isPlayerGenerated = false;

    public LevelEditorSceneInitializer(){

    }

    @Override
    public void init(Scene scene){
        spriteSheet = AssetPool.getSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets/Images/gizmos.png");

        levelEditorStuff = scene.createGameObject("LevelEditor");
        levelEditorStuff.setNoSerialize();
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new KeyControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(scene.camera()));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));
        scene.addGameObjectToScene(levelEditorStuff);
    }

    @Override
    public void loadResources(Scene scene){
        AssetPool.getShader("assets/shaders/default.glsl");

        AssetPool.getTexture("assets/images/testImage.png");

        AssetPool.addSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Tiles/NaturalWorld.png"),
                        128, 127, 18, 0));

        AssetPool.addSpriteSheet("assets/Images/gizmos.png",
                new SpriteSheet(AssetPool.getTexture("assets/Images/gizmos.png"),
                        24, 48, 3, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/idle.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/idle.png"),
                        512, 512, 30, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/walk.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/walk.png"),
                        512, 512, 30, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/jog.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/jog.png"),
                        512, 512, 30, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/jumpUp.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/jumpUp.png"),
                        512, 512, 16, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/jumpDown.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/jumpDown.png"),
                        512, 512, 31, 0));
        AssetPool.addSpriteSheet("assets/images/testDummy.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/testDummy.png"),
                        716, 1134, 1, 0));
        AssetPool.addSpriteSheet("assets/images/sword.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/sword.png"),
                        321, 321, 1, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/chest.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/chest.png"),
                        512, 512, 30, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/heal1.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/heal1.png"),
                        512, 512, 91, 0));


        for (GameObject go : scene.getGameObjects()){
            if(go.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }

            if(go.getComponent(StateMachine.class) != null){
                StateMachine stateMachine = go.getComponent(StateMachine.class);
                stateMachine.refreshTextures();
            }
        }
    }




    @Override
    public void imgui() {
        ImGui.begin("Level Editr");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Objects");

        if(ImGui.beginTabBar("Window TapBar")) {
            if(ImGui.beginTabItem("Solid Blocks")) {

                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < spriteSheet.size(); i++) {
                    //if(i == x) continue; skip sprite without box collider

                    Sprite sprite = spriteSheet.getSprite(i);
                    float spriteWidth = 32;
                    float spriteHeight = 32;
                    int id = sprite.getTextureID();
                    Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

                    ImGui.pushID(i);

                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        //Attach this to mouse cursor
                        RigidBody2D rb = new RigidBody2D();
                        rb.setBodyType(BodyType.Static);
                        object.addComponent(rb);
                        Box2DCollider b2d = new Box2DCollider();
                        b2d.setHalfSize(new Vector2f(0.25f, 0.25f));
                        object.addComponent(b2d);
                        object.addComponent(new Ground());
//                        if(i == x){
//                            object.addComponent(new Breakable());
//                        }
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }

                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            if(ImGui.beginTabItem("Decoration Blocks")){
                ImVec2 windowPos = new ImVec2();
                ImGui.getWindowPos(windowPos);
                ImVec2 windowSize = new ImVec2();
                ImGui.getWindowSize(windowSize);
                ImVec2 itemSpacing = new ImVec2();
                ImGui.getStyle().getItemSpacing(itemSpacing);

                float windowX2 = windowPos.x + windowSize.x;
                for (int i = 0; i < spriteSheet.size(); i++) {
                    //if(i == x) continue; skip sprite with box collider

                    Sprite sprite = spriteSheet.getSprite(i);
                    float spriteWidth = 32;
                    float spriteHeight = 32;
                    int id = sprite.getTextureID();
                    Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

                    ImGui.pushID(i);

                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y)) {
                        GameObject object = Prefabs.generateSpriteObject(sprite, 0.25f, 0.25f);
                        //Attach this to mouse cursor
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                    }

                    ImGui.popID();

                    ImVec2 lastButtonPos = new ImVec2();
                    ImGui.getItemRectMax(lastButtonPos);
                    float lastButtonX2 = lastButtonPos.x;
                    float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
                    if (i + 1 < spriteSheet.size() && nextButtonX2 < windowX2) {
                        ImGui.sameLine();
                    }
                }
                ImGui.endTabItem();
            }
            if(ImGui.beginTabItem("Prefabs")){
                int uid = 0;
                if(!isPlayerGenerated){
                    SpriteSheet playerSprites = AssetPool.getSpriteSheet("assets/Sprites/Characters/idle.png");
                    Sprite sprite = playerSprites.getSprite(0);
                    float spriteWidth = 32;
                    float spriteHeight = 32;
                    int id = sprite.getTextureID();
                    Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

                    ImGui.pushID(uid++);
                    if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y)) {
                        GameObject object = Prefabs.generatePlayer();
                        //Attach this to mouse cursor
                        levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                        isPlayerGenerated = true;
                    }
                    ImGui.popID();
                }
                SpriteSheet testDummy = AssetPool.getSpriteSheet("assets/images/testDummy.png");
                Sprite sprite = testDummy.getSprite(0);
                float spriteWidth = 32;
                float spriteHeight = 32;
                int id = sprite.getTextureID();
                Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y)) {
                    GameObject object = Prefabs.testDummy();
                    //Attach this to mouse cursor
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();

                /////////////////
                ImGui.sameLine();
                SpriteSheet chest = AssetPool.getSpriteSheet("assets/Sprites/Characters/chest.png");
                Sprite chestSprite = chest.getSprite(0);
                id = chestSprite.getTextureID();
                textureCoordinates = chestSprite.getTextureCoordinates();

                ImGui.pushID(uid++);
                if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y)) {
                    GameObject object = Prefabs.generateWoodenChest();
                    //Attach this to mouse cursor
                    levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
                }
                ImGui.popID();
                /////////////////
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }

        ImGui.end();
    }
}
