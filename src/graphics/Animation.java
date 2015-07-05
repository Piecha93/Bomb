package graphics;

import org.newdawn.slick.opengl.Texture;

public class Animation
{
    private Texture spriteSheetTexture;
    private Sprite[] sprites;
    private Sprite currentSprite;
    private int animationTime;
    private int frameTime;
    private boolean isRunning;
    private int framesNumber;
    private int currentFrame;
    private boolean isBounce;
    private boolean isVisible;
    private boolean isOnce;
    private float bounce;
    private int bouncedir;

    public Animation(Texture spriteSheetTexture, Sprite[] sprites, int frameTime, boolean isRunning)
    {
        this.spriteSheetTexture = spriteSheetTexture;
        this.sprites = sprites;
        this.frameTime = frameTime;
        this.isRunning = isRunning;
        this.framesNumber = sprites.length;
        this.animationTime = 0;
        this.currentFrame = 0;
        this.currentSprite = sprites[0];
        this.isBounce = false;
        this.bounce = 0;
        this.bouncedir = 1;
        this.isOnce = false;
        this.isVisible = true;
    }

    public void update(int delta)
    {
        if (isRunning)
        {
            animationTime += delta;
            if (animationTime > frameTime)
            {
                animationTime = 0;
                currentFrame++;
            }
            if (currentFrame >= framesNumber)
            {
                currentFrame = 0;
                if (isOnce)
                {
                    isRunning = false;
                    isVisible = false;
                }
            }
            if (isBounce == true)
            {
                bounce += delta / 40f * bouncedir;
                if (bounce > 10)
                {
                    bouncedir = -1;
                    bounce = 10;
                }
                else if(bounce < -10)
                {
                    bouncedir = 1;
                    bounce = -10;
                }
            }
        } else
        {
            currentFrame = 0;
            animationTime = 0;
            bounce = 0;
        }
        currentSprite = sprites[currentFrame];
    }

    public void render(float x, float y)
    {
        render(x, y, 32, 32);
    }

    public void render(float x, float y, float w, float h)
    {
        if (isVisible) Draw.drawSpritedSquare(spriteSheetTexture, currentSprite, x, y + bounce, w, h);
    }

    public void playOnce()
    {
        currentFrame = 0;
        animationTime = 0;
        isRunning = true;
        isOnce = true;
        isVisible = true;
    }

    public void start()
    {
        isRunning = true;
        isVisible = true;
    }

    public void stop()
    {
        isRunning = false;
    }

    public void bounceStart()
    {
        isBounce = true;
    }

    public void bounceStop()
    {
        isBounce = false;
    }

    public void setVisible(boolean visible)
    {
        this.isVisible = visible;
    }
}
