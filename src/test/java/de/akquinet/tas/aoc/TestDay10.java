package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay10 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    char[][] array;
    int heigth, width;
    List<Coordinate> startPoints = new ArrayList<>();

    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day10/day10_map_test.txt"));

        width = lines.get(0).length();
        heigth = lines.size();

        array = new char[heigth][width];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            char[] charArray = line.toCharArray();
            for (int j = 0; j < charArray.length; j++) {
                char p = charArray[j];
                array[i][j] = charArray[j];
                if (p == '0') {
                    startPoints.add(Coordinate.of(i, j));
                }
            }
        }
    }

    @Test
    void getPart1Sum()
    {
        LOG.info("getPart1Sum()");

        long count = getNumberOfTrailHeads(array, startPoints);

        LOG.info("count: {}", count);

        Assertions.assertThat(count).isEqualTo(36);
    }

    private long getNumberOfTrailHeads(char[][] array, List<Coordinate> startPoints2) {
        
        return startPoints2.stream()
            .map(p -> getTrailHeads(array, p, 0))
            .flatMap(Collection::stream)
            .count();
    }

    private Set<Coordinate> getTrailHeads(char[][] array, Coordinate p, int level) {
        LOG.debug("point: {}, level: {}", p, level);
        
        if (level == 9) {
            return Set.of(p);
        }
        
        List<Coordinate> nextPoints = getNextPoints(array, p, level + 1);
        
        Set<Coordinate> reachedHeads = new HashSet<>();

        nextPoints.forEach(
                nextPoint -> reachedHeads.addAll(getTrailHeads(array, nextPoint, level + 1))
                );

        return reachedHeads;
    }

    private List<Coordinate> getNextPoints(char[][] array, Coordinate p, int level)
    {
        List<Coordinate> points = new ArrayList<>();

        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                int x = p.getX() + i;
                int y = p.getY() + j;
                if (inside(x, y) && (Math.abs(i) + Math.abs(j) == 1) && array[x][y] == level + 48) {
                    Coordinate nextP = Coordinate.of(x, y);
                    LOG.debug("pos: {} level: {} next Point: {}", p, level, nextP);
                    points.add(nextP);
                }
            }
        }
        return points;
    }

    private boolean inside(int x, int y) {
        return (x < heigth) && (x >= 0) && (y >= 0) && (y < width);
    }

 }
