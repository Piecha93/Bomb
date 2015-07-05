package game;

import game.Bomberman;
import graphics.Draw;
import graphics.ResourcesLoader;
import gui.Text;
import gui.TextField;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

public class Menu
{
    private TextField loginField;
    private TextField passwordField;
    private TextField ipField;
    private TextField activeTextField;
    private Text errorField;
    private int keyTimer;

    public Menu()
    {
        loginField = new TextField(Display.getWidth()/2 - 300, Display.getHeight() - 400, 700, 100, "Login", 15, 32, true);
        passwordField = new TextField(Display.getWidth()/2 - 300, Display.getHeight() - 300, 700, 100, "Password", 15, 32, false);
        ipField = new TextField(Display.getWidth()/2 - 300, Display.getHeight() - 200, 700, 100, "192.168.0.102", 15, 32, false);
        errorField = new Text(Display.getWidth()/2 - 300, Display.getHeight() - 100, "", 15, false);
        activeTextField = loginField;
        keyTimer = 0;
    }

    public void update(int delta)
    {
        loginField.update(delta);
        passwordField.update(delta);
        ipField.update(delta);
        errorField.update(delta);
        keyTimer += delta;
    }

    public void render()
    {
        Draw.drawTexture(ResourcesLoader.getStartScreenTexture(), 0, 0, Display.getWidth() * 2, Display.getHeight() * 2);
        //Draw.drawSpritedSquare(ResourcesLoader.getExplosionTexture(), new Sprite("xx",-1000, -1000, 1000, 1000), 0, 0, Display.getWidth(), Display.getHeight());
        loginField.render();
        passwordField.render();
        ipField.render();
        errorField.render();
    }

    public void getInput()
    {
        if (keyTimer > 70)
        {
            keyTimer = 0;
            if (Keyboard.isKeyDown(14))
            {
                activeTextField.backspace();
            }
            if (Keyboard.isKeyDown(15))
            {
                activeTextField.setActive(false);
                if (activeTextField == loginField) activeTextField = passwordField;
                else if (activeTextField == passwordField) activeTextField = ipField;
                else if (activeTextField == ipField) activeTextField = loginField;
                activeTextField.setActive(true);
            }
            if (Keyboard.isKeyDown(52))
            {
                activeTextField.append(".");
            }
            if (Keyboard.isKeyDown(28))
            {
                if( Bomberman.getClient().connect(loginField.getText(), passwordField.getText(), ipField.getText()) )
                {
                    Bomberman.setState(Bomberman.State.GAME);
                    Bomberman.getClient().initGame();
                }
                else
                    errorField.setText("Logowanie nieudane");
            }
            while (Keyboard.next())
            {
                int key = Keyboard.getEventKey();
                if (Keyboard.getEventKeyState())
                {
                    String charName = Keyboard.getKeyName(key);
                  //  if ((key >= 2 && key <= 11) || (key >= 16 && key <= 50) || (key >= 71 && key <= 83) || key == 57 && key != 28 && charName.length() == 1)
                    if(charName.length() == 1)
                        activeTextField.append(charName);
                    if(charName.equals("SPACE"))
                        activeTextField.append(" ");
                }
            }

        }
    }
}