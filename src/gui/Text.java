package gui;

import graphics.Animation;
import graphics.Draw;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.newdawn.slick.opengl.Texture;

import java.util.Map;

public class Text
{
    private int x;
    private int y;
    private String text;
    private boolean isActive;
    private int size;

    private transient Texture textBoxTexture;
    private transient Texture spriteSheetTexture;
    private transient Sprite currentSprite;
    private transient Map<String, Sprite> alphabetSpriteMap;
    private Animation cursorAnimation;

    public Text(int x, int y, String text, int size, boolean isActive)
    {
        this.x = x;
        this.y = y;
        this.text = text;
        this.size = size;
        this.isActive = isActive;
        spriteSheetTexture = ResourcesLoader.getalphabetBlueTexture();
        alphabetSpriteMap = ResourcesLoader.getalphabetSprites();
        cursorAnimation = new Animation(
                spriteSheetTexture,
                new Sprite[]{alphabetSpriteMap.get("|"), alphabetSpriteMap.get(" ")},
                400, true);
        cursorAnimation.start();
    }

    public void append(String text)
    {
        this.text += text;
    }

    public void backspace()
    {
        if(text.length() > 0)
            text = text.substring(0, text.length() - 1);
    }

    public void render()
    {
        render(this.text);
    }

    public void render(int numberOfLastMesseages)
    {
        int messeagesCounter = 0;
        char[] tmpText = text.toCharArray();
        for(int i = 0; i < text.length(); i++)
        {
            String ch = Character.toString(tmpText[i]);
            if(ch.equals("\n"))
                messeagesCounter++;
        }
        if(messeagesCounter <= numberOfLastMesseages)
            render();
        else
            for(int i = 0; i < text.length(); i++)
            {
                String ch = Character.toString(tmpText[i]);
                if(ch.equals("\n"))
                    messeagesCounter--;
                if (messeagesCounter <= numberOfLastMesseages * 2)
                {
                    render(text.substring(i));
                    break;
                }
            }
    }

    public void render(String text)
    {
        int line = 0;
        int position = 0;
        char[] tmpText = text.toCharArray();
        for (int i = 0; i < text.length(); i++)
        {
            String ch = Character.toString(tmpText[i]);
            if (ch.equals("\n"))
            {
                line++;
                position = 0;
            } else
            {
                currentSprite = alphabetSpriteMap.get(ch);
                if (currentSprite != null) Draw.drawSpritedSquare(spriteSheetTexture, currentSprite, x - 10 + position * size, y + line * size, size, size);
                currentSprite = null;
                position++;
            }
        }
        if (isActive) cursorAnimation.render(x + text.length() * size - size / 3, y, size, size);
    }

    public void update(int delta)
    {
        cursorAnimation.update(delta);
    }

    public void setActive(boolean isActive)
    {
        this.isActive = isActive;
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
    }
}
