package server;

import java.io.Serializable;

public class User implements Serializable
{
    private String nickname;
    private int ID;

    public int getID()
    {
        return ID;
    }

    public String getNickname()
    {
        return nickname;
    }

    public User(int ID, String nickname)
    {
        this.nickname = nickname;
        this.ID = ID;
        //msg = new ArrayList<>();
    }
}
