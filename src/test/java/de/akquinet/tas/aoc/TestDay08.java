package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    Map<Character, List<Pair<Integer, Integer>>> map = new HashMap<>();
    
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
                        map.get(c).add(Pair.of(i, j));
                    } else {
                        map.put(c, new ArrayList<>(List.of(Pair.of(i, j))));
                    }
                }
            }
        }
    }

    @Test
    void getPart1Count()
    {
        LOG.info("getPart1Count()");
        
        long count = map.entrySet().stream()
            .map(e -> getValidAntiNodes(e.getValue()))
            .reduce(0l, (a,b) -> a+b);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array2.length; i++) {          // Rows
            for (int j = 0; j < array2[0].length; j++) {   // Columns
                sb.append(array2[i][j]);
            }
            sb.append("\n");
        }

        LOG.info("points:\n{}", sb);
        
        Assertions.assertThat(count).isEqualTo(14);
    }

    private Long getValidAntiNodes(List<Pair<Integer, Integer>> points) {
        return getAllCombinations(points).stream()
            .map(p -> getNumberOfAntiNodes(p))
            .reduce(0l, (a,b) -> a+b);
    }

    private long getNumberOfAntiNodes(Pair<Pair<Integer, Integer>, Pair<Integer, Integer>> points) {
        
        long count = 0;
        List<Pair<Integer, Integer>> line = getPointsOnLine(points.getLeft(), points.getRight());
        count = line.stream()
            .filter(p -> {
                boolean b = isRightDistance(p, points.getLeft(), points.getRight());
                if (b) {
                    array2[p.getLeft()][p.getRight()] = '#';
                }
                return b; 
            })
            .count();
        
        return count;
    }

    private boolean isRightDistance(Pair<Integer, Integer> p, Pair<Integer, Integer> left,
            Pair<Integer, Integer> right)
    {
        int d1 = distance(p, left);
        int d2 = distance(p, right);
        return ((d1 == 2 * d2) || (d2 == 2 * d1)); 
    }

    private int distance(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2)
    {
        int r1 = p1.getLeft(); // Column index of the start point
        int c1 = p1.getRight(); // Row index of the start point
        int r2 = p2.getLeft();   // Column index of the end point
        int c2 = p2.getRight();   // Row index of the end point
        
        double sqrt = Math.sqrt(Math.pow(r2 - r1, 2) + Math.pow(c2 - c1, 2));

        return (int)sqrt;
    }

    private List<Pair<Integer, Integer>> getPointsOnLine(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
        List<Pair<Integer, Integer>> pointsOnLine = new ArrayList<>();

        int x1 = p1.getLeft(); // Column index of the start point
        int y1 = p1.getRight(); // Row index of the start point
        int x2 = p2.getLeft();   // Column index of the end point
        int y2 = p2.getRight();   // Row index of the end point

        int dx = x2 - x1;
        int dy = y2 - y1;

        for (int i = 0; i < array.length; i++) {          // Rows
            for (int j = 0; j < array[0].length; j++) {   // Columns
                // Check if the point (i, j) satisfies the line equation
                if ((i - y1) * dx == (j - x1) * dy) {
                    pointsOnLine.add(Pair.of(j, i));
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

    private List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> getAllCombinations(
            List<Pair<Integer, Integer>> nums) {
        List<Pair<Pair<Integer, Integer>, Pair<Integer, Integer>>> combinations = new ArrayList<>();

        // Outer loop for the first element
        for (int i = 0; i < nums.size(); i++) {
            // Inner loop for the second element
            for (int j = i + 1; j < nums.size(); j++) {
                combinations.add(Pair.of(nums.get(i), nums.get(j)));
            }
        }
        return combinations;
    }
}
