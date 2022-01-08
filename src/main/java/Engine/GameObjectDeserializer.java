package Engine;

import com.google.gson.*;
import components.Component;

import java.lang.reflect.Type;

public class GameObjectDeserializer implements  JsonDeserializer<GameObject>  {
    @Override
    public GameObject deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject obj = json.getAsJsonObject();
        String name = obj.get("name").getAsString();
        JsonArray componnets = obj.getAsJsonArray("components");
        Transform transform = context.deserialize(obj.get("transform"), Transform.class);
        int zIndex = context.deserialize(obj.get("zIndex"), int.class);

        GameObject go = new GameObject(name, transform, zIndex);

        for(JsonElement e : componnets){
            Component c = context.deserialize(e, Component.class);
            go.addComponent(c);
        }
        return go;
    }
}
