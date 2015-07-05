package gui;


import org.lwjgl.opengl.Display;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Chat
{
    private List<String> messeages;
    private TextField messeagesTextField;
    private TextField writeTextField;

    public Chat()
    {
        messeages = new CopyOnWriteArrayList<String>();
        messeagesTextField = new TextField(-Display.getWidth()/2 + 20, Display.getHeight()/2 - 220, 300, 300, "", 9999, 10, false);
        writeTextField = new TextField(-Display.getWidth()/2 + 20, Display.getHeight()/2 - 40, 300, 50, "", 50, 10, false);
    }

    public void addMessage(String message)
    {
        messeages.add(message);
    }

    public void update(List<String> messeages)
    {
        this.messeages = messeages;
        messeagesTextField.setText("");
        for (String m : messeages)
        {
            messeagesTextField.append(m + "\n");
        }
    }

    public void update(int delta)
    {
        writeTextField.update(delta);
    }

    public void append(String text)
    {
        writeTextField.append(text);
    }

    public void render()
    {
        messeagesTextField.render(8);
        writeTextField.render();
    }

    public List<String> getMesseages()
    {
        return messeages;
    }

    public void startWriting()
    {
        writeTextField.setText("");
        writeTextField.setActive(true);
    }

    public void stopWriting()
    {
        writeTextField.setText("");
        writeTextField.setActive(false);
    }

    public String getWriteMessage()
    {
        return writeTextField.getText();
    }
}
