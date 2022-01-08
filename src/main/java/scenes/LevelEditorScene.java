package scenes;

import Engine.Camera;
import Engine.GameObject;
import Engine.Prefabs;
import Engine.Transform;
import components.*;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene {
    private GameObject ob;
    SpriteSheet spriteSheet;
    private SpriteRenderer objSprite;

    MouseControls mouseControls = new MouseControls();

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        loadResources();
        this.camera = new Camera(new Vector2f());
        spriteSheet = AssetPool.getSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png");
        if(levelLoaded){
            this.activeGameObject = gameObjects.get(0);
            return;
        }


        ob = new GameObject("0", new Transform(new Vector2f(100,100), new Vector2f(256,256)), 1);
        objSprite = new SpriteRenderer();
        objSprite.setColor(new Vector4f(1, 0, 0, 1));
        ob.addComponent(objSprite);
        ob.addComponent(new RigidBody());
        this.addGameObjectToScene(ob);
        this.activeGameObject = ob;

        GameObject ob2 = new GameObject("1", new Transform(new Vector2f(200,100), new Vector2f(256,256)), 2);
        SpriteRenderer objSprite2 = new SpriteRenderer();
        Sprite ob2S = new Sprite();
        ob2S.setTexture(AssetPool.getTexture("assets/images/testImage.png"));
        objSprite2.setSprite(ob2S);
        ob2.addComponent(objSprite2);
        this.addGameObjectToScene(ob2);
    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getTexture("assets/images/testImage.png");
        AssetPool.addSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Tiles/NaturalWorld.png"),
                        128, 127, 18, 0));
    }

    @Override
    public void update(float dt){
        mouseControls.update(dt);
        for(GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }

    @Override
    public void imgui(){
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
            float spriteWidth = sprite.getWidth() / 2;
            float spriteHeight= sprite.getHeight() / 2;
            int id = sprite.getTextureID();
            Vector2f[] textureCoordinates = sprite.getTextureCoordinates();

            ImGui.pushID(i);

            if(ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[0].x, textureCoordinates[0].y,textureCoordinates[2].x, textureCoordinates[2].y)){
                GameObject object = Prefabs.generateSpriteObject(sprite, spriteWidth, spriteHeight);
                //Attach this to mouse cursor
                mouseControls.pickupObject(object);
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
