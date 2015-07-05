package logic;

import Physics.Physics;
import game.Game;
import graphics.ResourcesLoader;
import graphics.Sprite;
import org.lwjgl.opengl.Display;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Area
{
    int displayWidth;
    int displayHeight;

    private List<List<MapObject>> objArray;
    private transient Map<String, Sprite> spriteMap;
    private int mapTab[][];
    private boolean isChanged;

    public Area()
    {
        spriteMap = ResourcesLoader.getTiledmapSprites();
        isChanged = false;
    }

    public void initFromFile()
    {
        loadFromFile();
    }

    public void update(int delta)
    {
        updateDisplayHeight();
        updateDisplayWidth();
    }

    public void updateDisplayWidth()
    {
        displayWidth = (int) ((Display.getWidth() / 64 + 3) / Game.getScale());
    }

    public void updateDisplayHeight()
    {
        displayHeight = (int) ((Display.getHeight() / 64 + 3) / Game.getScale());
    }

    public void render()
    {
        for (int i = (int) Game.getCameraX() - displayWidth; i < (int) Game.getCameraX() + displayWidth; i++)
        {
            for (int j = (int) Game.getCameraY() - displayHeight; j < (int) Game.getCameraY() + displayHeight; j++)
            {
                int x = i;
                int y = j;
                x = Physics.rollVar(x, Game.getSizeX());
                y = Physics.rollVar(y, Game.getSizeY());
                getObjArray().get(x).get(y).render(i, j);
            }
        }
    }

    public Explosion explosion(int x, int y, int power, List<Bonus> bonuses, Player owner)
    {
        Explosion exp = new Explosion(x, y, owner);
        boolean[] dir = {true, true, true, true};

        /* sprawdzenie wszystkich kierunkow */
        for (int i = 1; i <= power; i++)
        {
            GameObject[] tmpObj = {getObject(Physics.rollVar(x, Game.getSizeX()), Physics.rollVar(y + i, Game.getSizeY())), getObject(Physics.rollVar(x, Game.getSizeX()), Physics.rollVar(y - i, Game.getSizeY())), getObject(Physics.rollVar(x + i, Game.getSizeX()), Physics.rollVar(y, Game.getSizeY())), getObject(Physics.rollVar(x - i, Game.getSizeX()), Physics.rollVar(y, Game.getSizeY())),};

            /*  jesli stone - zatrzymaj wybuch
                jesli Brick - wysadz i zatrzymaj
                jesli EmptyObject - kontynuuj   */
            for (int j = 0; j < 4; j++)
            {
                if (tmpObj[j] instanceof Stone && dir[j] != false)
                {
                    dir[j] = false;
                } else if (tmpObj[j] instanceof Brick && dir[j] != false)
                {
                    dir[j] = false;
                    exp.add(j);
                    MapObject tmpMapObject = new EmptyObject((int) tmpObj[j].getX(), (int) tmpObj[j].getY(), spriteMap.get("grass"));
                    setObjectArray(tmpMapObject);
                    mapTab[(int)tmpMapObject.getX()][(int)tmpMapObject.getY()] = getNumber(tmpMapObject.getSprite());
                    /* Losowanie bnusu */
                    Random generator = new Random();
                    if (generator.nextInt(5) == 4)
                    {
                        Bonus bonus = new Bonus(tmpObj[j].getX(), tmpObj[j].getY(), BONUS_TYPE.fromInteger(generator.nextInt(3)));
                        bonuses.add(bonus);
                    }
                    isChanged = true;
                } else if (tmpObj[j] instanceof EmptyObject && dir[j] != false)
                {
                    exp.add(j);
                }
            }
        }
        return exp;
    }

    public void loadFromFile()
    {
        // FileDialog fd = new FileDialog(new JFrame(), "Load", FileDialog.LOAD);
        //  fd.setVisible(true);
        //if (fd.getFile() != null)
        {
            //      Path filePath = Paths.get(fd.getDirectory() + fd.getFile());
            Path filePath = Paths.get("res/maps/mapa test 48.txt");
            Scanner scanner = null;
            try
            {
                scanner = new Scanner(filePath);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            String text = null;
            Game.setSizeX(scanner.nextInt());
            Game.setSizeY(scanner.nextInt());

            mapTab = new int[Game.getSizeX()][Game.getSizeY()];
            objArray = new CopyOnWriteArrayList<List<MapObject>>();

            for (int i = 0; i < Game.getSizeX(); i++)
            {
                objArray.add(new CopyOnWriteArrayList<MapObject>());
            }
            int x = 0;
            while (scanner.hasNext())
            {
                text = scanner.next();
                String[] intFromLine = text.split(",");
                for (int y = 0; y < Game.getSizeY(); y++)
                {
                    mapTab[x][y] = Integer.parseInt(intFromLine[y]);
                    getObjArray().get(x).add(createGameObjectFromNumber(mapTab[x][y], x, y));
                }
                x++;
            }
            scanner.close();
        }
    }

    public void loadFromTab(int[][] tab)
    {
        if(tab != null)
        {
            Game.setSizeX(tab.length);
            Game.setSizeY(tab[0].length);
            objArray = new CopyOnWriteArrayList<List<MapObject>>();

            for (int i = 0; i < Game.getSizeX(); i++)
            {
                getObjArray().add(new CopyOnWriteArrayList<MapObject>());
                for (int j = 0; j < Game.getSizeY(); j++)
                {
                    getObjArray().get(i).add(createGameObjectFromNumber(tab[i][j], i, j));
                }
            }
            mapTab = tab;
        }
    }

    public void updateFromTab(int[][] updatedTab)
    {
        if(updatedTab != null)
        {
            for (int i = 0; i < Game.getSizeX(); i++)
            {
                for (int j = 0; j < Game.getSizeY(); j++)
                {
                    if(mapTab[i][j] != updatedTab[i][j])
                    {
                        //getObjArray().get(i).set(j, createGameObjectFromNumber(updatedTab[i][j], i, j));
                        setObjectArray(createGameObjectFromNumber(updatedTab[i][j], i, j));
                    }
                }
            }
            mapTab = updatedTab;
        }
    }

    public int[][] convertToTab()
    {
        if(getObjArray() != null)
        {
            int i = 0;
            int j = 0;
            int[][] tab = new int[Game.getSizeX()][Game.getSizeY()];

            List<List<MapObject>> tmpObjArray = new CopyOnWriteArrayList<List<MapObject>>(getObjArray());

            for (List<MapObject> gameObjects : tmpObjArray)
            {
                for (MapObject object : gameObjects)
                {
                    tab[i][j] = getNumber(object.getSprite());
                    j++;
                }
                i++;
                j = 0;
            }
            mapTab = tab;
            return tab;
        }
        return null;
    }

    public int[][] getMapTab()
    {
        return mapTab;
    }

    public void makePlace(int x, int y)
    {
        for (int i = 0; i < 2; i++)
        {
            x = Physics.rollVar(x + i, Game.getSizeX());
            for (int j = 0; j < 2; j++)
            {
                y = Physics.rollVar(y + j, Game.getSizeY());
                EmptyObject tmpEmpty = (new EmptyObject(x, y, spriteMap.get("burnedgrass")));
                setObjectArray(tmpEmpty);
                mapTab[x][y] = getNumber(getObjArray().get(x).get(y).getSprite());
            }
            y = Physics.rollVar(y - 1, Game.getSizeY());
        }
        isChanged = true;
    }

    public MapObject createGameObjectFromNumber(int num, int x, int y)
    {
        Sprite sprite = getSprite(num);
        if (num >= 0 && num <= 31) return new EmptyObject(x, y, sprite);
        else if (num >= 32 && num <= 35) return new Stone(x, y, sprite);
        else if (num >= 36 && num <= 39)
        {
            //chests
        } else if (num >= 40 && num <= 47) return new Brick(x, y, sprite);

        return null;
    }

    public GameObject getObject(int x, int y)
    {
        return getObjArray().get(x).get(y);
    }

    public Sprite getSprite(int spriteNumber)
    {
        int i = 0;
        for (Sprite tmpSprite : spriteMap.values())
        {
            if(i == spriteNumber)
                return tmpSprite;
            i++;
        }
        return null;
    }

    /* pobieranie numeru sprita */
    public int getNumber(Sprite sprite)
    {
        int number = 0;

        for (Sprite tmpSprite : spriteMap.values())
        {
            if(sprite.equals(tmpSprite))
                return number;
            number++;
        }
        return -1;
    }

    public synchronized List<List<MapObject>> getObjArray()
    {
        return objArray;
    }

    public synchronized void setObjectArray(MapObject obj)
    {
        objArray.get((int)obj.getX()).set((int)obj.getY(), obj);
    }

    public boolean isChanged()
    {
        return isChanged;
    }

    public void setIsChenged(boolean isChanged)
    {
        this.isChanged = isChanged;
    }
}