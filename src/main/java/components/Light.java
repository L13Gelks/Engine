package components;

import org.joml.Vector2f;
import org.joml.Vector4f;

public class Light extends  Component{
    public int lightId = 0;
    public Vector4f color = new Vector4f(256,256,256,2560);
    public Vector2f location = new Vector2f(0,0);;
    public float luminosity = 1.0f;
    public float radius = 1.0f;

    @Override
    public void update(float dt){

    }
}
