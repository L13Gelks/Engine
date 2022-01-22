package Engine;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements  JsonDeserializer<GameObject>  {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String name = obj.get("name").getAsString();
        JsonArray components = obj.getAsJsonArray("components");


        GameObject go = new GameObject(name);
        for(JsonElement e : components){
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }
        go.transform = go.getComponent(Transform.class);
        return go;
    }
}
