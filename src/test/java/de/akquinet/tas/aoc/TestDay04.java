package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay04 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private char[] xmas = { 'X', 'M', 'A', 'S' };

    @Test
    void getPart1XMASSearch() throws IOException {
        LOG.info("getPart1XMASSearch()");

        List<String> lines = IOUtils.readLines(TestDay04.class.getResourceAsStream("/day04_list.txt"));

        Assertions.assertThat(lines).isNotEmpty();

        int width = lines.get(0).length();
        int heigth = lines.size();

        var array = new char[heigth][width];

        for (int i = 0; i < lines.size(); i++) {
            array[i] = lines.get(i).toCharArray();

        }

        int result = 0;

        // Für die "gerade" Sachen brauchen wir keine extra Inside-Be

        // vorwärts, jede Zeile scannen
        for (int i = 0; i < heigth; i++) {
            result = result + checkXMAS(array, i, 0, 0, 1,
                    (arry, x, y) ->  (y == width),
                    (arry, x, y) ->  (y < width)
                    );
        }

        // rückwärts, jede Zeile scannen
        for (int i = 0; i < heigth; i++) {
            result = result + checkXMAS(array, i, width - 1, 0, -1,
                    (arry, x, y) ->  (y == 0),
                    (arry, x, y) ->  (y >= 0)
                    );
        }

        // runter, jede Spalte scannen
        for (int i = 0; i < width; i++) {
            result = result + checkXMAS(array, 0, i , 1, 0,
                    (arry, x, y) ->  (x == heigth),
                    (arry, x, y) ->  ((x < heigth) && (y < width))
                    );
        }

        // hoch, jede Spalte scannen
        for (int i = 0; i < width; i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, 0,
                    (arry, x, y) ->  (x == 0),
                    (arry, x, y) ->  ((x >= 0) && (y >= 0))
                    );
        }

        // vorwärts und runter, jede Diagonale scannen
        for (int i = 0 - heigth; i < width; i++) {
            result = result + checkXMAS(array, 0, i, 1, 1,
                    (arry, x, y) ->  ((y == width) || (x == heigth)),
                    (arry, x, y) ->  ((x < heigth) && (x >= 0) && (y >= 0) && (y < heigth))
                    );
        }

        // vorwärts und hoch, jede Diagonale scannen
        for (int i = 0 - heigth; i < width; i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, 1,
                    (arry, x, y) ->  ((x == 0) || (y == width)),
                    (arry, x, y) ->  ((x < heigth) && (x >= 0) && (y >= 0) && (y < heigth))
                    );
        }

        // rückwärts und runter, jede Diagonale scannen
        for (int i = 0; i < (width + heigth); i++) {
            result = result + checkXMAS(array, 0, i, 1, -1,
                    (arry, x, y) ->  (x == heigth),
                    (arry, x, y) ->  ((x < heigth) && (x >= 0) && (y >= 0) && (y < heigth))
                    );
        }

        // rückwärts und hoch, jede Diagonale scannen
        for (int i = 0; i < (width + heigth); i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, -1,
                    (arry, x, y) ->  (x == 0),
                    (arry, x, y) ->  ((x < heigth) && (x >= 0) && (y >= 0) && (y < heigth))
                    );
        }


        LOG.info("result: {}", result);
        
        Assertions.assertThat(result).isEqualTo(2532);

    }

    private int checkXMAS(char[][] array, int x, int y, int dx, int dy, BorderCondition borderCondition,
            InsideCondition ic) {
        if (borderCondition.isReached(array, x, y)) {
            return 0;
        }

        int count = 0;

        while (!(borderCondition.isReached(array, x, y))) {
            // liegt mein Suchbereich im Array?
            if (ic.isInside(array, x, y) && ic.isInside(array, x + 3 * dx, y + 3 * dy)) {
                // wenn die 4 Zeichen in Suchrichtung 'xmas' sind
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

    @FunctionalInterface
    interface BorderCondition {
        boolean isReached(char[][] matrix, int currentRow, int currentCol);
    }

    @FunctionalInterface
    interface InsideCondition {
        boolean isInside(char[][] matrix, int currentRow, int currentCol);
    }

}
