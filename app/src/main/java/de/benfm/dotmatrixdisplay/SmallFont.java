package de.benfm.dotmatrixdisplay;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class SmallFont {
    private final int columnCount = 3;
    private final int rowCount = 5;
    Map<String, boolean[][]> patterns = new HashMap<String, boolean[][]>();

    public SmallFont()
    {
        initFont();
    }

    private void initFont()
    {
        patterns.clear();

        boolean[][] pattern = new boolean[rowCount][columnCount];

        // 0
        pattern[0][1] = true;
        pattern[1][0] = true;
        pattern[1][2] = true;
        pattern[2][0] = true;
        pattern[2][2] = true;
        pattern[3][0] = true;
        pattern[3][2] = true;
        pattern[4][1] = true;
        patterns.put("0", pattern);

        // 1
        pattern = new boolean[rowCount][columnCount];
        pattern[0][1] = true;
        pattern[1][0] = true;
        pattern[1][1] = true;
        pattern[2][1] = true;
        pattern[3][1] = true;
        pattern[4][0] = true;
        pattern[4][1] = true;
        pattern[4][2] = true;
        patterns.put("1", pattern);

        // 2
        pattern = new boolean[rowCount][columnCount];
        pattern[0][1] = true;
        pattern[1][0] = true;
        pattern[1][2] = true;
        pattern[2][2] = true;
        pattern[3][1] = true;
        pattern[4][0] = true;
        pattern[4][1] = true;
        pattern[4][2] = true;
        patterns.put("2", pattern);

        // 3
        pattern = new boolean[rowCount][columnCount];
        pattern[0][0] = true;
        pattern[0][1] = true;
        pattern[0][2] = true;
        pattern[1][2] = true;
        pattern[2][1] = true;
        pattern[2][2] = true;
        pattern[3][2] = true;
        pattern[4][0] = true;
        pattern[4][1] = true;
        pattern[4][2] = true;
        patterns.put("3", pattern);

        // :
        pattern = new boolean[rowCount][columnCount];
        pattern[1][1] = true;
        pattern[3][1] = true;
        patterns.put(":", pattern);
    }

    public boolean[][] getPattern(String character)
    {
        return patterns.get(character);
    }

    public Set<String> getCharacters()
    {
        return patterns.keySet();
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
