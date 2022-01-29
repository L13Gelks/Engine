package Game.world;

import components.Component;
import editor.JImGui;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class World extends Component {
    public float currentTimeOfDay = 0.0f;
    public transient  float lengthOfHour = 1f;
    public transient  float lengthOfDay = lengthOfHour * 24f;

    public float getCurrentTimeOfDay(){
        float normalized;

        if(currentTimeOfDay < lengthOfDay / 2){
            normalized = currentTimeOfDay / (lengthOfDay / 2);
        } else {
            normalized = (lengthOfDay / currentTimeOfDay) - 1;
        }

        normalized += 0.1f;

        if(normalized > 1f){
            normalized = 1f;
        }

        return normalized;
    }

    public void update(float dt){
        lengthOfDay = lengthOfHour * 24f;
        if(this.lengthOfDay >= this.currentTimeOfDay){
            this.currentTimeOfDay += dt;
        }else{
            this.currentTimeOfDay = 0.0f;
        }
    }

    public void imgui() {
        ImGui.begin("World");
        imgui("World");
        ImGui.end();
    }

    //TODO: Improve this piece of code
    public void imgui(String string){
        Field[] fields =  this.getClass().getDeclaredFields();


        for(Field field : fields){
            try{
                boolean isPrivate = Modifier.isPrivate(field.getModifiers());
                if(isPrivate){
                    field.setAccessible(true);
                }
                Class type = field.getType();
                Object value = field.get(this);
                String name = field.getName();

                if(type == int.class){
                    int val = (int)value;
                    field.set(this, JImGui.dragInt(name, val));
                } else if(type == float.class){
                    float val = (float)value;
                    field.set(this, JImGui.dragFloat(name, val));
                } else if(type == boolean.class){
                    boolean val = (boolean)value;
                    if(ImGui.checkbox(name + ": ", val)){
                        field.set(this,  !val);
                    }
                } else if(type == Vector2f.class){
                    Vector2f val = (Vector2f)value;
                    JImGui.drawVec2Control(name, val);
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
                } else if (type == String.class) {
                    field.set(this, JImGui.inputText(field.getName() + ": ",
                            (String)value));
                }
                if(isPrivate){
                    field.setAccessible(false);
                }
            }catch (IllegalAccessException e) {e.printStackTrace();}
        }
    }
}
