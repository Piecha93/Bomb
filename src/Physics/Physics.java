package Physics;

import logic.GameObject;

public class Physics
{
    public static boolean checkCollision(GameObject obj1, GameObject obj2)
    {
        if ((obj1.getX() * 32 + obj1.getW()) > (obj2.getX()) * 32 && (obj1.getX() * 32) < (obj2.getX() * 32 + obj2.getW()) && (obj1.getY() * 32 + obj1.getH()) > (obj2.getY() * 32) && (obj1.getY() * 32) < (obj2.getY() * 32 + obj2.getH()))
            return true;

        return false;
    }

    public static float rollVar(float var, int size)
    {
        while (var < 0) var = size + var;
        var = var % size;
        return var;
    }

    public static int rollVar(int var, int size)
    {
        while (var < 0) var = size + var;
        var = var % size;
        return var;
    }
}
