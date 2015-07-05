package game;

import gui.Chat;
import gui.TextField;
import logic.*;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import server.Client;
import server.UpdatedData;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Game
{
    private Player mainPlayer;

    private static int sizeX;
    private static int sizeY;
    private static float cameraX;
    private static float cameraY;
    private static float scale;
    private Area map;

    private List<Bomb> bombs;
    private List<Explosion> explosions;
    private List<Bonus> bonuses;
    private List<Player> players;
    private Client client;
    private Chat chat;
    private volatile boolean initialized;
    private boolean chatActive;

    private int mapTime;
    private TextField mapTimeTextField;
    private TextField killsTextField;
    private TextField winsTextField;
    private TextField lostTextField;

    public Game()
    {
        initialized = false;
        chatActive = false;
    }

    public void serverInit(int[][] map, List<Player> players, List<Bomb> bombs, List<Explosion> explosions, List<Bonus> bonuses, int mapTime)
    {
        this.map = new Area();
        this.map.loadFromTab(map);
        this.mapTime = mapTime;
        if (map == null) initialized = false;

        this.players = new CopyOnWriteArrayList<Player>();
        for (Player opponent : players)
        {
            Player tmpPlayer;
            tmpPlayer = new Player(opponent);

            if (opponent.getID() == client.getUser().getID()) mainPlayer = tmpPlayer;
            else getPlayers().add(tmpPlayer);
        }
        if (mainPlayer == null) initialized = false;

        this.bombs = new CopyOnWriteArrayList<Bomb>();
        for (Bomb bomb : bombs)
        {
            Bomb tmpBomb;
            tmpBomb = new Bomb(bomb);
            getBombs().add(tmpBomb);
        }

        this.explosions = new CopyOnWriteArrayList<Explosion>();
        for (Explosion explosion : explosions)
        {
            Explosion tmpExplosion;
            tmpExplosion = new Explosion(explosion);
            getExplosions().add(tmpExplosion);
        }

        this.bonuses = new CopyOnWriteArrayList<Bonus>();
        for (Bonus bonus : bonuses)
        {
            Bonus tmpBonus;
            tmpBonus = new Bonus(bonus);
            getBonuses().add(tmpBonus);
        }

        chat = new Chat();
        mapTimeTextField = new TextField(
                -Display.getWidth()/2 + 20, -Display.getHeight()/2 + 50, 200, 50, "Time:" + Integer.toString(mapTime/1000), 4, 15, false);
        killsTextField = new TextField(
                -Display.getWidth()/2 + 20, -Display.getHeight()/2 + 80, 200, 50, "Kills:" + Integer.toString(mainPlayer.getKills()), 4, 15, false);
        winsTextField = new TextField(
                Display.getWidth()/2 - 100, -Display.getHeight()/2 + 50, 150, 50, "Wins:" + Integer.toString(mainPlayer.getWins()), 4, 15, false);
        lostTextField = new TextField(
                Display.getWidth()/2 - 100, -Display.getHeight()/2 + 80, 150, 50, "Lost:" + Integer.toString(mainPlayer.getLost()), 4, 15, false);
        initialized = true;
    }

    public void chatUpdate(List<String> messeages)
    {
        chat.update(messeages);
    }

    public void serverUpdate(UpdatedData data)
    {
        /* update glownego gracza */
        if (data.getMainPlayer() != null) getMainPlayer().update(data.getMainPlayer());

        /* update resztu graczy */
        for (Player updatedPlayer : data.getPlayers())
        {
            boolean updated = false;
            for (Player player : getPlayers())
            {
                if (player.getID() == updatedPlayer.getID())
                {
                    player.update(updatedPlayer);
                    updated = true;
                    break;
                }
            }
            if (updated == false && updatedPlayer.getID() != getMainPlayer().getID())
            {
                getPlayers().add(new Player(updatedPlayer));
            }
        }

        if (data.getPlayers().size() < getPlayers().size())
        {
            for (Player player : getPlayers())
            {
                boolean exist = false;
                for (Player updatedPlayer : data.getPlayers())
                {
                    if (player.getID() == updatedPlayer.getID())
                    {
                        exist = true;
                        break;
                    }
                }
                if (exist == false) getPlayers().remove(player);
            }
        }

        bombs = data.getBombs();
        explosions = data.getExplosions();

        /* update bonusow */
        for (Bonus updatedBonus : data.getBonuses())
        {
            boolean updated = false;
            for (Bonus bonus : getBonuses())
            {
                if (bonus.getX() == updatedBonus.getX() && bonus.getY() == updatedBonus.getY())
                {
                    updated = true;
                    break;
                }
            }
            if (updated == false)
            {
                getBonuses().add(new Bonus(updatedBonus));
            }
        }

        for (Bonus bonus : getBonuses())
        {
            boolean exist = false;
            for (Bonus updatedBonus : data.getBonuses())
            {
                if (bonus.getX() == updatedBonus.getX() && bonus.getY() == updatedBonus.getY())
                {
                    exist = true;
                    break;
                }
            }
            if (exist == false)
            {
                getBonuses().remove(bonus);
            }
        }
        /* update mapy */
        getMap().updateFromTab(data.getMap().getMapTab());
    }

    public void update(int delta)
    {
        if (initialized)
        {
            if (scale < 0.5f) scale = 0.5f;
            if (scale > 1.5f) scale = 1.5f;

            chat.update(delta);

            getMainPlayer().chechCollision(map, bombs, delta);
            getMainPlayer().update(delta);
            map.update(delta);
            mapTimeTextField.setText("Time:" + Integer.toString(mapTime / 1000));
            killsTextField.setText("Kills:" + Integer.toString(mainPlayer.getKills()));
            winsTextField.setText("Wins:" + Integer.toString(mainPlayer.getWins()));
            lostTextField.setText("Lost:" + Integer.toString(mainPlayer.getLost()));

            cameraX = getMainPlayer().getX();
            cameraY = getMainPlayer().getY();

            for (Player player : getPlayers())
            {
                player.chechCollision(map, bombs, delta);
                player.update(delta, false);
            }

            for (Bomb bomb : getBombs())
            {
                bomb.update(delta);
                if (bomb.isExplode())
                {
                    //    Explosion tmpExplosion = map.explosion((int) bomb.getX(), (int) bomb.getY(), bomb.getExplodePower(), bonuses, bomb.getOwner());
                    //    if (tmpExplosion != null) explosions.add(tmpExplosion);
                    bombs.remove(bomb);
                }
            }

            for (Explosion explosion : getExplosions())
            {
                explosion.update(delta);
                explosion.checkCollision(bombs, players);
                explosion.checkCollision(getMainPlayer());
                if (explosion.isEnded()) getExplosions().remove(explosion);
            }

            for (Bonus bonus : getBonuses())
            {
                bonus.update(delta);
                if (getMainPlayer().chechCollision(bonus) == true) getBonuses().remove(bonus);
            }
        }
    }

    public void render()
    {
        if (initialized)
        {
            map.render();
            for (Bomb bomb : getBombs())
                bomb.render();
            for (Bonus bonus : getBonuses())
                bonus.render();
            for (Player opponent : getPlayers())
                opponent.renderXY();
            getMainPlayer().render();
            for (Explosion explosion : getExplosions())
                explosion.render();
            chat.render();
            mapTimeTextField.render();
            killsTextField.render();
            winsTextField.render();
            lostTextField.render();

         /*   glMatrixMode(GL_PROJECTION);
            glLoadIdentity();
            glOrtho(x1 - Display.getWidth() / 2, Display.getWidth() / 2 + x1, Display.getHeight() / 2 + y1, y1 - Display.getHeight() / 2, -9999.0f, 9999.0f);
            glScaled(scale, scale, 1);
            glMatrixMode(GL_MODELVIEW);
            glLoadIdentity();*/
        }
    }

    public void getInput()
    {
        if (initialized)
        {
            if(chatActive)
            {
                while (Keyboard.next())
                {
                    int key = Keyboard.getEventKey();
                    if (Keyboard.getEventKeyState())
                    {
                        if(key == 28)
                        {
                            chatActive = false;
                            client.sendMessage(chat.getWriteMessage());
                            chat.stopWriting();
                        }
                        else
                        {
                            String charName = Keyboard.getKeyName(key);
                            //if ((key >= 2 && key <= 11) || (key >= 16 && key <= 50) || (key >= 71 && key <= 83) && charName.length() == 1)
                            if(charName.length() == 1)
                                chat.append(charName);
                            if(charName.equals("SPACE"))
                                chat.append(" ");
                        }
                    }
                }
            } else
            {
                while (Keyboard.next())
                {
                    int key = Keyboard.getEventKey();
                    if (Keyboard.getEventKeyState())
                    {
                        if (key == 28)
                        {
                            chatActive = true;
                            chat.startWriting();
                        }
                    }
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_UP))
                {
                    getMainPlayer().setMoveUp(true);
                    client.sendMove(DIRECTION.UP);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN))
                {
                    getMainPlayer().setMoveDown(true);
                    client.sendMove(DIRECTION.DOWN);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT))
                {
                    getMainPlayer().setMoveLeft(true);
                    client.sendMove(DIRECTION.LEFT);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
                {
                    getMainPlayer().setMoveRight(true);
                    client.sendMove(DIRECTION.RIGHT);
                }
                if (Keyboard.isKeyDown(Keyboard.KEY_SPACE))
                {
                    Bomb tmpBomb = getMainPlayer().setBomb(bombs);
                    if (tmpBomb != null) bombs.add(tmpBomb);
                    client.sendBomb();
                }
            }
        }
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

    public Player getMainPlayer()
    {
        return mainPlayer;
    }

    public Area getMap()
    {
        return map;
    }

    public static int getSizeX()
    {
        return sizeX;
    }

    public static void setSizeX(int sizeX)
    {
        Game.sizeX = sizeX;
    }

    public static int getSizeY()
    {
        return sizeY;
    }

    public static void setSizeY(int sizeY)
    {
        Game.sizeY = sizeY;
    }

    public static float getScale()
    {
        return scale;
    }

    public static float getCameraX()
    {
        return cameraX;
    }

    public static float getCameraY()
    {
        return cameraY;
    }

    public Client getClient()
    {
        return client;
    }

    public void setClient(Client client)
    {
        this.client = client;
    }

    public Chat getChat()
    {
        return chat;
    }

    public int getMapTime()
    {
        return mapTime;
    }

    public void setMapTime(int mapTime)
    {
        this.mapTime = mapTime;
    }
}