package logic;

import Physics.Physics;
import game.Game;
import graphics.Animation;
import graphics.Draw;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.newdawn.slick.opengl.Texture;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import static logic.DIRECTION.*;

public class Player extends GameObject implements Serializable
{
    private boolean isMoveUp;
    private boolean isMoveDown;
    private boolean isMoveLeft;
    private boolean isMoveRight;
    private transient Animation animationUp;
    private transient Animation animationDown;
    private transient Animation animationLeft;
    private transient Animation animationRight;
    private transient Animation animationDeath;
    private transient Animation currentAnimation;

    private DIRECTION dir;
    private float speed;
    private int bombQuanity;
    private int bombPower;
    private int bombsDropped;
    private boolean isAlive;
    private int timeToSpawn;
    private int kills;
    private int wins;
    private int lost;

    private String nickname;

    private transient Texture playerTexture;
    private transient Map<String, Sprite> playerSpriteMap;
    private transient Texture deathTexture;
    private transient Map<String, Sprite> deathSpriteMap;

    private final int BOMBS_QUANITY = 1;
    private final int BOMBS_POWER = 1;
    private final float SPEED = 0.4f;

    int ID;

    public Player(float x, float y)
    {
        super(x, y, 28, 28);
        setDirection(DOWN);
        speed = SPEED;
        bombsDropped = 0;
        bombPower = BOMBS_POWER;
        bombQuanity = BOMBS_QUANITY;
        isAlive = true;
        kills = 0;
        nickname = "DEFAULT";
        timeToSpawn = -1;
        wins = 0;
        lost = 0;
        loadResources();
    }

    public Player(Player player)
    {
        super(player.getX(), player.getY(), 28, 28);
        speed = player.speed;
        bombsDropped = player.bombsDropped;
        bombPower = player.bombPower;
        bombQuanity = player.bombQuanity;
        isAlive = player.isAlive();
        kills = player.kills;
        nickname = player.nickname;
        ID = player.ID;
        dir = player.dir;
        isMoveUp = player.isMoveUp;
        isMoveDown = player.isMoveDown;
        isMoveRight = player.isMoveRight;
        isMoveLeft = player.isMoveLeft;
        timeToSpawn = player.timeToSpawn;
        wins = player.wins;
        lost = player.lost;
        loadResources();
    }

    public Player(int x, int y, String nickname)
    {
        this(x, y);
        this.nickname = nickname;
    }

    public void loadResources()
    {
        playerSpriteMap = ResourcesLoader.getPlayerSprites();
        playerTexture = ResourcesLoader.getPlayerTexture();
        deathTexture = ResourcesLoader.getPlayerDeathTexture();
        deathSpriteMap = ResourcesLoader.getPlayerDeathSprites();

        animationUp = new Animation(playerTexture, new Sprite[]{playerSpriteMap.get("back1"), playerSpriteMap.get("back2"), playerSpriteMap.get("back3"), playerSpriteMap.get("back4")}, 100, false);
        animationDown = new Animation(playerTexture, new Sprite[]{playerSpriteMap.get("front1"), playerSpriteMap.get("front2"), playerSpriteMap.get("front3"), playerSpriteMap.get("front4")}, 100, false);
        animationLeft = new Animation(playerTexture, new Sprite[]{playerSpriteMap.get("left1"), playerSpriteMap.get("left2"), playerSpriteMap.get("left3"), playerSpriteMap.get("left4")}, 100, false);
        animationRight = new Animation(playerTexture, new Sprite[]{playerSpriteMap.get("right1"), playerSpriteMap.get("right2"), playerSpriteMap.get("right3"), playerSpriteMap.get("right4")}, 100, false);
        animationDeath = new Animation(deathTexture, new Sprite[]{deathSpriteMap.get("smoke1"), deathSpriteMap.get("smoke2"), deathSpriteMap.get("smoke3"), deathSpriteMap.get("smoke4")}, 150, false);
        currentAnimation = animationDown;
        setAnimation();
    }

    public void update(int delta)
    {
        update(delta, true);
    }

    public void update(int delta, boolean resetMove)
    {
        if (isAlive)
        {
            dir = NONE;
            if (isMoveUp)
            {
                dir = UP;
                setY(getY() - speed * delta / 100);
            }
            if (isMoveDown)
            {
                dir = DOWN;
                setY(getY() + speed * delta / 100);
            }
            if (isMoveLeft)
            {
                dir = LEFT;
                setX(getX() - speed * delta / 100);
            }
            if (isMoveRight)
            {
                dir = RIGHT;
                setX(getX() + speed * delta / 100);
            }

            setX(Physics.rollVar(getX(), Game.getSizeX()));
            setY(Physics.rollVar(getY(), Game.getSizeY()));
            setAnimation();

            if(resetMove)
            {
                isMoveUp = false;
                isMoveDown = false;
                isMoveLeft = false;
                isMoveRight = false;
            }
        }
        else
            timeToSpawn -= delta;

        if(currentAnimation != null)
            currentAnimation.update(delta);
    }

    public void update(Player player)
    {
        setX(player.getX());
        setY(player.getY());
        speed = player.speed;
        bombsDropped = player.bombsDropped;
        bombPower = player.bombPower;
        bombQuanity = player.bombQuanity;
        kills = player.kills;
        nickname = player.nickname;
        ID = player.ID;
        dir = player.dir;
        isMoveUp = player.isMoveUp;
        isMoveDown = player.isMoveDown;
        isMoveRight = player.isMoveRight;
        isMoveLeft = player.isMoveLeft;
        timeToSpawn = player.timeToSpawn;
        wins = player.wins;
        lost = player.lost;

        if(isAlive() == true && player.isAlive() == false)
            death();
        isAlive = player.isAlive();
    }

    public void setAnimation()
    {
        switch (dir)
        {
            case UP:
                currentAnimation = animationUp;
                currentAnimation.start();
                break;
            case DOWN:
                currentAnimation = animationDown;
                currentAnimation.start();
                break;
            case LEFT:
                currentAnimation = animationLeft;
                currentAnimation.start();
                break;
            case RIGHT:
                currentAnimation = animationRight;
                currentAnimation.start();
                break;
            case NONE:
                if(currentAnimation != null)
                    currentAnimation.stop();
                else
                    currentAnimation = animationDown;
        }
    }

    public Bomb setBomb(List<Bomb> bombs)
    {
        if (isAlive)
        {
            boolean set = true;
            if (bombsDropped >= bombQuanity) set = false;
            for (Bomb bomb : bombs)
            {
                if (bomb.getX() == Math.round(getX()) && bomb.getY() == Math.round(getY())) set = false;
            }
            if (set)
            {
                bombsDropped++;
                return new Bomb(getX(), getY(), this);
            }
        }
        return null;
    }

    public void render()
    {
        render(0, 0);
    }

    public void render(float x, float y)
    {
        if(currentAnimation != null)
            currentAnimation.render(x - 6, y - 6, 42, 42);
        if (isAlive)
            Draw.drawString(ResourcesLoader.getFont(), x - nickname.length() / 2, y - 20, nickname);
    }

    public void renderXY()
    {
        float x = Physics.rollVar(getX() - Game.getCameraX(), Game.getSizeX());
        float y = Physics.rollVar(getY() - Game.getCameraY(), Game.getSizeY());

        render(x * 32, y * 32);
        render((x - Game.getSizeX()) * 32, (y - Game.getSizeY()) * 32);
        render(x * 32, (y - Game.getSizeY()) * 32);
        render((x - Game.getSizeX()) * 32, y * 32);
    }

    public void chechCollision(Area map, List<Bomb> bombs, int delta)
    {
        GameObject tmpPlayerY = new GameObject(this);
        GameObject tmpPlayerX = new GameObject(this);
        GameObject tmpObj;

        if (isMoveUp) tmpPlayerY.setY(tmpPlayerY.getY() - speed * delta / 100);
        if (isMoveDown) tmpPlayerY.setY(tmpPlayerY.getY() + speed * delta / 100);

        if (isMoveLeft) tmpPlayerX.setX(tmpPlayerX.getX() - speed * delta / 100);
        if (isMoveRight) tmpPlayerX.setX(tmpPlayerX.getX() + speed * delta / 100);

        for (int i = -1; i <= 1; i++)
        {
            for (int j = -1; j <= 1; j++)
            {
                tmpObj = map.getObject(Physics.rollVar((int) tmpPlayerY.getX() + j, Game.getSizeX()), Physics.rollVar((int) tmpPlayerY.getY() + i, Game.getSizeY()));

                if (!(tmpObj instanceof EmptyObject) && Physics.checkCollision(tmpObj, tmpPlayerY))
                {
                    isMoveUp = false;
                    isMoveDown = false;
                }
                if (!(tmpObj instanceof EmptyObject) && Physics.checkCollision(tmpObj, tmpPlayerX))
                {
                    isMoveLeft = false;
                    isMoveRight = false;
                }
            }
        }
        for (Bomb bomb : bombs)
        {
            if (!Physics.checkCollision(bomb, this))
            {
                if (Physics.checkCollision(bomb, tmpPlayerY))
                {
                    isMoveUp = false;
                    isMoveDown = false;
                }
                if (Physics.checkCollision(bomb, tmpPlayerX))
                {
                    isMoveRight = false;
                    isMoveLeft = false;
                }
            }
        }
    }

    public boolean chechCollision(Bonus bonus)
    {
        if (Physics.checkCollision(bonus, this))
        {
            switch (bonus.getType())
            {
                case POWER:
                    this.bombPower++;
                    break;
                case BOMB:
                    this.bombQuanity++;
                    break;
                case SPEED:
                    if (this.speed < 0.7f) this.speed += 0.1f;
            }
            return true;
        } else return false;
    }

    public void death()
    {
        currentAnimation = animationDeath;
        currentAnimation.playOnce();
        setDirection(DOWN);
        speed = SPEED;
        bombsDropped = 0;
        bombPower = BOMBS_POWER;
        bombQuanity = BOMBS_QUANITY;
        isAlive = false;
        timeToSpawn = 3000;
    }

    public void spawn()
    {
        isAlive = true;
        setDirection(DOWN);
        setAnimation();
        timeToSpawn = -1;
    }

    public void setMoveUp(boolean isMoveUp)
    {
        this.isMoveUp = isMoveUp;
    }

    public void setMoveDown(boolean isMoveDown)
    {
        this.isMoveDown = isMoveDown;
    }

    public void setMoveLeft(boolean isMoveLeft)
    {
        this.isMoveLeft = isMoveLeft;
    }

    public void setMoveRight(boolean isMoveRight)
    {
        this.isMoveRight = isMoveRight;
    }

    public void setDirection(DIRECTION dir)
    {
        this.dir = dir;
    }

    public int getBombPower()
    {
        return bombPower;
    }

    public void setBombQuanity(int bombQuanity)
    {
        this.bombQuanity = bombQuanity;
    }

    public int getBombQuanity()
    {
        return bombQuanity;
    }

    public void setAlive(boolean alive)
    {
        this.isAlive = alive;
    }

    public boolean isAlive()
    {
        return isAlive;
    }

    public void incKills()
    {
        kills++;
    }

    public void resetKills()
    {
        kills = 0;
    }

    public int getKills()
    {
        return kills;
    }

    public String getNickname()
    {
        return nickname;
    }

    public void setID(int ID)
    {
        this.ID = ID;
    }

    public int getID()
    {
        return ID;
    }

    public int getTimeToSpawn()
    {
        return timeToSpawn;
    }

    public void setWins(int wins)
    {
        this.wins = wins;
    }

    public int getWins()
    {
        return wins;
    }

    public void setLost(int lost)
    {
        this.lost = lost;
    }

    public int getLost()
    {
        return lost;
    }

}