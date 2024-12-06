package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay04 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    char[][] array;
    int heigth, width;
    
    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay04.class.getResourceAsStream("/day04_list.txt"));

        Assertions.assertThat(lines).isNotEmpty();

        width = lines.get(0).length();
        heigth = lines.size();

        array = new char[heigth][width];

        for (int i = 0; i < lines.size(); i++) {
            array[i] = lines.get(i).toCharArray();
        }
    }

    @Test
    void getPart1XMASSearch() throws IOException {
        LOG.info("getPart1XMASSearch()");


        int result = 0;

        // vorwärts, jede Zeile scannen
        for (int i = 0; i < heigth; i++) {
            result = result + checkXMAS(array, i, 0, 0, 1,
                    (arry, x, y) ->  (y == width)
                    );
        }

        // rückwärts, jede Zeile scannen
        for (int i = 0; i < heigth; i++) {
            result = result + checkXMAS(array, i, width - 1, 0, -1,
                    (arry, x, y) ->  (y == 0)
                    );
        }

        // runter, jede Spalte scannen
        for (int i = 0; i < width; i++) {
            result = result + checkXMAS(array, 0, i , 1, 0,
                    (arry, x, y) ->  (x == heigth)
                    );
        }

        // hoch, jede Spalte scannen
        for (int i = 0; i < width; i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, 0,
                    (arry, x, y) ->  (x == 0)
                    );
        }

        // vorwärts und runter, jede Diagonale scannen
        for (int i = 0 - heigth; i < width; i++) {
            result = result + checkXMAS(array, 0, i, 1, 1,
                    (arry, x, y) ->  (y == width)
                    );
        }

        // vorwärts und hoch, jede Diagonale scannen
        for (int i = 0 - heigth; i < width; i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, 1,
                    (arry, x, y) ->  (y == width)
                    );
        }

        // rückwärts und runter, jede Diagonale scannen
        for (int i = 0; i < (width + heigth); i++) {
            result = result + checkXMAS(array, 0, i, 1, -1,
                    (arry, x, y) ->  (x == heigth)
                    );
        }

        // rückwärts und hoch, jede Diagonale scannen
        for (int i = 0; i < (width + heigth); i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, -1,
                    (arry, x, y) ->  (x == 0)
                    );
        }


        LOG.info("result: {}", result);
        
        Assertions.assertThat(result).isEqualTo(2532);

    }

    @Test
    void getPart2XMASSearch()
    {
        LOG.info("getPart2XMASSearch()");

        int result = 0;
        // finde alle 'A', die direkt am Rand interessieren nicht
        for (int i = 1; i < heigth - 1; i++) {
            for (int j = 1; j < width - 1; j++) {
                if (array[i][j] == 'A' && checkMAS(i, j)) {
                    result = result + 1;
                }
            }
        }

        LOG.info("result: {}", result);

        Assertions.assertThat(result).isEqualTo(1941);

    }
    
    private boolean checkMAS(int i, int j) {
        // left
        String str11 = "" + array[i - 1][j - 1] + array[i][j] + array[i + 1][j + 1];
        String str12 = "" + array[i + 1][j - 1] + array[i][j] + array[i - 1][j + 1];

        if ("MAS".equals(str11) && "MAS".equals(str12)) {
            return true;
        }

        // right
        String str21 = "" + array[i - 1][j + 1] + array[i][j] + array[i + 1][j - 1];
        String str22 = "" + array[i + 1][j + 1] + array[i][j] + array[i - 1][j - 1];

        if ("MAS".equals(str21) && "MAS".equals(str22)) {
            return true;
        }

        // up
        String str31 = "" + array[i + 1][j - 1] + array[i][j] + array[i - 1][j + 1];
        String str32 = "" + array[i + 1][j + 1] + array[i][j] + array[i - 1][j - 1];
        if ("MAS".equals(str31) && "MAS".equals(str32)) {
            return true;
        }

        // down
        String str41 = "" + array[i - 1][j - 1] + array[i][j] + array[i + 1][j + 1];
        String str42 = "" + array[i - 1][j + 1] + array[i][j] + array[i + 1][j - 1];
        if ("MAS".equals(str41) && "MAS".equals(str42)) {
            return true;
        }

        return false;
    }

    private int checkXMAS(char[][] array, int x, int y, int dx, int dy, BorderCondition borderCondition)
    {
        int count = 0;

        while (!(borderCondition.isReached(array, x, y))) {
            // liegt mein Suchbereich im Array?
            if (inside(x, y) && inside(x + 3 * dx, y + 3 * dy)) {
                // wenn die 4 Zeichen in Suchrichtung 'XMAS' sind
                String str = "" + array[x][y] + array[x + 1 * dx][y + 1 * dy] + array[x + 2 * dx][y + 2 * dy]
                        + array[x + 3 * dx][y + 3 * dy];
                if ("XMAS".equals(str)) {
                    count = count + 1;
                    x = x + 2 * dx;
                    y = y + 2 * dy;
                }
            }
            x = x + dx;
            y = y + dy;
        }

        return count;

    }

    private boolean inside(int x, int y) {
        return (x < heigth) && (x >= 0) && (y >= 0) && (y < width);
    }

    @FunctionalInterface
    interface BorderCondition {
        boolean isReached(char[][] matrix, int currentRow, int currentCol);
    }

}
