package components;

import Engine.GameObject;
import imgui.ImGui;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class Component {
    private static int ID_COUNTER = 0;
    private int uid = -1;

    public  transient GameObject gameObject = null;

    public void start() {

    }

    public void update(float dt){

    };

    public void imgui(){
        try{
            Field[] fields = this.getClass().getDeclaredFields();
            for(Field field : fields){
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                boolean isTransient = Modifier.isTransient(field.getModifiers());
                if(isTransient){
                    continue;
                }
                if(isPrivate){
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int)value;
                    int[] inInt = {val};
                    if(ImGui.dragInt(name + ": ", inInt)){
                        field.set(this,  inInt[0]);
                    }
                } else if(type == float.class){
                    float val = (float)value;
                    float[] inFloat = {val};
                    if(ImGui.dragFloat(name + ": ", inFloat)){
                        field.set(this,  inFloat[0]);
                    }
                } else if(type == boolean.class){
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name + ": ", val)){
                        field.set(this,  !val);
                    }
                } else if(type == Vector3f.class){
                    Vector3f val = (Vector3f)value;
                    float[] imVec = {val.x, val.y, val.z};
                    if(ImGui.dragFloat3(name + ": ", imVec)){
                        val.set(imVec[0], imVec[1], imVec[2]);
                    }
                } else if(type == Vector4f.class){
                    Vector4f val = (Vector4f)value;
                    float[] imVec = {val.x, val.y, val.z, val.w};
                    if(ImGui.dragFloat4(name + ": ", imVec)){
                        val.set(imVec[0], imVec[1], imVec[2], imVec[3]);
                    }
                }
                if(isPrivate){
                    field.setAccessible(false);
                }
            }
        }catch (IllegalAccessException e) {e.printStackTrace();}
    }

    public void generateId(){
        if(this.uid == -1){
            this.uid = ID_COUNTER++;
        }
    }

    public int getUid(){
        return  this.uid;
    }

    public static  void init(int maxId){
        ID_COUNTER = maxId;
    }
}
