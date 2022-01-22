package util.Factories;

import Engine.GameObject;
import Engine.Prefabs;
import components.MouseControls;
import components.Sprite;
import components.SpriteSheet;
import imgui.ImGui;
import org.joml.Vector2f;
import util.AssetPool;

public class PrefabsFactory {
    GameObject levelEditorStuff = null;
    Vector2f[] textureCoordinates;
    float spriteWidth = 32;
    float spriteHeight = 32;
    int id = -1;
    //Unique id for each preFabricated item created
    int uid = 0;

    public  PrefabsFactory(GameObject levelEditorStuff){
        this.levelEditorStuff = levelEditorStuff;
    }

    private void generateObject(GameObject gameObject) {
        ImGui.pushID(uid++);
        if (ImGui.imageButton(id, spriteWidth, spriteHeight, textureCoordinates[2].x, textureCoordinates[0].y, textureCoordinates[0].x, textureCoordinates[2].y)) {
            levelEditorStuff.getComponent(MouseControls.class).pickupObject(gameObject);
        }
        ImGui.popID();
    }

    public void generatePlayer(){
        SpriteSheet playerSprites = AssetPool.getSpriteSheet("assets/Sprites/Characters/idle.png");
        Sprite sprite = playerSprites.getSprite(0);
        id = sprite.getTextureID();
        textureCoordinates = sprite.getTextureCoordinates();
        this.generateObject(Prefabs.generatePlayer());
    }

    public void generateTestDummy(){
        SpriteSheet testDummy = AssetPool.getSpriteSheet("assets/images/testDummy.png");
        Sprite sprite = testDummy.getSprite(0);
        id = sprite.getTextureID();
        textureCoordinates = sprite.getTextureCoordinates();
        this.generateObject(Prefabs.testDummy());
    }

    public void generateWoodenChest(){
        SpriteSheet chest = AssetPool.getSpriteSheet("assets/Sprites/Characters/chest.png");
        Sprite chestSprite = chest.getSprite(0);
        id = chestSprite.getTextureID();
        textureCoordinates = chestSprite.getTextureCoordinates();
        this.generateObject(Prefabs.generateWoodenChest());
    }
}
