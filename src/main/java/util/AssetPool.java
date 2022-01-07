package util;

import components.SpriteSheet;
import renderer.Shader;
import renderer.Texture;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//Min 5:03:44

public class AssetPool {
    private static Map<String, Shader>  shaders = new HashMap<>();
    private static Map<String, Texture>  textures = new HashMap<>();
    private static Map<String, SpriteSheet>  spriteSheetMap = new HashMap<>();

    public static Shader getShader(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.shaders.containsKey(file.getAbsolutePath())){
            return shaders.get(file.getAbsolutePath());
        } else {
            Shader shader = new Shader(resourceName);
            shader.compile();
            AssetPool.shaders.put(file.getAbsolutePath(), shader);
            return shader;
        }
    }

    public static Texture getTexture(String resourceName){
        File file = new File(resourceName);
        if(AssetPool.textures.containsKey(file.getAbsolutePath())){
            return textures.get(file.getAbsolutePath());
        } else {
            Texture texture = new Texture(resourceName);
            AssetPool.textures.put(file.getAbsolutePath(), texture);
            return texture;
        }
    }

    public static void addSpriteSheet(String resourceName, SpriteSheet spriteSheet){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheetMap.containsKey(file.getAbsolutePath())){
            AssetPool.spriteSheetMap.put(file.getAbsolutePath(), spriteSheet);
        }
    }

    public static SpriteSheet getSpriteSheet(String resourceName){
        File file = new File(resourceName);
        if(!AssetPool.spriteSheetMap.containsKey(file.getAbsolutePath())){
            assert false: "Error: tried to access spritesheet" + resourceName;
        }
        //Pink image spritesheet
        return AssetPool.spriteSheetMap.getOrDefault(file.getAbsolutePath(), null);
    }
}
