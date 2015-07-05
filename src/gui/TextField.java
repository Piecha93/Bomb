package gui;

import graphics.Draw;
import graphics.ResourcesLoader;
import org.newdawn.slick.opengl.Texture;

public class TextField
{
    private int x;
    private int y;
    private int width;
    private int height;
    private int maxChar;
    private Text text;

    private transient Texture textBoxTexture;

    public TextField(int x, int y, int width, int height, String text, int maxChar, int size, boolean isActive)
    {
        this.x = x;
        this.y = y;
        this.text = new Text(x, y, text, size, isActive);
        this.width = width;
        this.height = height;
        this.maxChar = maxChar;
        textBoxTexture = ResourcesLoader.getTextBoxTexture();
    }

    public void append(String text)
    {
        this.text.append(text);

        while (this.text.getText().length() > maxChar)
        {
            this.text.backspace();
        }
    }

    public void backspace()
    {
        text.backspace();
    }

    public void render()
    {
        Draw.drawTexture(textBoxTexture, x - 20, y - 15, width, height);
        text.render();
    }

    public void render(int numberOfLastMesseages)
    {
        Draw.drawTexture(textBoxTexture, x - 20, y - 15, width, height);
        text.render(numberOfLastMesseages);
    }

    public void update(int delta)
    {
        this.text.update(delta);
    }

    public void setActive(boolean isActive)
    {
        this.text.setActive(isActive);
    }

    public String getText()
    {
        return text.getText();
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }
}
