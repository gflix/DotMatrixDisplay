package de.benfm.dotmatrixdisplay;

import android.util.Log;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class FontCharacter
{
    private static final String TAG = "Character";
    private char id;
    private int width = 0;
    private int height = 0;
    private boolean pattern[][] = null;

    public FontCharacter(Element element, int maxWidth, int height) throws Exception
    {
        if (!element.hasAttribute("id"))
        {
            throw new Exception("no attribute id");
        }
        String idText = element.getAttribute("id");
        if (idText.length() != 1)
        {
            throw new Exception("invalid length of attribute id (" + idText + "), must be 1");
        }
        id = idText.charAt(0);

        NodeList rowNodes = element.getElementsByTagName("Row");
        if (rowNodes.getLength() != height)
        {
            throw new Exception("invalid height of character \"" + id + "\"");
        }
        this.height = height;

        int minCharacterWidth = maxWidth;
        int maxCharacterWidth = 0;
        for (int i = 0; i < height; ++i)
        {
            Node rowNode = rowNodes.item(i);
            if (rowNode.getNodeType() != Node.ELEMENT_NODE)
            {
                throw new Exception("invalid row element within character \"" + id + "\"");
            }
            if (!rowNode.hasChildNodes() || rowNode.getFirstChild().getNodeType() != Node.TEXT_NODE)
            {
                throw new Exception("invalid child of row element within character \"" + id + "\"");
            }
            int rowTextLength = rowNode.getFirstChild().getTextContent().length();

            minCharacterWidth = min(minCharacterWidth, rowTextLength);
            maxCharacterWidth = max(minCharacterWidth, rowTextLength);
        }
        if (minCharacterWidth != maxCharacterWidth)
        {
            throw new Exception("character width differs within character \"" + id + "\"");
        }
        width = minCharacterWidth;

        pattern = new boolean[height][width];
        for (int y = 0; y < height; ++y)
        {
            String rowText = rowNodes.item(y).getFirstChild().getTextContent();
            for (int x = 0; x < width; ++x)
            {
                pattern[y][x] = rowText.charAt(x) == 'X';
            }
        }

        Log.i(TAG, "Loaded character \"" + id + "\" with width=" + Integer.toString(width));
    }

    public char getId()
    {
        return id;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }

    public boolean[][] getPattern()
    {
        return pattern;
    }

    private int min(int a, int b)
    {
        return a < b ? a : b;
    }

    private int max(int a, int b)
    {
        return a > b ? a : b;
    }
}
