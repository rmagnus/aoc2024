package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
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
        List<String> lines = IOUtils.readLines(TestDay12.class.getResourceAsStream("/day12/day12_map.txt"));

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
        
        List<Region> regions = findRegions(array);
        
        Integer price = regions.stream()
            .map(r -> r.getSize() * r.getPerimeter())
            .reduce(0, (a,b) -> a + b);
        
        LOG.info("price: {}", price);
        
        Assertions.assertThat(price).isEqualTo(1461806);
    }

    @Test
    void getPart2Size()
    {
        LOG.info("getPart2Size()");
        
    }

    public static List<Region> findRegions(char[][] grid) {
        int rows = grid.length;
        int cols = grid[0].length;

        boolean[][] visited = new boolean[rows][cols];
        List<Region> regions = new ArrayList<>();

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (!visited[i][j]) {
                    // Start a new region
                    Region region = exploreRegion(grid, visited, i, j);
                    regions.add(region);
                }
            }
        }

        return regions;
    }    

    private static Region exploreRegion(char[][] grid, boolean[][] visited, int startRow, int startCol) {
        int rows = grid.length;
        int cols = grid[0].length;
        char character = grid[startRow][startCol];

        // BFS or DFS stack to explore the region
        Stack<int[]> stack = new Stack<>();
        stack.push(new int[]{startRow, startCol});
        visited[startRow][startCol] = true;

        int size = 0;
        int perimeter = 0;

        // Directions: up, down, left, right
        int[] dr = {-1, 1, 0, 0};
        int[] dc = {0, 0, -1, 1};

        while (!stack.isEmpty()) {
            int[] cell = stack.pop();
            int r = cell[0], c = cell[1];
            size++;

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

        return new Region(character, size, perimeter);
    }
    

    @Getter
    static class Region {
        char character; // The character of this region
        int size; // Number of cells in this region
        int perimeter; // Perimeter of this region

        Region(char character, int size, int perimeter) {
            this.character = character;
            this.size = size;
            this.perimeter = perimeter;
        }

        @Override
        public String toString() {
            return String.format("Character: %c, Size: %d, Perimeter: %d", character, size, perimeter);
        }
    }

 }
