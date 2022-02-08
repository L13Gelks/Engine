package Engine;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import components.Component;
import components.ComponentDeserializer;
import components.SpriteRenderer;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import renderer.DebugDraw;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

public class GameObject {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public String name;
    private List<Component> components;
    public transient Transform transform;
    private boolean doSerialization = true;

    private boolean isDead = false;

    //create init!!!!!!!!!!
    public GameObject(String name){
        this.name = name;
        this.components = new ArrayList<>();

        this.uid = ID_COUNTER++;
    }

    public <T extends Component> T getComponent(Class<T> componentClass){
        for(Component c : components){
            if(componentClass.isAssignableFrom(c.getClass())){
                try {
                    return componentClass.cast(c);
                } catch(ClassCastException e){
                    e.printStackTrace();
                    assert false: "Error casting component";
                }
            }
        }
        return null;
    }

    public <T extends Component> void removeComponent(Class<T> componentClass){
        for(int i = 0; i < components.size(); i++){
            Component c = components.get(i);
            if(componentClass.isAssignableFrom(c.getClass())){
                components.remove(i);
                return;
            }
        }
    }

    public void addComponent(Component c){
        c.generateId();
        this.components.add(c);
        c.gameObject = this;
    }

    public void editorUpdate(float dt){
        //TODO: CHANGE CODE FOR THIS
        List<GameObject> gos = Window.getImGuiLayer().getPropertiesWindow().getActiveGameObjects();
        for(GameObject go : gos) {
            Vector2f center = new Vector2f(go.transform.position);
            DebugDraw.addBox2D(center, new Vector2f(.25f,.25f), go.transform.rotation, new Vector3f(0.4f, 0.2f, 0.2f), -1);
        }

        for (Component component : components) {
            component.editorUpdate(dt);
        }
    }

    public void update(float dt){
        for (Component component : components) {
            component.update(dt);
        }
    }

    public void start(){
        for(int i = 0; i < components.size(); i++){
            components.get(i).start();
        }
    }

    public void imgui(){
        for(Component c : components){
            if(ImGui.collapsingHeader(c.getClass().getSimpleName())){
                c.imgui();
            }
        }
    }

    public void destroy(){
        this.isDead = true;
        for(int i = 0; i < components.size(); i++){
            components.get(i).destroy();
        }
    }

    public GameObject copy(){
        //TODO
        Gson gson = new GsonBuilder().
                setPrettyPrinting().
                registerTypeAdapter(Component.class, new ComponentDeserializer()).
                registerTypeAdapter(GameObject.class, new GameObjectDeserializer()).
                enableComplexMapKeySerialization().
                create();
        String objAsJson = gson.toJson(this);
        GameObject obj = gson.fromJson(objAsJson, GameObject.class);
        obj.generateId();
        for (Component c : obj.getAllComponents()){
            c.getUid();
        }

        SpriteRenderer spriteRenderer = obj.getComponent(SpriteRenderer.class);
        if(spriteRenderer != null && spriteRenderer.getTexture() != null){
            spriteRenderer.setTexture(AssetPool.getTexture(spriteRenderer.getTexture().getFilepath()));
        }

        return obj;
    }

    public boolean isDead(){
        return  this.isDead;
    }

    public int getUid(){
        return  this.uid;
    }

    public static  void init(int maxId){
        ID_COUNTER = maxId;
    }

    public List<Component> getAllComponents(){
        return this.components;
    }

    public void setNoSerialize(){
        this.doSerialization = false;
    }

    public boolean doSerialization(){
        return this.doSerialization;
    }

    public void generateId(){
        this.uid = ID_COUNTER++;
    }
}
