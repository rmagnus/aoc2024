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
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day08/day08_map_test.txt"));

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
        
        logNodePos();
        
        int count = set.size();
        
        Assertions.assertThat(count).isEqualTo(14);
    }

    private void logNodePos() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array2.length; i++) {          // Rows
            for (int j = 0; j < array2[0].length; j++) {   // Columns
                sb.append(array2[i][j]);
            }
            sb.append("\n");
        }

        LOG.info("points:\n{}", sb);
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
        List<Coordinate> line = getPointsOnLine(points.getLeft(), points.getRight());
        line.stream()
            .filter(p -> {
                boolean b = isRightDistance(p, points.getLeft(), points.getRight());
                if (b) {
                    array2[p.getX()][p.getY()] = '#';
                    LOG.info("p1: {} p2: {} pos: {}", points.getLeft(), points.getRight(), p);
                    logNodePos();
                }
                return b; 
            })
            .forEach(c -> set.add(c));
        
        return set;
    }

    private boolean isRightDistance(Coordinate p, Coordinate left,
            Coordinate right)
    {
        int d1 = distance(p, left);
        int d2 = distance(p, right);
        return ((d1 == 2 * d2) || (d2 == 2 * d1)); 
    }

    private int distance(Coordinate p1, Coordinate p2)
    {
        int r1 = p1.getX(); // Column index of the start point
        int c1 = p1.getY(); // Row index of the start point
        int r2 = p2.getX();   // Column index of the end point
        int c2 = p2.getY();   // Row index of the end point
        
        double sqrt = Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(c2 - c1, 2));

        return (int)sqrt;
    }

    private List<Coordinate> getPointsOnLine(Coordinate p1, Coordinate p2) {
        List<Coordinate> pointsOnLine = new ArrayList<>();

        int x1 = p1.getX(); // Column index of the start point
        int y1 = p1.getY(); // Row index of the start point
        int x2 = p2.getX();   // Column index of the end point
        int y2 = p2.getY();   // Row index of the end point

        int dx = x2 - x1;
        int dy = y2 - y1;

        for (int i = 0; i < array.length; i++) {          // Rows
            for (int j = 0; j < array[0].length; j++) {   // Columns
                // Check if the point (i, j) satisfies the line equation
                if ((i - y1) * dx == (j - x1) * dy) {
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
