package de.benfm.dotmatrixdisplay;

import android.graphics.Point;
import android.util.Log;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class Font {
    private static final String TAG = "Font";
    private String name = null;
    private int maxWidth = 0;
    private int height = 0;
    private Map<Character, FontCharacter> characters = new HashMap<Character, FontCharacter>();

    public Font(InputStream inputStream) throws Exception
    {
        loadFont(inputStream);
    }

    private void loadFont(InputStream inputStream) throws Exception
    {
        Log.i(TAG, "loadFont()");

        Document document = null;
        Element documentElement = null;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(inputStream);
            documentElement = document.getDocumentElement();
            //documentElement.normalize();

            if (!documentElement.getTagName().equals("Font"))
            {
                throw new Exception("invalid document element (" + documentElement.getTagName() + ")");
            }

            name = documentElement.getAttribute("name");
            maxWidth = Integer.parseInt(documentElement.getAttribute("maxWidth"));
            height = Integer.parseInt(documentElement.getAttribute("height"));

            NodeList characterNodes = documentElement.getElementsByTagName("Character");
            for (int i = 0; i < characterNodes.getLength(); ++i)
            {
                if (characterNodes.item(i).getNodeType() != Node.ELEMENT_NODE)
                {
                    continue;
                }
                Element characterNode = (Element) characterNodes.item(i);
                FontCharacter character = new FontCharacter(characterNode, maxWidth, height);
                characters.put(character.getId(), character);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
            return;
        }

        Log.i(TAG, "loadFont(success)");
    }

    public String getName()
    {
        return name;
    }

    public FontCharacter getCharacter(char character)
    {
        try
        {
            return characters.get(character);
        }
        catch (Exception e)
        {
            return null;
        }
    }

    public Point getTextDimension(String text) throws Exception
    {
        int width = 0;
        if (text != null)
        {
            for (int i = 0; i < text.length(); ++i)
            {
                char character = text.charAt(i);
                FontCharacter fontCharacter = getCharacter(character);
                if (fontCharacter == null)
                {
                    throw new Exception("character \"" + character + "\" not found in font \"" + name + "\"");
                }
                width += fontCharacter.getWidth() + (i >= 1 ? 1 : 0);
            }
        }
        return new Point(width, height);
    }
}
