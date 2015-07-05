package logic;

import game.Game;
import graphics.Draw;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.newdawn.slick.opengl.Texture;

public class MapObject extends GameObject
{
    Sprite sprite;
    Texture spriteSheetTexture;

    public MapObject(float x, float y, Sprite sprite)
    {
        setX(x);
        setY(y);
        setW(32);
        setH(32);
        setSprite(sprite);
        this.spriteSheetTexture = ResourcesLoader.getTiledmapTexture();
    }

    public Sprite getSprite()
    {
        return sprite;
    }

    public void setSprite(Sprite sprite)
    {
        this.sprite = sprite;
    }

    public void render(int i, int j)
    {
        Draw.drawSpritedSquare(spriteSheetTexture, sprite, ((float) i - Game.getCameraX()) * 32, ((float) j - Game.getCameraY()) * 32, getW(), getH());

    }
}
