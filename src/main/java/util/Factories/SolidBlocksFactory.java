package util.Factories;

import Engine.GameObject;
import Engine.Prefabs;
import components.Ground;
import components.MouseControls;
import components.Sprite;
import components.SpriteSheet;
import imgui.ImGui;
import imgui.ImVec2;
import org.joml.Vector2f;
import physics2d.components.Box2DCollider;
import physics2d.components.RigidBody2D;
import physics2d.enums.BodyType;
import util.AssetPool;

public class SolidBlocksFactory {
    GameObject levelEditorStuff = null;
    SpriteSheet spriteSheet;

    public SolidBlocksFactory(GameObject levelEditorStuff){
        this.levelEditorStuff = levelEditorStuff;
        spriteSheet = AssetPool.getSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png");
        init();
    }

    private void init(){
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
    }
}
