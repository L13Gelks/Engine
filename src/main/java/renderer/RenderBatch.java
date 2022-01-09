package renderer;

import Engine.Window;
import components.Sprite;
import components.SpriteRenderer;
import org.joml.Vector2f;
import org.joml.Vector4f;
import util.AssetPool;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

public class RenderBatch implements Comparable<RenderBatch>{
    //vertex
    //Pos              Color                        texCoords     texID
    //float, float     float, float, float, float   float, float   float
    private final int POS_SIZE = 2;
    private final int COLOR_SIZE = 4;
    private final int TEXTURE_COORDINATES_SIZE = 2;
    private final int TEXTURE_ID_SIZE = 1;
    private final int ENTITY_ID_SIZE = 1;

    private final int POS_OFFSET = 0;
    private final int COLOR_OFFSET = POS_OFFSET + POS_SIZE * Float.BYTES;
    private final int TEXTURE_COORDINATES_OFFSET = COLOR_OFFSET + COLOR_SIZE * Float.BYTES;
    private final int TEXTURE_ID_OFFSET = TEXTURE_COORDINATES_OFFSET + TEXTURE_COORDINATES_SIZE * Float.BYTES;
    private final int ENTITY_ID_OFFSET = TEXTURE_ID_OFFSET + TEXTURE_ID_SIZE * Float.BYTES;
    private final int VERTEX_SIZE = 10;
    private final int VERTEX_SIZE_BYTES = VERTEX_SIZE * Float.BYTES;

    private SpriteRenderer[] sprites;
    private int numSprites;
    private boolean hasSpace;
    private float[] vertices;
    private int[] textureSlots = {0, 1, 2, 3, 4, 5, 6, 7};

    private int vaoID, vboID;
    private int maxBatchSize;
    private int zIndex;

    private List<Texture> textures;

    public RenderBatch(int maxBatchSize, int zIndex) {
        this.zIndex = zIndex;
        this.sprites = new SpriteRenderer[maxBatchSize];
        this.maxBatchSize = maxBatchSize;

        vertices = new float[maxBatchSize * 4 * VERTEX_SIZE];

        this.numSprites = 0;
        this.hasSpace = true;
        this.textures = new ArrayList<>();
    }

    public void start() {
        //Generate and bind vertex arr object
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        //Allocate space for vertices
        vboID =  glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, vertices.length * Float.BYTES, GL_DYNAMIC_DRAW);

        //Create and upload indices buffer
        int eboID = glGenBuffers();
        int[] indices = generateIndices();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, eboID);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, indices, GL_STATIC_DRAW);

        // Enable buffer attribute pointers
        glVertexAttribPointer(0, POS_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, POS_OFFSET);
        glEnableVertexAttribArray(0);

        glVertexAttribPointer(1, COLOR_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, COLOR_OFFSET);
        glEnableVertexAttribArray(1);

        glVertexAttribPointer(2, TEXTURE_COORDINATES_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_COORDINATES_OFFSET);
        glEnableVertexAttribArray(2);

        glVertexAttribPointer(3, TEXTURE_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, TEXTURE_ID_OFFSET);
        glEnableVertexAttribArray(3);

        glVertexAttribPointer(4, ENTITY_ID_SIZE, GL_FLOAT, false, VERTEX_SIZE_BYTES, ENTITY_ID_OFFSET);
        glEnableVertexAttribArray(4);
    }

    public void addSprite(SpriteRenderer spr){
        //get index and add render obj
        int index = this.numSprites;
        this.sprites[index] = spr;
        this.numSprites++;

        if(spr.getTexture() != null){
            if(!textures.contains(spr.getTexture())){
                textures.add(spr.getTexture());
            }
        }

        //Add properties to local vertices array
        loadVertexProperties(index);

        if(numSprites >= this.maxBatchSize){
            this.hasSpace = false;
        }
    }

    public void render() {
        boolean rebufferData = false;

        for(int i = 0; i < numSprites; i++){
            SpriteRenderer spr = sprites[i];
            if(spr.isDirty()){
                loadVertexProperties(i);
                spr.setClean();
                rebufferData = true;
            }
        }

        if(rebufferData){
            glBindBuffer(GL_ARRAY_BUFFER, vboID);
            glBufferSubData(GL_ARRAY_BUFFER, 0, vertices);
        }

        //Use shader
        Shader shader = Renderer.getBoundShader();
        shader.uploadMat4f("uProjection", Window.getScene().camera().getProjectionMatrix());
        shader.uploadMat4f("uView", Window.getScene().camera().getViewMatrix());
        for(int i = 0; i < textures.size(); i++){
            glActiveTexture(GL_TEXTURE0 + i + 1);
            textures.get(i).bind();
        }
        shader.uploadIntArray("uTextures", textureSlots);

        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glDrawElements(GL_TRIANGLES, this.numSprites * 6, GL_UNSIGNED_INT, 0);

        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        for(int i = 0; i < textures.size(); i++){
            textures.get(i).unbind();
        }
        shader.detach();
    }

    private void loadVertexProperties(int index){
        SpriteRenderer sprite = this.sprites[index];

        //Find offset within arr
        int offset = index * 4 * VERTEX_SIZE;

        Vector4f color = sprite.getColor();
        Vector2f[] TextureCoordinates = sprite.getTextureCoordinates();

        int textureID = 0;
        if(sprite.getTexture() != null){
            for(int i = 0; i < textures.size(); i++){
                if(textures.get(i).equals(sprite.getTexture())){
                    textureID = i + 1;
                    break;
                }
            }
        }


        //Add vertices with appropriate properties
        float xAdd = 1.0f;
        float yAdd = 1.0f;
        for (int i = 0; i < 4; i++){
            if(i == 1){
                yAdd = 0.0f;
            }else if(i == 2){
                xAdd = 0.0f;
            }else if(i == 3){
                yAdd = 1.0f;
            }
            //Load position
            vertices[offset] = sprite.gameObject.transform.position.x + (xAdd * sprite.gameObject.transform.scale.x);
            vertices[offset + 1] = sprite.gameObject.transform.position.y + (yAdd * sprite.gameObject.transform.scale.y);

            //Load color
            vertices[offset + 2] = color.x;
            vertices[offset + 3] = color.y;
            vertices[offset + 4] = color.z;
            vertices[offset + 5] = color.w;

            //Load texture coordinates
            vertices[offset + 6] = TextureCoordinates[i].x;
            vertices[offset + 7] = TextureCoordinates[i].y;

            vertices[offset + 8] = textureID;

            //Load ENTITY ID
            vertices[offset + 9] = sprite.gameObject.getUid() + 1;
            offset += VERTEX_SIZE;
        }

    }

    private int[] generateIndices(){
        int[] elements = new int[6 * maxBatchSize];
        for(int i = 0; i < maxBatchSize; i++){
            loadElementIndices(elements, i);
        }

        return elements;
    }

    private void loadElementIndices(int[] elements, int index){
        int offsetArrayIndex = 6 * index;
        int offset = 4 * index;

        //320 021 | 764 465

        elements[offsetArrayIndex] = offset + 3;
        elements[offsetArrayIndex + 1] = offset + 2;
        elements[offsetArrayIndex + 2] = offset + 0;

        elements[offsetArrayIndex + 3] = offset + 0;
        elements[offsetArrayIndex + 4] = offset + 2;
        elements[offsetArrayIndex + 5] = offset + 1;
    }

    public boolean hasSpace(){
        return this.hasSpace;
    }

    public boolean hasSTextureSpace(){
        return this.textures.size() < 8;
    }

    public boolean hasTexture(Texture texture){
        return this.textures.contains(texture);
    }

    public int zIndex(){
        return this.zIndex;
    }

    @Override
    public int compareTo(RenderBatch o) {
        return Integer.compare(this.zIndex, o.zIndex);
    }
}
