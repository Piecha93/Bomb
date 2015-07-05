package server;

import logic.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class UpdatedData
{
    private List<Player> players;
    private List<Bomb> bombs;
    private List<Bonus> bonuses;
    private List<Explosion> explosions;
    private Player mainPlayer;
    private Area map;

    UpdatedData()
    {
        players = new CopyOnWriteArrayList<Player>();
        bombs = new CopyOnWriteArrayList<Bomb>();
        bonuses = new CopyOnWriteArrayList<Bonus>();
        explosions = new CopyOnWriteArrayList<Explosion>();
        mainPlayer = null;
        map = new Area();
    }


    public Player getMainPlayer()
    {
        return mainPlayer;
    }

    public List<Bomb> getBombs()
    {
        return bombs;
    }

    public List<Explosion> getExplosions()
    {
        return explosions;
    }

    public List<Bonus> getBonuses()
    {
        return bonuses;
    }

    public List<Player> getPlayers()
    {
        return players;
    }

    public void setMainPlayer(Player player)
    {
        mainPlayer = new Player(player);
    }

    public Area getMap()
    {
        return map;
    }
}
