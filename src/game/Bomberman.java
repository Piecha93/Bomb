package game;

import graphics.ResourcesLoader;
import logic.Timer;
import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import server.Client;

import java.rmi.RemoteException;

import static org.lwjgl.opengl.GL11.*;

public class Bomberman
{
    private static Game game;
    private static Menu menu;

    public static enum State
    {
        MAIN_MENU, GAME
    }

    private static State state;
    public static final String gameName = "Bomberman";
    private static Timer timer;
    private static int delta;
    private static Client client;

    public static final int WIDTH = 800;
    public static final int HEIGHT = 600;

    public static void main(String args[])
    {
        initDisplay();
        setState(State.MAIN_MENU);
        initGame();
        gameLoop();
        clean();
    }

    public static void gameLoop()
    {
        while (!Display.isCloseRequested())
        {
            delta = timer.getDelta();
            getInput();
            update(delta);
            render();
        }
    }

    private static void getInput()
    {
        switch (state)
        {
            case MAIN_MENU:
                menu.getInput();
                break;
            case GAME:
                game.getInput();
                break;
        }
    }

    private static void render()
    {
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        switch (state)
        {
            case MAIN_MENU:
                menu.render();
                break;
            case GAME:
                game.render();
                break;
        }

        Display.update();
        Display.sync(60);
    }

    private static void update(int delta)
    {
        switch (state)
        {
            case MAIN_MENU:
                menu.update(delta);
                break;
            case GAME:
                game.update(delta);
                break;
        }
    }

    private static void initGame()
    {
        game = new Game();
        ResourcesLoader.LoadResources();
        try
        {
            client = new Client();
        }
        catch (RemoteException e)
        {
            e.printStackTrace();
        }
        game.setClient(client);
        client.setGame(game);
        menu = new Menu();
        timer = new Timer();
        delta = timer.getDelta();
    }

    private static void clean()
    {
        Display.destroy();
        if(client != null)
            client.disconnect();
        System.exit(1);
    }

    public static void initDisplay()
    {
        try
        {
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.create();
          //  Display.setVSyncEnabled(true);
           // Display.setResizable(true);
        }
        catch (LWJGLException e)
        {
            e.printStackTrace();
        }
    }

    public static void initGameGL()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(-Display.getWidth() / 2, Display.getWidth() / 2, Display.getHeight() / 2, -Display.getHeight() / 2, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void initMenuGl()
    {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(0, Display.getWidth(), Display.getHeight(), 0, -1, 1);
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
        glDisable(GL_DEPTH_TEST);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void setState(State st)
    {
        state = st;

        switch (state)
        {
            case GAME:
                initGameGL();
                break;
            case MAIN_MENU:
                initMenuGl();
                break;
        }
    }

    public static Client getClient()
    {
        return client;
    }
}
