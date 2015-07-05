package logic;

public enum BONUS_TYPE
{
    BOMB, POWER, SPEED;

    public static BONUS_TYPE fromInteger(int x)
    {
        switch (x)
        {
            case 0:
                return BOMB;
            case 1:
                return POWER;
            case 2:
                return SPEED;
        }
        return null;
    }
}
