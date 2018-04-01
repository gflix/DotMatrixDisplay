package de.benfm.dotmatrixdisplay;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.GridLayout;
import android.widget.LinearLayout;

public class DotMatrixLayout extends GridLayout {
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
}
