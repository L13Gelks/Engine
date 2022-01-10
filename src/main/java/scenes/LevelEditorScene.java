package scenes;

import Engine.*;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import renderer.DebugDraw;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject ob;
    SpriteSheet spriteSheet;
    private SpriteRenderer objSprite;

    GameObject levelEditorStuff = this.createGameObject("LevelEditor");

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        loadResources();
        spriteSheet = AssetPool.getSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png");
        SpriteSheet gizmos = AssetPool.getSpriteSheet("assets/Images/gizmos.png");

        this.camera = new Camera(new Vector2f());
        levelEditorStuff.addComponent(new MouseControls());
        levelEditorStuff.addComponent(new GridLines());
        levelEditorStuff.addComponent(new EditorCamera(this.camera));
        levelEditorStuff.addComponent(new GizmoSystem(gizmos));

//        ob = new GameObject("0", new Transform(new Vector2f(100,100), new Vector2f(256,256)), 1);
//        objSprite = new SpriteRenderer();
//        objSprite.setColor(new Vector4f(1, 0, 0, 1));
//        ob.addComponent(objSprite);
//        ob.addComponent(new RigidBody());
//        this.addGameObjectToScene(ob);
//        this.activeGameObject = ob;

        levelEditorStuff.start();
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getTexture("assets/images/testImage.png");
        AssetPool.addSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Tiles/NaturalWorld.png"),
                        128, 127, 18, 0));
        AssetPool.addSpriteSheet("assets/Images/gizmos.png",
                new SpriteSheet(AssetPool.getTexture("assets/Images/gizmos.png"),
                        24, 48, 3, 0));
        for (GameObject go : gameObjects){
            if(go.getComponent(SpriteRenderer.class) != null){
                SpriteRenderer spr = go.getComponent(SpriteRenderer.class);
                if(spr.getTexture() != null){
                    spr.setTexture(AssetPool.getTexture(spr.getTexture().getFilepath()));
                }
            }
        }
    }

    @Override
    public void update(float dt){
        levelEditorStuff.update(dt);
        this.camera.adjustProjection();

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }
    }

    @Override
    public void render() {
        this.renderer.render();
    }
    @Override
    public void imgui(){
        ImGui.begin("Level Editr");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Test Window");

        ImVec2 windowPos = new ImVec2();
        ImGui.getWindowPos(windowPos);
        ImVec2 windowSize = new ImVec2();
        ImGui.getWindowSize(windowSize);
        ImVec2 itemSpacing = new ImVec2();
        ImGui.getStyle().getItemSpacing(itemSpacing);

        float windowX2 = windowPos.x + windowSize.x;
        for(int i = 0; i < spriteSheet.size(); i++){
            Sprite sprite = spriteSheet.getSprite(i);
            float spriteWidth = 32;
            float spriteHeight= 32;
            int id = sprite.getTextureID();
            Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

            ImGui.pushID(i);

            if(ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y,textureCoordinates[0].x, textureCoordinates[2].y)){
                GameObject object = Prefabs.generateSpriteObject(sprite, 32, 32);
                //Attach this to mouse cursor
                levelEditorStuff.getComponent(MouseControls.class).pickupObject(object);
            }

            ImGui.popID();

            ImVec2 lastButtonPos = new ImVec2();
            ImGui.getItemRectMax(lastButtonPos);
            float lastButtonX2 = lastButtonPos.x;
            float nextButtonX2 = lastButtonX2 + itemSpacing.x + spriteWidth;
            if(i + 1 < spriteSheet.size() && nextButtonX2 < windowX2){
                ImGui.sameLine();
            }
        }

        ImGui.end();
    }
}
