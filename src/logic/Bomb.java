package logic;

import Physics.Physics;
import game.Game;
import graphics.Animation;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.newdawn.slick.opengl.Texture;

import java.util.Map;

public class Bomb extends GameObject
{
    private transient Texture spriteSheetTexture;
    private transient Map<String, Sprite> spriteMap;

    private transient Animation animation1;
    private transient Animation animation2;
    private transient Animation animation3;
    private transient Animation currentAnimation;

    private int explodeTime;
    private int explodePower;
    private boolean exploded;
    private Player owner;

    public Bomb(float x, float y, Player owner)
    {
        super(Math.round(x), Math.round(y), 32, 32);

        setPlayer(owner);
        explodeTime = 3000;
        explodePower = owner.getBombPower();
        exploded = false;
        loadResources();
    }

    public Bomb(Bomb bomb)
    {
        super(bomb.getX(), bomb.getY(), 32, 32);

        setPlayer(bomb.getOwner());
        this.explodeTime = bomb.explodeTime;
        this.explodePower = bomb.explodePower;
        this.exploded = bomb.exploded;
        loadResources();
    }

    public void loadResources()
    {
        spriteMap = ResourcesLoader.getBombSprites();
        spriteSheetTexture = ResourcesLoader.getBombTexture();
        animation1 = new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("bomb1"), spriteMap.get("bomb2"), spriteMap.get("bomb3"),}, 50, true);
        animation2 = new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("bomb4"), spriteMap.get("bomb5"), spriteMap.get("bomb6"),}, 50, true);
        animation3 = new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("bomb7"), spriteMap.get("bomb8"), spriteMap.get("bomb9")}, 50, true);
        currentAnimation = animation1;
    }

    public void update(int delta)
    {
        if (!exploded)
        {
            explodeTime -= delta;
            if (explodeTime <= 0) bang();

            if (explodeTime > 2000) currentAnimation = animation1;
            else if (explodeTime > 1000) currentAnimation = animation2;
            else currentAnimation = animation3;
            currentAnimation.update(delta);
        }
    }

    public void update(Bomb bomb)
    {
        explodeTime = bomb.explodeTime;
        exploded = bomb.exploded;
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

    public void setPlayer(Player owner)
    {
        this.owner = owner;
    }

    public boolean isExplode()
    {
        return exploded;
    }

    public int getExplodePower()
    {
        return explodePower;
    }

    public Player getOwner()
    {
        return owner;
    }

    public void bang()
    {
        owner.setBombQuanity(owner.getBombQuanity() + 1);
        exploded = true;
    }
}
