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

    private int checkXMAS(char[][] array, int x, int y, int dx, int dy, BorderCondition borderCondition)
    {
        int count = 0;

        while (!(borderCondition.isReached(array, x, y))) {
            // liegt mein Suchbereich im Array?
            if (inside(array, x, y) && inside(array, x + 3 * dx, y + 3 * dy)) {
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

    private boolean inside(char[][] array, int x, int y) {
        int heigth = array.length;
        int width = array[0].length;
        return (x < heigth) && (x >= 0) && (y >= 0) && (y < width);
    }

    @FunctionalInterface
    interface BorderCondition {
        boolean isReached(char[][] matrix, int currentRow, int currentCol);
    }

}
