package Game.entity;

import components.Component;
import editor.JImGui;
import imgui.ImGui;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class Enemy extends Entity {
    //TODO: Improve this piece of code
    public void imgui(String string){
        int counter = 0;
        Field[] fields = null;
        Field[] firstArray = this.getClass().getDeclaredFields();
        Field[] secondArray = this.getClass().getSuperclass().getSuperclass().getDeclaredFields();
        int fal = firstArray.length;
        int sal = secondArray.length;
        fields = new Field[fal + sal];
        System.arraycopy(firstArray, 0, fields, 0, fal);
        System.arraycopy(secondArray, 0, fields, fal, sal);

        for(Field field : fields){
            switch (counter) {
                case 10:
                    ImGui.text("Levels");
                    break;
                case 18:
                    ImGui.text("Stats");
                    break;
                case 24:
                    ImGui.text("Stats Extra");
                    break;
                case 29:
                    ImGui.text("Stats Offensive and Defensive");
                    break;
                case 38:
                    ImGui.text("Resistances");
                    break;
            }
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
            counter++;
        }
    }
}
