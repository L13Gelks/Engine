package scenes;

import Engine.*;
import components.*;
import imgui.ImGui;
import util.AssetPool;
import util.Factories.DecorationBlocksFactory;
import util.Factories.PrefabsFactory;
import util.Factories.SolidBlocksFactory;
import util.ResourceLoader;

public class LevelEditorSceneInitializer extends SceneInitializer {
    private GameObject levelEditorStuff;
    public static transient boolean isPlayerGenerated = false;

    public LevelEditorSceneInitializer(){

    }

    @Override
    public void init(Scene scene){
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
        //Load textures and shaders for the game
        ResourceLoader.loadResources();

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
    public void imGui() {
        ImGui.begin("Level Editor");
        levelEditorStuff.imgui();
        ImGui.end();

        ImGui.begin("Objects");

        if(ImGui.beginTabBar("Window TapBar")) {
            if(ImGui.beginTabItem("Solid Blocks")) {
                SolidBlocksFactory solidBlocksFactory = new SolidBlocksFactory(levelEditorStuff);
                ImGui.endTabItem();
            }
            if(ImGui.beginTabItem("Decoration Blocks")) {
                DecorationBlocksFactory decorationBlocksFactory = new DecorationBlocksFactory(levelEditorStuff);
                ImGui.endTabItem();
            }
            if(ImGui.beginTabItem("Prefabs")) {
                PrefabsFactory prefabsFactory = new PrefabsFactory(levelEditorStuff);
                if(!isPlayerGenerated){
                    prefabsFactory.generatePlayer();
                }
                prefabsFactory.generateTestDummy();
                ImGui.sameLine();
                prefabsFactory.generateWoodenChest();
                ImGui.sameLine();
                prefabsFactory.generateLight();
                ImGui.endTabItem();
            }
            ImGui.endTabBar();
        }
        ImGui.end();
    }
}
