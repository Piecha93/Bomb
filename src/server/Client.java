package server;

import game.Game;
import logic.*;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class Client extends UnicastRemoteObject
        implements ClientInterface
{
    private Registry reg;
    private ServerInterface rmiServer;
    private List<User> users;
    private String nickname;
    private User user;
    private Game game;
    private UpdatedData data;
    private volatile boolean updated;
    private Thread updateThread;

    public Client() throws RemoteException
    {
        super();
        updated = false;
        data = new UpdatedData();
    }

    public boolean connect(String nickname,String password, String ip)
    {
        try
        {
            reg = LocateRegistry.getRegistry(ip, 1099);
            rmiServer = (ServerInterface) reg.lookup("BombermanServer");
            user = rmiServer.logIn(this, nickname, password);

            if (user == null)
            {
                System.out.println("Logowanie nieudane.");
                return false;
            }
            this.nickname = nickname;
            users = rmiServer.getUsersList();
        }
        catch (RemoteException ex)
        {
            ex.printStackTrace();
            System.out.println("Nie udalo sie polaczyc");
            return false;
        }
        catch (NotBoundException ex)
        {
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public void setGame(Game game)
    {
        this.game = game;
    }

    public void disconnect()
    {
        if(rmiServer != null)
        {
            try
            {
                rmiServer.logOut(getUser().getID());
            }
            catch (RemoteException e)
            {
                e.printStackTrace();
            }
            catch (NullPointerException e)
            {
                e.printStackTrace();
            }
        }
    }

    public void initGame()
    {
        try
        {
            rmiServer.initGame(user);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }

        updateThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                updateGame();
            }
        });
        updateThread.start();
    }

    public void updateGame()
    {
        while (true)
        {
            if(updated)
            {
                game.serverUpdate(data);
                updated = false;
            }
            try
            {
                Thread.sleep(10);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
    }

    public User getUser()
    {
        return user;
    }

    public void sendMove(DIRECTION dir)
    {
        try
        {
            rmiServer.playerMove(user.getID(), dir);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void sendBomb()
    {
        try
        {
            rmiServer.setBomb(user.getID());
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        catch (NullPointerException e)
        {
            e.printStackTrace();
        }
    }

    public void initGame(int[][] map, List<Player> players, List<Bomb> bombs, List<Explosion> explosions, List<Bonus> bonuses, int mapTime)
                throws RemoteException
    {
        game.serverInit(map, players, bombs, explosions, bonuses, mapTime);
    }

    public void sendMessage(String message)
    {
        message = nickname + ": " + message + "\n";
        try
        {
            rmiServer.sendMessage(message);
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void updatePlayers(List<Player> players) throws RemoteException
    {
        data.getPlayers().removeAll(data.getPlayers());
        for (Player player : players)
        {
            Player tmpPlayer;
            tmpPlayer = new Player(player);

            if (player.getID() == getUser().getID()) data.setMainPlayer(tmpPlayer);
            else data.getPlayers().add(tmpPlayer);
        }
    }

    @Override
    public void updateBombs(List<Bomb> bombs) throws RemoteException
    {
        data.getBombs().removeAll(data.getBombs());
        for(Bomb bomb : bombs)
        {
            data.getBombs().add(new Bomb(bomb));
        }
    }

    @Override
    public void updateBonuses(List<Bonus> bonuses) throws RemoteException
    {
        data.getBonuses().removeAll(data.getBonuses());
        for(Bonus bonus : bonuses)
        {
            data.getBonuses().add(new Bonus(bonus));
        }
    }

    @Override
    public void updateExplosions(List<Explosion> explosions) throws RemoteException
    {
        data.getExplosions().removeAll(data.getExplosions());
        for(Explosion explosion : explosions)
        {
            data.getExplosions().add(new Explosion(explosion));
        }
    }

    @Override
    public void updateMap(int[][] map) throws RemoteException
    {
        data.getMap().loadFromTab(map);
    }

    public void setUpdated(boolean updated)
    {
        this.updated = updated;
    }

    @Override
    public void updateMessages(List<String> messeages) throws RemoteException
    {
        try
        {
            game.getChat().update(messeages);
        }
        catch (NullPointerException e)
        {
            //gra jeszcze niezainicjalizowana
        }
    }

    @Override
    public void updateMapTime(int mapTime) throws RemoteException
    {
        game.setMapTime(mapTime);
    }
}
