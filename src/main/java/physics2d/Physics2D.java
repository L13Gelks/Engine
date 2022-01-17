package physics2d;

import Engine.GameObject;
import Engine.Transform;
import Engine.Window;
import components.Ground;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.*;
import org.joml.Vector2f;
import org.joml.Vector3f;
import physics2d.components.Box2DCollider;
import physics2d.components.CircleCollider;
import physics2d.components.PillboxCollider;
import physics2d.components.RigidBody2D;
import renderer.DebugDraw;

import java.awt.*;

public class Physics2D {
    private Vec2 gravity = new Vec2(0, -10.0f);
    private World world = new World(gravity);

    private float physicsTime = 0.0f;
    private float physicsTimeStep = 1.0f / 61f;
    private int velocityIterations = 8;
    private int positionIterations = 3;

    public Physics2D() {
        world.setContactListener(new EngineContactListener());
    }

    public void add(GameObject go){
        RigidBody2D rb = go.getComponent(RigidBody2D.class);
        if (rb != null && rb.getRawBody() == null) {
            Transform transform = go.transform;

            BodyDef bodyDef = new BodyDef();
            bodyDef.angle = (float)Math.toRadians(transform.rotation);
            bodyDef.position.set(transform.position.x, transform.position.y);
            bodyDef.angularDamping = rb.getAngularDamping();
            bodyDef.linearDamping = rb.getLinearDamping();
            bodyDef.fixedRotation = rb.isFixedRotation();
            bodyDef.gravityScale = rb.gravityScale;
            bodyDef.angularVelocity = rb.angularVelocity;
            bodyDef.bullet = rb.isContinuousCollision();
            bodyDef.userData = rb.gameObject;
            switch (rb.getBodyType()) {
                case Kinematic: bodyDef.type = BodyType.KINEMATIC; break;
                case Static: bodyDef.type = BodyType.STATIC; break;
                case Dynamic: bodyDef.type = BodyType.DYNAMIC; break;
            }

            Body body = this.world.createBody(bodyDef);
            body.m_mass = rb.getMass();
            rb.setRawBody(body);
            CircleCollider circleCollider;
            Box2DCollider boxCollider;
            PillboxCollider pillboxCollider;

            if ((circleCollider = go.getComponent(CircleCollider.class)) != null) {
                addCircleCollider(rb, circleCollider);
            }

            if ((boxCollider = go.getComponent(Box2DCollider.class)) != null) {
                addBox2DCollider(rb, boxCollider);
            }

            if ((pillboxCollider = go.getComponent(PillboxCollider.class)) != null) {
                addPillboxCollider(rb, pillboxCollider);
            }
        }
    }

    public void destroyGameObject(GameObject gameObject){
        RigidBody2D rb = gameObject.getComponent(RigidBody2D.class);
        if (rb != null) {
            if (rb.getRawBody() != null) {
                world.destroyBody(rb.getRawBody());
                rb.setRawBody(null);
            }
        }
    }

    public void update(float dt){
        physicsTime += dt;
        if (physicsTime >= 0.0f) {
            physicsTime -= physicsTimeStep;
            world.step(physicsTimeStep, velocityIterations, positionIterations);
        }
    }

    public void addBox2DCollider(RigidBody2D rb, Box2DCollider boxCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        PolygonShape shape = new PolygonShape();
        Vector2f halfSize = new Vector2f(boxCollider.getHalfSize()).mul(0.5f);
        Vector2f offset = boxCollider.getOffset();
        Vector2f origin = new Vector2f(boxCollider.getOrigin());
        shape.setAsBox(halfSize.x, halfSize.y, new Vec2(offset.x, offset.y), 0);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = boxCollider.gameObject;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    public void addCircleCollider(RigidBody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        CircleShape shape = new CircleShape();
        shape.setRadius(circleCollider.getRadius());
        shape.m_p.set(circleCollider.getOffset().x, circleCollider.getOffset().y);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = rb.getFriction();
        fixtureDef.userData = circleCollider.gameObject;
        fixtureDef.isSensor = rb.isSensor();
        body.createFixture(fixtureDef);
    }

    public RaycastInfo raycast(GameObject requestingObject, Vector2f point1, Vector2f point2) {
        RaycastInfo callback = new RaycastInfo(requestingObject);
        world.raycast(callback, new Vec2(point1.x, point1.y),
                new Vec2(point2.x, point2.y));
        return callback;
    }

    private int fixtureListSize(Body body) {
        int size = 0;
        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            size++;
            fixture = fixture.m_next;
        }
        return size;
    }

    public boolean isLocked() {
        return world.isLocked();
    }

//    public static boolean checkOnGround(
//            GameObject gameObject,
//            float innerPlayerWidth,
//            float height) {
//        Vector2f raycastBegin = new Vector2f(gameObject.transform.position);
//        raycastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
//        Vector2f raycastEnd = new Vector2f(raycastBegin).add(0.0f, height);
//
//        RaycastInfo info = Window.getPhysics().raycast(gameObject, raycastBegin, raycastEnd);
//
//        Vector2f raycast2Begin = new Vector2f(raycastBegin).add(innerPlayerWidth, 0.0f);
//        Vector2f raycast2End = new Vector2f(raycastEnd).add(innerPlayerWidth, 0.0f);
//        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, raycast2Begin, raycast2End);
//
//        return (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
//                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);
//    }

    public void setIsSensor(RigidBody2D rb) {
        Body body = rb.getRawBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = true;
            fixture = fixture.m_next;
        }
    }

    public void setNotSensor(RigidBody2D rb) {
        Body body = rb.getRawBody();
        if (body == null) return;

        Fixture fixture = body.getFixtureList();
        while (fixture != null) {
            fixture.m_isSensor = false;
            fixture = fixture.m_next;
        }
    }

    public void resetCircleCollider(RigidBody2D rb, CircleCollider circleCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }
        addCircleCollider(rb, circleCollider);
        body.resetMassData();
    }

    public void resetBox2DCollider(RigidBody2D rb, Box2DCollider boxCollider) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addBox2DCollider(rb, boxCollider);
        body.resetMassData();
    }

    public void resetPillboxCollider(RigidBody2D rb, PillboxCollider pb) {
        Body body = rb.getRawBody();
        if (body == null) return;

        int size = fixtureListSize(body);
        for (int i = 0; i < size; i++) {
            body.destroyFixture(body.getFixtureList());
        }

        addPillboxCollider(rb, pb);
        body.resetMassData();
    }

    public void addPillboxCollider(RigidBody2D rb, PillboxCollider pb) {
        Body body = rb.getRawBody();
        assert body != null : "Raw body must not be null";

        addBox2DCollider(rb, pb.getBox());
        addCircleCollider(rb, pb.getTopCircle());
        addCircleCollider(rb, pb.getBottomCircle());
    }

    public Vector2f getGravity(){
        return new Vector2f(this.world.getGravity().x, this.world.getGravity().y);
    }

    public static boolean checkOnGround(
            GameObject gameObject,
            float innerPlayerWidth,
            float height) {

        Vector2f rayCastBegin = new Vector2f(gameObject.transform.position);
        rayCastBegin.sub(innerPlayerWidth / 2.0f, 0.0f);
        Vector2f rayCastEnd = new Vector2f(rayCastBegin).add(0.0f, height);

        RaycastInfo info = Window.getPhysics().raycast(gameObject, rayCastBegin, rayCastEnd);

        Vector2f rayCast2Begin = new Vector2f(rayCastBegin).add(innerPlayerWidth, 0.0f);
        Vector2f rayCast2End = new Vector2f(rayCastEnd).add(innerPlayerWidth, 0.0f);
        RaycastInfo info2 = Window.getPhysics().raycast(gameObject, rayCast2Begin, rayCast2End);

        DebugDraw.addLine2D(rayCastBegin, rayCastEnd, new Vector3f(1.0f, 0.0f, 0.0f));
        DebugDraw.addLine2D(rayCast2Begin, rayCast2End, new Vector3f(1.0f, 0.0f, 0.0f));

        return (info.hit && info.hitObject != null && info.hitObject.getComponent(Ground.class) != null) ||
                (info2.hit && info2.hitObject != null && info2.hitObject.getComponent(Ground.class) != null);
    }
}
