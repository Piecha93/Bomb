package graphics;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;

public class Draw
{
    public static void drawSpritedSquare(Texture texture, Sprite sprite, float x, float y, float w, float h)
    {
        texture.bind();
        float tx = (float) sprite.getX() / (float) texture.getTextureWidth();
        float ty = (float) sprite.getY() / (float) texture.getTextureHeight();
        float tx2 = tx + sprite.getWidth() / (float) texture.getTextureWidth();
        float ty2 = ty + sprite.getHeight() / (float) texture.getTextureHeight();

        GL11.glBegin(GL11.GL_QUADS);
            GL11.glTexCoord2f(tx, ty);
            GL11.glVertex2f(x, y);
            GL11.glTexCoord2f(tx2, ty);
            GL11.glVertex2f(x + w, y);
            GL11.glTexCoord2f(tx2, ty2);
            GL11.glVertex2f(x + w, y + h);
            GL11.glTexCoord2f(tx, ty2);
            GL11.glVertex2f(x, y + h);
        GL11.glEnd();
    }

    public static void drawTexture(Texture texture, float x, float y, float w, float h)
    {
        texture.bind();
        float tx = 0;
        float ty = 0;
        float tx2 = 1;
        float ty2 = 1;

        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(tx, ty);
        GL11.glVertex2f(x, y);
        GL11.glTexCoord2f(tx2, ty);
        GL11.glVertex2f(x + w, y);
        GL11.glTexCoord2f(tx2, ty2);
        GL11.glVertex2f(x + w, y + h);
        GL11.glTexCoord2f(tx, ty2);
        GL11.glVertex2f(x, y + h);
        GL11.glEnd();
    }

    public static void drawString(TrueTypeFont font, float x, float y, String text)
    {
        font.drawString(x, y, text);
    }
}


