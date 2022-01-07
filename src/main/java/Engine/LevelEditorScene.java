package Engine;

import components.Sprite;
import components.SpriteRenderer;
import components.SpriteSheet;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

public class LevelEditorScene extends Scene{
    public LevelEditorScene(){

    }
    private GameObject ob;
    SpriteSheet spriteSheet;
    @Override
    public void init(){
        loadResources();

        this.camera = new Camera(new Vector2f());

        spriteSheet = AssetPool.getSpriteSheet("assets/images/sprite.png");

        ob = new GameObject("1", new Transform(new Vector2f(100,100), new Vector2f(128,256)), 3);
        ob.addComponent(new SpriteRenderer(spriteSheet.getSprite(8)));
        this.addGameObjectToScene(ob);

        GameObject ob2 = new GameObject("1", new Transform(new Vector2f(200,100), new Vector2f(128,256)), 2);
        ob2.addComponent(new SpriteRenderer(spriteSheet.getSprite(0)));
        this.addGameObjectToScene(ob2);


    }

    private void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.addSpriteSheet("assets/images/sprite.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/sprite.png"),
                        100, 189, 42, 92));
    }

    @Override
    public void update(float dt){

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }
        this.renderer.render();
    }
}
