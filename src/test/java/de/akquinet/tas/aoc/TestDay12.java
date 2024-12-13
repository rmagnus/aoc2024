package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

class TestDay12 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
   
    char[][] array;
    int heigth, width;

    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay12.class.getResourceAsStream("/day12/day12_map_test.txt"));

        width = lines.get(0).length();
        heigth = lines.size();

        array = new char[heigth][width];

        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            array[i] = line.toCharArray();
        }
    }

    @Test
    void getPart1Price()
    {
        LOG.info("getPart1Price()");
        
        List<Region> regions = findRegions(array, (grid, visited, rows, cols) -> exploreRegion(grid, visited, rows, cols));
        
        Integer price = regions.stream()
            .map(r -> r.getPoints().size() * r.getPerimeter())
            .reduce(0, (a,b) -> a + b);
        
        LOG.info("price: {}", price);
        
        Assertions.assertThat(price).isEqualTo(1930);
    }

    @Test
    void getPart2Size()
    {
        LOG.info("getPart2Size()");

        List<Region> regions = findRegions(array, (grid, visited, rows, cols) -> exploreRegionWithLines(grid, visited, rows, cols));
        
        Integer price = regions.stream()
                .map(r -> r.getPoints().size() * r.getPerimeter())
                .reduce(0, (a,b) -> a + b);
        
        LOG.info("price: {}", price);
        
        Assertions.assertThat(price).isEqualTo(1206);

    }


    private List<Region> findRegions(char[][] grid, RegionFunction rf) {
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];
        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    // Start a new region
                    Region region = rf.getRegion(grid, visited, i, j);
                    regions.add(region);
                }
            }
        }

        return regions;
    }    
    
    private Region exploreRegion(char[][] grid, boolean[][] visited, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;
        char character = grid[startRow][startCol];

        // BFS or DFS stack to explore the region
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        Set<Coordinate> points = new HashSet<>();
        int perimeter = 0;

        // Directions: up, down, left, right
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int r = cell[0], c = cell[1];
            points.add(Coordinate.of(r, c));
            
            // Check neighbors
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != character) {
                    // Out of bounds or different character -> contributes to perimeter
                    perimeter++;
                } else if (!visited[nr][nc]) {
                    // Valid neighbor and not visited
                    visited[nr][nc] = true;
                    stack.push(new int[]{nr, nc});
                }
            }
        }
         
        return new Region(character, points, perimeter);
    }
        
    private Region exploreRegionWithLines(char[][] grid, boolean[][] visited, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;
        char character = grid[startRow][startCol];

        // DFS stack to explore the region
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        Set<Coordinate> points = new HashSet<>();
        int corners = 0;
        
        // Directions: up, down, left, right
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int r = cell[0], c = cell[1];
            points.add(Coordinate.of(r, c));
            corners = corners + getNrOfCornes(character, grid, r, c);
            
            // Check neighbors
            for (int d = 0; d < 4; d++) {
                int nr = r + dr[d];
                int nc = c + dc[d];

                if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != character) {
                    // Out of bounds or different character -> contributes to perimeter
                    
                } else if (!visited[nr][nc]) {
                    // Valid neighbor and not visited
                    visited[nr][nc] = true;
                    stack.push(new int[]{nr, nc});
                }
            }
            
        }
        
        LOG.info("Region: {} size: {} corners: {}", character, points.size(), corners);
        
        return new Region(character, points, corners);
    }
    

    private int getNrOfCornes(char c, char[][] grid, int x, int y) {
        
        
        int corners = getCorners(c, grid, x, y);
        
        LOG.info("c: {} x: {} y: {} : corners: {}", c, x + 1, y + 1, corners);
        
        if (corners > 1) { return corners; }
        
        if (corners == 0) { return checkInsideCorner(c, grid, x, y); }
        

        return 0;
    }


    private int checkInsideCorner(char c, char[][] grid, int x, int y) {
        int rows = grid.length;
        int cols = grid[0].length;
        int[] dr = { -1, -1, 1, 1 };
        int[] dc = { 1, -1, -1, 1 };
        int cCount = 0;
        for (int d = 0; d < 4; d++) {
            int nr = x + dr[d];
            int nc = y + dc[d];

            if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != c) {
                cCount = cCount +1;
            }
        }
        return cCount;
    }

    private int getCorners(char c, char[][] grid, int x, int y)
    {
        int[] dr = {-1, 0, 1, 0};
        int[] dc = {0, 1, 0, -1};
        int cCount = 0;
        for (int d = 0; d < 4; d++) {
            if (isFree(c, grid, x, y, dr, dc, d) && isFree(c, grid, x, y, dr, dc, (d +1) % 4)) {
                cCount = cCount +1;
            }
        }
        return cCount;
    }

    private boolean isFree(char c, char[][] grid, int x, int y, int[] dr, int[] dc, int d) {
        int rows = grid.length;
        int cols = grid[0].length;
        int nr = x + dr[d];
        int nc = y + dc[d];
        
        LOG.debug("check field x: {} y: {}", nr + 1, nc + 1);

        if (nr < 0 || nr >= rows || nc < 0 || nc >= cols || grid[nr][nc] != c) {
            return true;
        }
        return false;
    }


    @Getter
    static class Region {
        char character; // The character of this region
        Set<Coordinate> points = new HashSet<>();
        int perimeter; // Perimeter of this region

        Region(char character, Set<Coordinate> points, int perimeter) {
            this.character = character;
            this.points = points;
            this.perimeter = perimeter;
        }

    }
    
    @FunctionalInterface
    interface RegionFunction {
        Region getRegion(char[][] grid, boolean[][] visited, int startRow, int startCol);
    }

 }
