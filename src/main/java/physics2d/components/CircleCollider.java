package physics2d.components;

import components.Component;
import org.joml.Vector2f;
import renderer.DebugDraw;

public class CircleCollider extends Component {
    protected Vector2f offset = new Vector2f();
    private float radius = 1f;

    public float getRadius() {
        return radius;
    }
    public void setOffset(Vector2f newOffset) { this.offset.set(newOffset); }
    public Vector2f getOffset() {
        return this.offset;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void editorUpdate(float dt){
        Vector2f center = new Vector2f(this.gameObject.transform.position).add(this.offset);
        //Todo: same for box collider
        //DebugDraw.addCircle(center, this.radius);
    }
}
