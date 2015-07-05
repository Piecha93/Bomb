package server;

import logic.*;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ClientInterface extends Remote
{
    public void initGame(
            int[][] map, List<Player> players, List<Bomb> bombs, List<Explosion> explosions, List<Bonus> bonuses, int mapTime)
    throws RemoteException;

    public void updatePlayers(List<Player> players) throws RemoteException;
    public void updateBombs(List<Bomb> bombs) throws RemoteException;
    public void updateBonuses(List<Bonus> bonuses) throws RemoteException;
    public void updateExplosions(List<Explosion> explosions) throws RemoteException;
    public void updateMap(int[][] map) throws RemoteException;
    public void setUpdated(boolean updated) throws RemoteException;
    public void updateMessages(List<String> messeages) throws RemoteException;
    public void updateMapTime(int mapTime) throws RemoteException;

}