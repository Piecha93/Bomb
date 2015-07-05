package graphics;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Tomek on 2015-03-21.
 */
public class SpriteSheetLoader
{
    public static Map<String, Sprite> setUpSpriteSheets(String spritesheetLocation)
    {
        Map<String, Sprite> spriteMap = new LinkedHashMap<String, Sprite>();
        SAXBuilder builder = new SAXBuilder();
        try
        {
            Document document = builder.build(new File(spritesheetLocation));
            Element root = document.getRootElement();
            for (Object spriteObject : root.getChildren())
            {
                Element spriteElement = (Element) spriteObject;
                String name = spriteElement.getAttributeValue("n");
                int x = spriteElement.getAttribute("x").getIntValue();
                int y = spriteElement.getAttribute("y").getIntValue();
                int w = spriteElement.getAttribute("w").getIntValue();
                int h = spriteElement.getAttribute("h").getIntValue();
                Sprite sprite = new Sprite(name, x, y, w, h);
                spriteMap.put(sprite.getName(), sprite);
            }
        }
        catch (JDOMException e)
        {
            e.printStackTrace();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return spriteMap;
    }
}
