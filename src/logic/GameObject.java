package logic;

import java.io.Serializable;

public class GameObject implements Serializable
{
    private float x;
    private float y;
    private float w;
    private float h;

    public GameObject()
    {
        this.x = 0;
        this.y = 0;
        this.w = 32;
        this.h = 32;
    }

    public GameObject(GameObject obj)
    {
        this.x = obj.getX();
        this.y = obj.getY();
        this.w = obj.getW();
        this.h = obj.getH();
    }

    public GameObject(float x, float y, float w, float h)
    {
        setX(x);
        setY(y);
        setW(w);
        setH(h);
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public void setX(float x)
    {
        this.x = x;
    }

    public void setY(float y)
    {
        this.y = y;
    }

    public float getW()
    {
        return w;
    }

    public float getH()
    {
        return h;
    }

    public void setW(float w)
    {
        this.w = w;
    }

    public void setH(float h)
    {
        this.h = h;
    }
}
