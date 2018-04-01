package de.benfm.dotmatrixdisplay;

import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class DotMatrixLayout extends GridLayout {
    private final String TAG = "DotMatrixLayout";
    private final int columnCount = 32;
    private final int rowCount = 18;
    private LinearLayout leds[][];

    public DotMatrixLayout(Context context)
    {
        super(context);
        initLeds(context);
    }

    public DotMatrixLayout(Context context, AttributeSet attributeSet)
    {
        super(context, attributeSet);
        initLeds(context);
    }

    public DotMatrixLayout(Context context, AttributeSet attributeSet, int defaultStyle)
    {
        super(context, attributeSet, defaultStyle);
        initLeds(context);
    }

    public void initLeds(Context context)
    {
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        leds = new LinearLayout[rowCount][columnCount];
        setColumnCount(columnCount);
        setRowCount(rowCount);

        for (int y = 0; y < rowCount; ++y)
        {
            for (int x = 0; x < columnCount; ++x)
            {
                leds[y][x] = (LinearLayout) inflater.inflate(R.layout.led_off, null);
                addView(
                    leds[y][x],
                    new GridLayout.LayoutParams(
                        GridLayout.spec(y, GridLayout.CENTER),
                        GridLayout.spec(x, GridLayout.CENTER)));
            }
        }
    }

    private void switchLed(int x, int y, int resourceId)
    {
        assert x >= 0 && x < columnCount;
        assert y >= 0 && y < rowCount;

        ImageView led = (ImageView) leds[y][x].getChildAt(0);
        led.setImageResource(resourceId);
    }

    public void switchLed(int x, int y, boolean on)
    {
        switchLed(x, y, on ? R.mipmap.led_on : R.mipmap.led_off);
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
        Log.i(TAG, "putPattern(x=" + Integer.toString(x) + ", y=" + Integer.toString(y) + ")");
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
        Point textDimension = font.getTextDimension(text);

        if (x + textDimension.x >= columnCount || y + textDimension.y >= rowCount)
        {
            throw new Exception("text exceeds the available space");
        }

        int px = x;
        for (int i = 0; i < text.length(); ++i)
        {
            FontCharacter character = font.getCharacter(text.charAt(i));
            putPattern(px, y, character.getPattern());

            px += character.getWidth() + 1;
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
}
