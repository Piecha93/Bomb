package logic;

import Physics.Physics;
import game.Game;
import graphics.Animation;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.newdawn.slick.opengl.Texture;

import java.util.List;
import java.util.Map;

public class Explosion extends GameObject
{
    private transient Texture  spriteSheetTexture;
    private transient Map<String, Sprite> spriteMap;
    private int right, left, up, down;
    boolean ended;
    int time;

    private transient Animation animationCenter;
    private transient Animation animationHorizontally;
    private transient Animation animationUpright;
    private transient Animation animationRight;
    private transient Animation animationLeft;
    private transient Animation animationUp;
    private transient Animation animationDown;
    private transient Animation currentAnimation;

    private Player owner;

    public Explosion(float x, float y, Player owner)
    {
        super(Math.round(x), Math.round(y), 32, 32);
        time = 0;
        ended = false;
        this.owner = owner;
        right = left = up = down = 0;
        loadResources();
    }

    public Explosion(Explosion exp)
    {
        super(exp.getX(), exp.getY(), 32, 32);
        this.time = exp.time;
        this.ended = exp.ended;
        this.right = exp.right;
        this.left = exp.left;
        this.up = exp.up;
        this.down = exp.down;
        loadResources();
    }

    public void loadResources()
    {
        spriteMap = ResourcesLoader.getExplosionSprites();
        spriteSheetTexture = ResourcesLoader.getExplosionTexture();

        animationCenter = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("center"),}, 50, true));
        animationHorizontally = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("horizontally"),}, 50, true));
        animationUpright = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("upright"),}, 50, true));
        animationRight = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("rightend"),}, 50, true));
        animationLeft = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("leftend"),}, 50, true));
        animationUp = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("downend"),}, 50, true));
        animationDown = (new Animation(spriteSheetTexture, new Sprite[]{spriteMap.get("upend"),}, 50, true));
    }

    public void update(int delta)
    {
        time += delta;
        if (time > 400) ended = true;
    }

    public void add(int dir)
    {
        switch (dir)
        {
            case 0:
                up++;
                break;
            case 1:
                down++;
                break;
            case 2:
                right++;
                break;
            case 3:
                left++;
                break;
        }
    }

    public void render()
    {
        currentAnimation = animationCenter;
        float x = Physics.rollVar(getX() - Game.getCameraX(), Game.getSizeX());
        float y = Physics.rollVar(getY() - Game.getCameraY(), Game.getSizeY());

        currentAnimation.render(x * 32, y * 32, getW(), getH());
        currentAnimation.render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32, getW(), getH());
        currentAnimation.render(x * 32, (y - Game.getSizeY()) * 32, getW(), getH());
        currentAnimation.render((x - Game.getSizeX()) * 32, y * 32, getW(), getH());

        for (int i = 0; i < right; i++)
        {
            if (i == (right - 1)) currentAnimation = animationRight;
            else currentAnimation = animationUpright;
            x = Physics.rollVar(getX() + i + 1 - Game.getCameraX(), Game.getSizeX());
            y = Physics.rollVar(getY() - Game.getCameraY(), Game.getSizeY());

            currentAnimation.render(x * 32, y * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render(x * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, y * 32, getW(), getH());
        }

        for (int i = 0; i < left; i++)
        {
            if (i == (left - 1)) currentAnimation = animationLeft;
            else currentAnimation = animationUpright;
            x = Physics.rollVar(getX() - i - 1 - Game.getCameraX(), Game.getSizeX());
            y = Physics.rollVar(getY() - Game.getCameraY(), Game.getSizeY());

            currentAnimation.render(x * 32, y * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render(x * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, y * 32, getW(), getH());
        }

        for (int i = 0; i < up; i++)
        {
            if (i == (up - 1)) currentAnimation = animationUp;
            else currentAnimation = animationHorizontally;
            x = Physics.rollVar(getX() - Game.getCameraX(), Game.getSizeX());
            y = Physics.rollVar(getY() + i + 1 - Game.getCameraY(), Game.getSizeY());

            currentAnimation.render(x * 32, y * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render(x * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, y * 32, getW(), getH());
        }

        for (int i = 0; i < down; i++)
        {
            if (i == (down - 1)) currentAnimation = animationDown;
            else currentAnimation = animationHorizontally;
            x = Physics.rollVar(getX() - Game.getCameraX(), Game.getSizeX());
            y = Physics.rollVar(getY() - i - 1 - Game.getCameraY(), Game.getSizeY());

            currentAnimation.render(x * 32, y * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render(x * 32, (y - Game.getSizeY()) * 32, getW(), getH());
            currentAnimation.render((x - Game.getSizeX()) * 32, y * 32, getW(), getH());
        }
    }

    public void checkCollision(List<Bomb> bombs, List<Player> players)
    {
        for (Bomb bomb : bombs)
        {
            for (int i = -left; i <= right; i++)
            {
                GameObject tmpExplosion = new GameObject(this.getX() + i, this.getY(), 32, 32);
                if (Physics.checkCollision(bomb, tmpExplosion))
                {
                    bomb.bang();
                }
            }
            for (int i = -down; i <= up; i++)
            {
                GameObject tmpExplosion = new GameObject(this.getX(), this.getY() + i, 32, 32);
                if (Physics.checkCollision(bomb, tmpExplosion))
                {
                    bomb.bang();
                }
            }
        }

        for (Player player : players)
        {
            checkCollision(player);
        }
    }

    public void checkCollision(Player player)
    {
        if(player != null && owner != null)
        {
            for (int i = -left; i <= right; i++)
            {
                GameObject tmpExplosion = new GameObject(this.getX() + i, this.getY(), 25, 25);
                if (Physics.checkCollision(player, tmpExplosion))
                {
                    if (player != owner && player.isAlive())
                    {
                        owner.incKills();
                        player.death();
                    } else if (player.isAlive())
                    {
                        player.death();
                    }
                }
            }

            for (int i = -down; i <= up; i++)
            {
                GameObject tmpExplosion = new GameObject(this.getX(), this.getY() + i, 25, 25);
                if (Physics.checkCollision(player, tmpExplosion))
                {
                    if (player != owner && player.isAlive())
                    {
                        owner.incKills();
                        player.death();
                    } else if (player.isAlive())
                    {
                        player.death();
                    }
                }
            }
        }
    }

    public boolean isEnded()
    {
        return ended;
    }

    public int getRight()
    {
        return right;
    }

    public int getLeft()
    {
        return left;
    }

    public int getUp()
    {
        return up;
    }

    public int getDown()
    {
        return down;
    }

    public Player getOwner()
    {
        return owner;
    }

    public void setOwner(Player owner)
    {
        this.owner = owner;
    }
}
