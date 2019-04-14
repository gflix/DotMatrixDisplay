package de.benfm.dotmatrixdisplay;

import android.graphics.Point;

import javax.microedition.khronos.opengles.GL10;

public class DotMatrix {
    public DotMatrix()
    {
        leds = new boolean[rowCount][columnCount];
        reset();
    }

    public void reset()
    {
        for (int y = 0; y < rowCount; ++y)
        {
            for (int x = 0; x < columnCount; ++x)
            {
                leds[y][x] = false;
            }
        }
    }

    public void draw(GL10 gl, Led ledOn, Led ledOff)
    {
        for (int y = 0; y < rowCount; ++y)
        {
            for (int x = 0; x < columnCount; ++x)
            {
                Led led = leds[y][x] ? ledOn : ledOff;
                led.draw(gl, x + xOffset, -y + yOffset);
            }
        }
    }

    public void switchLed(int x, int y, boolean on)
    {
        if (x < 0 || x >= columnCount ||
                y < 0 || y >= rowCount)
        {
            return;
        }

        leds[y][x] = on;
    }

    public void switchLedOn(int x, int y)
    {
        switchLed(x, y, true);
    }

    public void switchLedOff(int x, int y)
    {
        switchLed(x, y, false);
    }

    public void putPattern(int x, int y, boolean pattern[][])
    {
        int py = 0;
        for (boolean[] row: pattern)
        {
            int px = 0;
            for (boolean column: row)
            {
                switchLed(x + px, y + py, column);
                ++px;
            }
            ++py;
        }
    }

    public void putString(int x, int y, Font font, String text) throws Exception
    {
        if (font == null)
        {
            throw new Exception("invalid font");
        }
        Point textDimension = font.getTextDimension(text);

        int px = x;
        for (int i = 0; i < text.length(); ++i)
        {
            char character = text.charAt(i);
            FontCharacter fontCharacter = font.getCharacter(character);
            if (fontCharacter == null)
            {
                throw new Exception("unable to retrieve character \"" + character +
                        "\" from font \"" + font.getName() + "\"");
            }
            putPattern(px, y, fontCharacter.getPattern());

            px += fontCharacter.getWidth() + 1;
        }
    }

    public int getColumnCount()
    {
        return columnCount;
    }

    public int getRowCount()
    {
        return rowCount;
    }

    private final String TAG = "DotMatrix";
    private final int columnCount = 38;
    private final int rowCount = 21;
    private final float yOffset = (rowCount - 1) / 2.0f;
    private final float xOffset = -(columnCount - 1) / 2.0f;
    private boolean leds[][];
}
