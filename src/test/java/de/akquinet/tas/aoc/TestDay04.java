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

        // search forward
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array.length - 3; j++) {
                if (isXMAS(array, i, j, i, j + 1, i, j + 2, i, j + 3)) {
                    result++;
                }
            }
        }

        // search backward
        for (int i = 0; i < array.length; i++) {
            for (int j = 3; j < array.length; j++) {
                if (isXMAS(array, i, j, i, j - 1, i, j - 2, i, j - 3)) {
                    result++;
                }
            }
        }

        // search down
        for (int i = 0; i < array.length - 3; i++) {
            for (int j = 0; j < array.length; j++) {
                if (isXMAS(array, i, j, i + 1, j, i + 2, j, i + 3, j)) {
                    result++;
                }
            }
        }

        // search up
        for (int i = 3; i < array.length; i++) {
            for (int j = 0; j < array.length; j++) {
                if (isXMAS(array, i, j, i - 1, j, i - 2, j, i - 3, j)) {
                    result++;
                }
            }
        }

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

        LOG.info("result: {}", result);

    }

    private boolean isXMAS(char[][] array, int... indizies) {
        int h1 = indizies[0];
        int w1 = indizies[1];
        int h2 = indizies[2];
        int w2 = indizies[3];
        int h3 = indizies[4];
        int w3 = indizies[5];
        int h4 = indizies[6];
        int w4 = indizies[7];

        int height = array.length;
        int width = array[0].length;

        if ((h1 > height) || (h2 > height) || (h3 > height) || (h4 > height)) {
            return false;
        }

        if ((w1 > width) || (w2 > width) || (w3 > width) || (w4 > width)) {
            return false;
        }

        boolean b = array[h1][w1] == 'X' && array[h2][w2] == 'M' && array[h3][w3] == 'A' && array[h4][w4] == 'S';
        return b;

    }

}
