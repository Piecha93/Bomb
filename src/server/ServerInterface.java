package server;

import logic.DIRECTION;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ServerInterface extends Remote
{
    public User logIn(ClientInterface client, String nickname, String password) throws RemoteException;
    public List<User> getUsersList() throws RemoteException;
    public void initGame(User user) throws RemoteException;
    public void playerMove(int ID, DIRECTION dir) throws RemoteException;
    public void setBomb(int ID) throws RemoteException;
    public void sendMessage(String messeage) throws RemoteException;
    public void logOut(int id) throws RemoteException;
}
