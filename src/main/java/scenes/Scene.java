package scenes;

import Engine.Camera;
import Engine.GameObject;
import Engine.GameObjectDeserializer;
import Engine.Transform;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import imgui.ImGui;
import org.joml.Vector2f;
import physics2d.Physics2D;
import renderer.Renderer;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Scene {
    private Renderer renderer;
    private Camera camera;
    private boolean isRunning;
    private List<GameObject> gameObjects;
    private List<GameObject> pendingGameObjects;
    private SceneInitializer sceneInitializer;
    private Physics2D physics2D;

    public Scene(SceneInitializer sceneInitializer) {
        this.sceneInitializer = sceneInitializer;
        this.physics2D = new Physics2D();
        this.renderer= new Renderer();
        this.gameObjects = new ArrayList<>();
        this.pendingGameObjects = new ArrayList<>();
        this.isRunning = false;
    }

    public void init() {
        this.camera = new Camera(new Vector2f());
        this.sceneInitializer.loadResources(this);
        this.sceneInitializer.init(this);
    }
    public Physics2D getPhysics() {
        return this.physics2D;
    }

    public void start(){
        for(int i = 0; i < gameObjects.size(); i++){
            GameObject go = gameObjects.get(i);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        isRunning = true;
    }

    public void addGameObjectToScene(GameObject go){
        if(!isRunning){
            gameObjects.add(go);
        } else {
            pendingGameObjects.add(go);
        }
    }

    public void editorUpdate(float dt){
        this.camera.adjustProjection();

        for(int i = 0; i < gameObjects.size(); i++){
            GameObject go = gameObjects.get(i);
            go.editorUpdate(dt);
            if(go.isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }

        for(GameObject go : pendingGameObjects){
            gameObjects.add(go);
            go.start();
            this.renderer.add(go);
            this.physics2D.add(go);
        }
        pendingGameObjects.clear();
    }

    public void update(float dt){
        this.camera.adjustProjection();
        this.physics2D.update(dt);

        for(int i = 0; i < gameObjects.size(); i++){
            GameObject go = gameObjects.get(i);
            go.update(dt);
            if(go.isDead()){
                gameObjects.remove(i);
                this.renderer.destroyGameObject(go);
                this.physics2D.destroyGameObject(go);
                i--;
            }
        }
    }

    public void render(){
        this.renderer.render();
    }

    public void destroy(){
        for(GameObject go : gameObjects){
            go.destroy();
        }
    }

    public <T extends Component> GameObject getGameObjectWith(Class<T> clasz){
        for(GameObject go : gameObjects){
            if(go.getComponent(clasz) != null){
                return go;
            }
        }

        return  null;
    }

    public List<GameObject> getGameObjects(){
        return this.gameObjects;
    }

    public GameObject getGameObject(int gameObjectId){
        Optional<GameObject> result = this.gameObjects.stream()
                .filter(gameObject -> gameObject.getUid() == gameObjectId)
                .findFirst();
        return result.orElse(null);
    }

    public Camera camera(){
        return this.camera;
    }

    public void imgui() {
        this.sceneInitializer.imgui();
    }

    public GameObject createGameObject(String name){
        GameObject go = new GameObject(name);
        go.addComponent(new Transform());
        go.transform = go.getComponent(Transform.class);
        return go;
    }

    public void save(){
        Gson gson = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(Component.class, new ComponentDeserializer()).
                registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).
                enableComplexMapKeySerialization().
                create();
        try{
            FileWriter writer = new FileWriter("level.txt");
            List<GameObject> obSerialize = new ArrayList<>();
            for(GameObject go : this.gameObjects){
                if(go.doSerialization()){
                    obSerialize.add(go);
                }
            }
            writer.write(gson.toJson(obSerialize));
            writer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void load(){
        Gson gson = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(Component.class, new ComponentDeserializer()).
                registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).
                enableComplexMapKeySerialization().
                create();

        String inFile = "";

        try{
            inFile = new String(Files.readAllBytes(Paths.get("level.txt")));
        }catch(IOException e){
            e.printStackTrace();
        }

        if(!inFile.equals("")) {
            int maxGoId = -1;
            int maxCompId = -1;
            GameObject[] objects = gson.fromJson(inFile, GameObject[].class);
            for(int i = 0; i < objects.length; i++){
                addGameObjectToScene(objects[i]);
                for(Component c : objects[i].getAllComponents()){
                    if(c.getUid() > maxCompId){
                        maxCompId = c.getUid();
                    }
                }
                if(objects[i].getUid() > maxGoId){
                    maxGoId = objects[i].getUid();
                }
            }
            maxGoId++;
            maxCompId++;
            GameObject.init(maxGoId);
            Component.init(maxCompId);
        }
    }
}
