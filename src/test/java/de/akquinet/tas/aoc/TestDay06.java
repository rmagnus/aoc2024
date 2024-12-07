package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay06 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    char[][] array;
    int heigth, width, startX, startY;
    List<Pair<Integer, Integer>> vectors = List.of(Pair.of(-1, 0), Pair.of(0, 1), Pair.of(1, 0), Pair.of(0, -1));

    @BeforeEach
    public void beforeEach() throws IOException {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day06/day06_map.txt"));

        width = lines.get(0).length();
        heigth = lines.size();

        array = new char[heigth][width];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            array[i] = line.toCharArray();
            if (line.indexOf('^') != -1) {
                startX = i;
                startY = line.indexOf('^');
            }
        }

    }

    @Test
    void getPart1GetSteps() {
        LOG.info("getPart1GetSteps()");

        int steps = getSteps();

        LOG.info("steps: {}", steps);

        Assertions.assertThat(steps).isEqualTo(5080);
    }

    private int getSteps() {
        
        Set<Pair<Integer,Integer>> pos = new HashSet<>();

        int direction = 0;
        int x = startX;
        int y = startY;
        Pair<Integer, Integer> vector = vectors.get(direction);
        while (true) {
            while (array[x + vector.getLeft()][y + vector.getRight()] != '#') {
                pos.add(Pair.of(x, y));
                x = x + vector.getLeft();
                y = y + vector.getRight();
                if (leavesArea(x, y, vector)) {
                    return pos.size() + 1;
                }
            }
            direction = (direction + 1) % 4;
            vector = vectors.get(direction);
        }
    }

    private boolean leavesArea(int x, int y, Pair<Integer, Integer> vector) {
        x = x + vector.getLeft();
        if ((x < 0) || (x == heigth)) {return true; }

        y = y + vector.getRight();
        if ((y < 0) || (y == width)) {return true; }

        return false;
    }

    @Test
    void getPart2XSearchInValidMiddlePage() throws IOException {
        LOG.info("getPart2XSearchInValidMiddlePage()");

    }

}
