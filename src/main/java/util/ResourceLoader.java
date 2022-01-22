package util;

import components.SpriteSheet;

public class ResourceLoader {
    public static void loadResources(){
        AssetPool.getShader("assets/shaders/default.glsl");
        AssetPool.getTexture("assets/images/testImage.png");

        AssetPool.addSpriteSheet("assets/Images/gizmos.png",
                new SpriteSheet(AssetPool.getTexture("assets/Images/gizmos.png"),
                        24, 48, 3, 0));

        loadPlayerResources();
        loadObjectsResources();
        loadEnemiesResources();
        loadWorldResources();
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //This Section Contains Sprites For The Main Character
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void loadPlayerResources(){
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
        AssetPool.addSpriteSheet("assets/Sprites/Characters/chest.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/chest.png"),
                        512, 512, 30, 0));
        AssetPool.addSpriteSheet("assets/Sprites/Characters/heal1.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Characters/heal1.png"),
                        512, 512, 91, 0));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //This Section Contains Sprites For All Objects
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void loadObjectsResources(){
        AssetPool.addSpriteSheet("assets/images/sword.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/sword.png"),
                        321, 321, 1, 0));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //This Section Contains Sprites For Enemies
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void loadEnemiesResources(){
        AssetPool.addSpriteSheet("assets/images/testDummy.png",
                new SpriteSheet(AssetPool.getTexture("assets/images/testDummy.png"),
                        716, 1134, 1, 0));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //This Section Contains Sprites For The World
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private static void loadWorldResources(){
        AssetPool.addSpriteSheet("assets/Sprites/Tiles/NaturalWorld.png",
                new SpriteSheet(AssetPool.getTexture("assets/Sprites/Tiles/NaturalWorld.png"),
                        128, 127, 18, 0));
    }
}
