package components;

import org.joml.Vector2f;
import renderer.Texture;
import java.util.ArrayList;
import java.util.List;

public class SpriteSheet {
    private Texture texture;
    private List<Sprite> sprites;

    public SpriteSheet(Texture texture, int spriteWidth, int spriteHeight, int numSprites, int spacing){
        this.sprites = new ArrayList<>();

        this.texture = texture;
        int currentX = 0;
        int currentY = texture.getTextureHeight() - spriteHeight;

        for(int i = 0; i < numSprites; i++){
            float topY = (currentY + spriteHeight) / (float)texture.getTextureHeight();
            float rightX = (currentX + spriteWidth) / (float)texture.getTextureWidth();
            float leftX = currentX / (float)texture.getTextureWidth();
            float bottomY = currentY / (float)texture.getTextureHeight();

            Vector2f[] texCoords = {
                    new Vector2f(rightX, topY),
                    new Vector2f(rightX, bottomY),
                    new Vector2f(leftX, bottomY),
                    new Vector2f(leftX, topY)
            };

            Sprite sprite = new Sprite();
            sprite.setTexture(this.texture);
            sprite.setTextureCoordinates(texCoords);
            sprite.setWidth(spriteWidth);
            sprite.setHeight(spriteHeight);
            this.sprites.add(sprite);

            currentX += spriteWidth + spacing;
            if(currentX >= texture.getTextureWidth()){
                currentX = 0;
                currentY -= spriteHeight + spacing;
            }
        }
    }

    public Sprite getSprite(int index){
        return this.sprites.get(index);
    }

    public int size(){
        return this.sprites.size();
    }
}
