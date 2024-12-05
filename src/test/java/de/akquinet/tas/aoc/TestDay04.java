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

        List<String> lines = IOUtils.readLines(TestDay04.class.getResourceAsStream("/day04_list_test.txt"));

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
            result = result + checkXMAS(array, i, 0, 0, 1);
        }

        // rückwärts, jede Zeile scannen
        for (int i = 0; i < heigth; i++) {
            result = result + checkXMAS(array, i, width - 1, 0, -1);
        }

        // runter, jede Spalte scannen
        for (int i = 0; i < width; i++) {
            result = result + checkXMAS(array, 0, i , 1, 0);
        }

        // hoch, jede Spalte scannen
        for (int i = 0; i < width; i++) {
            result = result + checkXMAS(array, heigth - 1, i, -1, 0);
        }

/*

        // search diagonal right down
        for (int i = 0; i < array.length - 3; i++) {
            for (int j = 0; j < array.length - 3; j++) {
                if (isXMAS(array, i, j, i + 1, j + 1, i + 2, j + 2, i + 3, j + 3)) {
                    result++;
                }
            }
        }

        // search diagonal right up
        for (int i = 3; i < array.length; i++) {
            for (int j = 0; j < array.length - 3; j++) {
                if (isXMAS(array, i, j, i - 1, j + 1, i - 2, j + 2, i - 3, j + 3)) {
                    result++;
                }
            }
        }

        // search diagonal left down
        for (int i = 0; i < array.length - 3; i++) {
            for (int j = 3; j < array.length; j++) {
                if (isXMAS(array, i + 3, j, i + 2, j - 1, i + 1, j - 2, i, j - 3)) {
                    result++;
                }
            }
        }

        // search diagonal left up
        for (int i = 3; i < array.length; i++) {
            for (int j = 3; j < array.length; j++) {
                if (isXMAS(array, i, j, i - 1, j - 1, i - 2, j - 2, i - 3, j - 3)) {
                    result++;
                }
            }
        }
*/

        LOG.info("result: {}", result);

    }

    private int checkXMAS(char[][] array, int x, int y, int dx, int dy)
    {
        if (reachBorder(array, x, y)) {
            return 0;
        }
        
        int count = 0;
        char[] xmas = {'X', 'M', 'A', 'S' };

        for (int i = 0; i < xmas.length; i++) {
            while (!reachBorder(array, x, y)) {
                char c = array[x][y];
                if (c == xmas[i]) {
                    count++;
                    break;
                }
                x = x + dx;
                y = y + dy;
            }
        }
        
        if (count < 4) {
            return checkXMAS(array, x, y, dx, dy);
        }
        
        return 1 + checkXMAS(array, x, y, dx, dy);
    }
    
    private boolean reachBorder(char[][] array, int x, int y) {
        int width = array[0].length;
        int heigth = array.length;

        return (x == width) || (y == heigth) || (x == -1) || (y == -1);
    }

}
