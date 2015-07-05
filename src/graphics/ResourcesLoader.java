package graphics;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class ResourcesLoader
{
    /*
         textures and spritemaps
    */
    private static String SPRITESHEET_TILEMAP_LOCATION = "res/map/tileset2.png";
    private static String SPRITESHEET_TILEMAPWINTER_LOCATION = "res/map/tileset-winter.png";
    private static String SPRITESHEET_TILEMAP_XML_LOCATION = "res/map/tileset2.xml";
    private static Texture tiledmapTexture;
    private static Map<String, Sprite> tiledmapSprites;

    public static Map<String, Sprite> getTiledmapSprites()
    {
        return tiledmapSprites;
    }

    public static Texture getTiledmapTexture()
    {
        return tiledmapTexture;
    }

    private static String SPRITESHEET_BOMB_LOCATION = "res/bomb/bomb.png";
    private static String SPRITESHEET_BOMB_XML_LOCATION = "res/bomb/bomb.xml";
    private static Texture bombTexture;
    private static Map<String, Sprite> bombSprites;

    public static Map<String, Sprite> getBombSprites()
    {
        return bombSprites;
    }

    public static Texture getBombTexture()
    {
        return bombTexture;
    }

    private static String SPRITESHEET_CHRACTER_GOLD_LOCATION = "res/character/character_gold.png";
    private static String SPRITESHEET_CHARACTER_XML_LOCATION = "res/character/character.xml";
    private static Texture playerTexture;
    private static Map<String, Sprite> playerSprites;

    public static Map<String, Sprite> getPlayerSprites()
    {
        return playerSprites;
    }

    public static Texture getPlayerTexture()
    {
        return playerTexture;
    }

    private static String SPRITESHEET_CHRACTER_DEATH_LOCATION = "res/character/death.png";
    private static String SPRITESHEET_CHARACTER_DEATH_XML_LOCATION = "res/character/death.xml";
    private static Texture deathTexture;
    private static Map<String, Sprite> deathSprites;

    public static Map<String, Sprite> getPlayerDeathSprites()
    {
        return deathSprites;
    }

    public static Texture getPlayerDeathTexture()
    {
        return deathTexture;
    }

    private static String SPRITESHEET_EXPLOSION_LOCATION = "res/bomb/explosion.png";
    private static String SPRITESHEET_EXPLOSION_XML_LOCATION = "res/bomb/explosion.xml";
    private static Texture explosionTexture;
    private static Map<String, Sprite> explosionSprites;

    public static Map<String, Sprite> getExplosionSprites()
    {
        return explosionSprites;
    }

    public static Texture getExplosionTexture()
    {
        return explosionTexture;
    }

    private static String SPRITESHEET_BONUS_LOCATION = "res/bonus/bonus.png";
    private static String SPRITESHEET_BONUS_XML_LOCATION = "res/bonus/bonus.xml";
    private static Texture bonusTexture;
    private static Map<String, Sprite> bonusSprites;

    public static Map<String, Sprite> getBonusSprites()
    {
        return bonusSprites;
    }

    public static Texture getBonusTexture()
    {
        return bonusTexture;
    }

    private static String SPRITESHEET_ALPHABET_LOCATION = "res/textbox/alphabet.png";
    private static String SPRITESHEET_ALPHABETBLUE_LOCATION = "res/textbox/alphabetblue.png";
    private static String SPRITESHEET_ALPHABET__XML_LOCATION = "res/textbox/alphabet.xml";
    private static Texture alphabetTexture;
    private static Texture alphabetBlueTexture;
    private static Map<String, Sprite> alphabetSprites;

    public static Map<String, Sprite> getalphabetSprites()
    {
        return alphabetSprites;
    }

    public static Texture getalphabetTexture()
    {
        return alphabetTexture;
    }
    public static Texture getalphabetBlueTexture()
    {
        return alphabetBlueTexture;
    }

    private static String STARTSCREEN_LOCATION = "res/start.png";
    private static Texture startScreen;

    public static Texture getStartScreenTexture()
    {
        return startScreen;
    }

    private static String TEXTBOX_LOCATION = "res/textbox/textbox.png";
    private static Texture textBox;

    public static Texture getTextBoxTexture()
    {
        return textBox;
    }

    /*
        fonts
     */
    private static Font awtFont;
    private static TrueTypeFont font;

    public static TrueTypeFont getFont()
    {
        return font;
    }

    public static void LoadResources()
    {
        try
        {
            // load textures
            tiledmapTexture     = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_TILEMAP_LOCATION)));
            bombTexture         = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_BOMB_LOCATION)));
            playerTexture       = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_CHRACTER_GOLD_LOCATION)));
            explosionTexture    = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_EXPLOSION_LOCATION)));
            bonusTexture        = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_BONUS_LOCATION)));
            deathTexture        = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_CHRACTER_DEATH_LOCATION)));
            alphabetTexture     = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_ALPHABET_LOCATION)));
            alphabetBlueTexture = TextureLoader.getTexture("PNG", new FileInputStream(new File(SPRITESHEET_ALPHABETBLUE_LOCATION)));
            startScreen         = TextureLoader.getTexture("PNG", new FileInputStream(new File(STARTSCREEN_LOCATION)));
            textBox             = TextureLoader.getTexture("PNG", new FileInputStream(new File(TEXTBOX_LOCATION)));


            //load sprites
            tiledmapSprites     = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_TILEMAP_XML_LOCATION);
            bombSprites         = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_BOMB_XML_LOCATION);
            playerSprites       = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_CHARACTER_XML_LOCATION);
            explosionSprites    = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_EXPLOSION_XML_LOCATION);
            bonusSprites        = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_BONUS_XML_LOCATION);
            deathSprites        = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_CHARACTER_DEATH_XML_LOCATION);
            alphabetSprites     = SpriteSheetLoader.setUpSpriteSheets(SPRITESHEET_ALPHABET__XML_LOCATION);

            //load font
            awtFont = new Font("Times New Roman", Font.BOLD, 10);
            font = new TrueTypeFont(awtFont, false);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
        catch (IOException e)
        {
            e.printStackTrace();
            Display.destroy();
            System.exit(1);
        }
    }
}
