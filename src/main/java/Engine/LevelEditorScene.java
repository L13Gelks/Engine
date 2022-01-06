package Engine;

import components.FontRenderer;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.lwjgl.BufferUtils;
import renderer.Shader;
import renderer.Texture;
import util.Time;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL30.*;

public class LevelEditorScene extends Scene{

    private float[] vertexArray = {
             //position                //color              //uv coordinates
             100.5f, 0.5f,   0.0f,    1.0f,0.0f,0.0f,1.0f,  1, 1, //Bottom right
             0.5f,   100.5f, 0.0f,    0.0f,1.0f,0.0f,1.0f,  0, 0, //Top left
             100.5f, 100.5f, 0.0f,    0.0f,0.0f,1.0f,1.0f,  1, 0, //Top right
             0.5f,   0.5f,   0.0f,    1.0f,1.0f,0.0f,1.0f,  0, 1 //Bottom left
    };
    private int[] elementArray = {
            2, 1, 0, //Top right Triangle
            0, 1, 3 //Bottom left Triangle
    };

    private int vaoID, vboID, eboID;

    private Shader defaultShader;
    private Texture testTexture;

    GameObject testObj;

    private boolean firstTime = false;

    public LevelEditorScene(){

    }

    @Override
    public void init(){
        System.out.println("creating test obj");
        this.testObj = new GameObject("test");
        this.testObj.addComponent(new FontRenderer());
        this.testObj.addComponent(new SpriteRenderer());
        this.addGameObjectToScene(this.testObj);

        this.camera = new Camera(new Vector2f());
        defaultShader = new Shader("assets/shaders/default.glsl");
        defaultShader.compile();
        this.testTexture = new Texture("assets/images/testImage.png");


        //generate vao, vbo, ebo Buffer objects and send to GPU
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        //Create float buffer of vertices
        FloatBuffer vertexBuffer = BufferUtils.createFloatBuffer(vertexArray.length);
        vertexBuffer.put(vertexArray).flip();

        //Create VBO upload vertex buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertexBuffer, GL_STATIC_DRAW);
        //Create indices and upload
        IntBuffer elementBuffer = BufferUtils.createIntBuffer(elementArray.length);
        elementBuffer.put(elementArray).flip();

         eboID = glGenBuffers();
         glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
         glBufferData(GL_ELEMENT_ARRAY_BUFFER, elementBuffer, GL_STATIC_DRAW);

         //Add vertex attribute pointers
        int positionSize = 3;
        int colorSize = 4;
        int uvSize = 2;
        int vertexSizeBytes = (positionSize + colorSize + uvSize) * Float.BYTES;
        glVertexAttribPointer(0, positionSize, GL_FLOAT, false, vertexSizeBytes, 0);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, colorSize, GL_FLOAT, false,vertexSizeBytes,positionSize * Float.BYTES);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, uvSize, GL_FLOAT, false, vertexSizeBytes, (positionSize + colorSize) * Float.BYTES);
        glEnableVertexAttribArray(2);
    }

    @Override
    public void update(float dt){
        camera.position.x -= dt * 50.f;
        defaultShader.use();

        //UPLOAD TEXTURE TO SHADER
        defaultShader.uploadTexture("TEXTURE_SAMPLER", 0);
        glActiveTexture(GL_TEXTURE0);
        testTexture.bind();

        defaultShader.uploadMat4f("uProjection", camera.getProjectionMatrix());
        defaultShader.uploadMat4f("uView", camera.getViewMatrix());
        defaultShader.uploadFloat("uTime", Time.getTime());
        //Bind VAO
        glBindVertexArray(vaoID);

        //Enable vertex attribute pointers
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, elementArray.length, GL_UNSIGNED_INT, 0);

        //Unbind
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);
        defaultShader.detach();

        if(!firstTime){
            GameObject go = new GameObject("G TEST 2");
            go.addComponent(new SpriteRenderer());
            this.addGameObjectToScene(go);
            firstTime = true;
        }

        for(GameObject go : this.gameObjects){
            go.update(dt);
        }
    }
}
