package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collection;
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

class TestDay08 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    char[][] array, array2;
    int heigth, width;
    Map<Character, List<Coordinate>> map = new HashMap<>();

    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day08/day08_map.txt"));

        width = lines.get(0).length();
        heigth = lines.size();

        array = new char[heigth][width];
        array2 = new char[heigth][width];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            array[i] = line.toCharArray();
            array2[i] = line.toCharArray();
            for (int j = 0; j < array.length; j++) {
                char c = array[i][j];
                if (c != '.') {
                    if (map.containsKey(c)) {
                        map.get(c).add(Coordinate.of(i, j));
                    } else {
                        map.put(c, new ArrayList<>(List.of(Coordinate.of(i, j))));
                    }
                }
            }
        }
    }

    @Test
    void getPart1Count()
    {
        LOG.info("getPart1Count()");

        Set<Coordinate> set = new HashSet<>();

        map.values().stream()
                .forEach(l -> set.addAll(getValidAntiNodes(l)));

        int count = set.size();

        Assertions.assertThat(count).isEqualTo(269);
    }

    private Set<Coordinate> getValidAntiNodes(List<Coordinate> points) {
        Set<Coordinate> set = new HashSet<>();

        getAllCombinations(points).stream()
            .map(p -> getSetOfAntiNodes(p))
            .flatMap(Collection::stream)
            .forEach(c -> set.add(c));

        return set;
    }

    private Set<Coordinate> getSetOfAntiNodes(Pair<Coordinate, Coordinate> points)
    {
        Set<Coordinate> set = new HashSet<>();
        Set<Coordinate> line = getPointsOnLine(points.getLeft(), points.getRight());
        line.stream()
            .filter(p ->  isRightDistance(p, points.getLeft(), points.getRight()))
            .forEach(c -> set.add(c));

        return set;
    }

    private boolean isRightDistance(Coordinate p, Coordinate a,
            Coordinate b)
    {
        int dxpa = p.getX() - a.getX();
        int dypa = p.getY() - a.getY();
        int dxpb = p.getX() - b.getX();
        int dypb = p.getY() - b.getY();

        if (((dxpa == (2 * dxpb)) && (dypa == (2 * dypb) )) || ((dxpb == (2 * dxpa)) && (dypb == (2 * dypa) ))) {
            return true;
        }

        return false;
    }

    private Set<Coordinate> getPointsOnLine(Coordinate p1, Coordinate p2) {
        Set<Coordinate> pointsOnLine = new HashSet<>();

        int x1 = p1.getX(); // Column index of the start point
        int y1 = p1.getY(); // Row index of the start point
        int x2 = p2.getX(); // Column index of the end point
        int y2 = p2.getY(); // Row index of the end point

        int dx = x2 - x1;
        int dy = y2 - y1;

        for (int i = 0; i < array.length; i++) { // Rows
            for (int j = 0; j < array[0].length; j++) { // Columns
                // Check if the point (i, j) satisfies the line equation
                if (((i - y1) * dx) == ((j - x1) * dy)) {
                    pointsOnLine.add(Coordinate.of(j, i));
                }
            }
        }

        return pointsOnLine;
    }

    @Test
    void getPart2Sum()
    {
        LOG.info("getPart2Sum()");

    }

    private List<Pair<Coordinate, Coordinate>> getAllCombinations(
            List<Coordinate> points) {
        List<Pair<Coordinate, Coordinate>> combinations = new ArrayList<>();

        // Outer loop for the first element
        for (int i = 0; i < points.size(); i++) {
            // Inner loop for the second element
            for (int j = i + 1; j < points.size(); j++) {
                combinations.add(Pair.of(points.get(i), points.get(j)));
            }
        }
        return combinations;
    }
}
