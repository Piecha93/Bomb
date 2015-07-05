package logic;

import Physics.Physics;
import game.Game;
import graphics.Animation;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.newdawn.slick.opengl.Texture;

import java.util.Map;

public class Bonus extends GameObject
{
    private transient Texture spriteSheetTexture;
    private transient Map<String, Sprite> spriteMap;
    private transient Animation currentAnimation;

    private BONUS_TYPE type;

    public Bonus(float x, float y, BONUS_TYPE type)
    {
        super(x, y, 32, 32);
        this.type = type;
        loadResources();
    }

    public Bonus(Bonus bonus)
    {
        super(bonus.getX(), bonus.getY(), 32, 32);
        this.type = bonus.type;
        loadResources();
    }

    public void loadResources()
    {
        spriteMap = ResourcesLoader.getBonusSprites();
        spriteSheetTexture = ResourcesLoader.getBonusTexture();

        Sprite tmpSprite = null;
        if (type == BONUS_TYPE.BOMB) tmpSprite = spriteMap.get("bomb");
        if (type == BONUS_TYPE.POWER) tmpSprite = spriteMap.get("power");
        if (type == BONUS_TYPE.SPEED) tmpSprite = spriteMap.get("speed");

        currentAnimation = new Animation(spriteSheetTexture, new Sprite[]{tmpSprite}, 50, true);
        currentAnimation.bounceStart();
    }

    public void update(int delta)
    {
        currentAnimation.update(delta);
    }

    public void render()
    {
        float x = Physics.rollVar(getX() - Game.getCameraX(), Game.getSizeX());
        float y = Physics.rollVar(getY() - Game.getCameraY(), Game.getSizeY());

        currentAnimation.render(x * 32, y * 32, getW(), getH());
        currentAnimation.render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32, getW(), getH());
        currentAnimation.render(x * 32, (y - Game.getSizeY()) * 32, getW(), getH());
        currentAnimation.render((x - Game.getSizeX()) * 32, y * 32, getW(), getH());
    }

    public BONUS_TYPE getType()
    {
        return type;
    }
}
