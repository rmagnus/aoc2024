package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    List<Coordinate> vectors = List.of(Coordinate.of(-1, 0), Coordinate.of(0, 1), Coordinate.of(1, 0), Coordinate.of(0, -1));

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

    @Test
    void getPart2GetObstacleCount() {
        LOG.info("getPart2GetObstacleCount()");

        int count = getObstacleCount();

        LOG.info("steps: {}", count);

        Assertions.assertThat(count).isGreaterThan(-1);
    }

    private int getObstacleCount() {
        
        Map<Coordinate, Coordinate> map = new HashMap<>();

        int count = 0;
        int direction = 0;
        int x = startX;
        int y = startY;
        Coordinate vector = vectors.get(direction);
        while (true) {
            while (array[x + vector.getX()][y + vector.getY()] != '#') {
                map.put(Coordinate.of(x, y), vector);
                x = x + vector.getX();
                y = y + vector.getY();
                if (leavesArea(x, y, vector)) {
                    return count;
                }
                if (isPlaceForObstacle(map, x, y, direction)) {
                    LOG.info("x: {} y: {}",1 + x + vector.getX(),1 + y + vector.getY());
                    count = count + 1; 
                }
            }
            direction = (direction + 1) % 4;
            vector = vectors.get(direction);
        }
    }

    private boolean isPlaceForObstacle(Map<Coordinate, Coordinate> map, int x, int y,
            int direction)
    {
        Coordinate pos = Coordinate.of(x, y);

        if (!map.containsKey(pos)) {
            return false;
        }
        
        Coordinate lastVector = map.get(pos);
        
        return lastVector.equals(vectors.get((direction+1)%4));
    }

    private int getSteps() {
        
        Set<Pair<Integer,Integer>> pos = new HashSet<>();

        int direction = 0;
        int x = startX;
        int y = startY;
        Coordinate vector = vectors.get(direction);
        while (true) {
            while (array[x + vector.getX()][y + vector.getY()] != '#') {
                pos.add(Pair.of(x, y));
                x = x + vector.getX();
                y = y + vector.getY();
                if (leavesArea(x, y, vector)) {
                    return pos.size() + 1;
                }
            }
            direction = (direction + 1) % 4;
            vector = vectors.get(direction);
        }
    }

    private boolean leavesArea(int x, int y, Coordinate vector) {
        x = x + vector.getX();
        if ((x < 0) || (x == heigth)) {return true; }

        y = y + vector.getY();
        if ((y < 0) || (y == width)) {return true; }

        return false;
    }

}
