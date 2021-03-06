package renderer;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.stb.STBImage.*;

public class Texture {
    private String filepath;
    private transient int textureID;
    private int textureWidth, textureHeight;

    public Texture(){
        textureID = -1;
        this.textureWidth = -1;
        this.textureHeight = -1;
    }

    public Texture(int width, int height){
        this.filepath = "Generated";

        //Generate textures on gpu
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width, height,
                0, GL_RGB, GL_UNSIGNED_BYTE, 0);
    }

    public void init(String filepath){
        this.filepath = filepath;
        //Generate textures on gpu
        textureID = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, textureID);

        //Set texture params
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);

        IntBuffer width = BufferUtils.createIntBuffer(1);
        IntBuffer height = BufferUtils.createIntBuffer(1);
        IntBuffer channels = BufferUtils.createIntBuffer(1);
        stbi_set_flip_vertically_on_load(true);
        ByteBuffer image = stbi_load(filepath, width, height, channels, 0);

        if(image != null){
            this.textureWidth = width.get(0);
            this.textureHeight = height.get(0);

            if(channels.get(0) == 3){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGB, width.get(0), height.get(0),
                        0, GL_RGB, GL_UNSIGNED_BYTE, image);
            }else if(channels.get(0) == 4){
                glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width.get(0), height.get(0),
                        0, GL_RGBA, GL_UNSIGNED_BYTE, image);
            }else{
                assert false: "Error (Texture) UNKNOWN number of channels: " + channels.get(0);
            }

        }else{
            assert false: "Error (Texture) Could not load image " + filepath;
        }

        stbi_image_free(image);
    }

    public int getTextureWidth(){
        return textureWidth;
    }

    public int getTextureHeight(){
        return textureHeight;
    }

    public String getFilepath(){
        return  this.filepath;
    }

    public void bind(){
        glBindTexture(GL_TEXTURE_2D, textureID);
    }

    public void unbind(){
        glBindTexture(GL_TEXTURE_2D, 0);
    }

    public int getID(){
        return textureID;
    }

    @Override
    public boolean equals(Object o){
        if(o == null) return false;
        if(!(o instanceof Texture)) return false;
        Texture t = (Texture)o;
        return t.getTextureWidth() == this.textureWidth && t.getTextureHeight() == this.getTextureHeight()
        && t.getID() == this.textureID && t.getFilepath().equals(this.filepath);
    }
}
