package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay07 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<Pair<Long, List<Long>>> list = new ArrayList<>();

    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day07/day07_list.txt"));

        list = lines.stream()
            .map(l -> {
                String[] split = l.split(":");
                return Pair.of(Long.valueOf(split[0].trim()), 
                            Arrays.asList(split[1].trim().split(" "))
                                .stream()
                                .map(s -> Long.valueOf(s))
                                .toList());
            })
            .toList();
    }

    @Test
    void getPart1Sum()
    {
        LOG.info("getPart1Sum()");

        long sum = list.stream()
            .filter(p -> existsValidCombinationsTwo(p.getRight()).contains(p.getLeft()))
            .map(p -> Long.valueOf(p.getLeft()))
            .reduce(0L, (a,b) -> a+b);
        
        LOG.info("sum: {}", sum);

        Assertions.assertThat(sum).isEqualTo(303766880536l);
    }

    @Test
    void getPart2Sum()
    {
        LOG.info("getPart2Sum()");

        long sum = list.stream()
            .filter(p -> existsValidCombinationsThree(p.getRight()).contains(p.getLeft()))
            .map(p -> Long.valueOf(p.getLeft()))
            .reduce(0L, (a,b) -> a+b);
        
        LOG.info("sum: {}", sum);

        Assertions.assertThat(sum).isEqualTo(337041851384440l);
    }

    private List<Long> existsValidCombinationsTwo(List<Long> nums) {
        List<Long> results = new ArrayList<>();
        if (nums == null || nums.isEmpty()) {
            return results;
        }
        // Start the recursive process
        generateCombinationsHelperTwo(nums, 1, nums.get(0), results);
        return results;
    }

    private List<Long> existsValidCombinationsThree(List<Long> nums) {
        List<Long> results = new ArrayList<>();
        if (nums == null || nums.isEmpty()) {
            return results;
        }
        // Start the recursive process
        generateCombinationsHelperThree(nums, 1, nums.get(0), results);
        return results;
    }

    private void generateCombinationsHelperTwo(List<Long> nums, int index, long current, List<Long> results) {
        // Base case: If we've reached the end of the array, add the current result
        if (index == nums.size()) {
            results.add(current);
            return;
        }

        // Recursive case: Apply + and - to the current element with the next element
        generateCombinationsHelperTwo(nums, index + 1, current + nums.get(index), results);
        generateCombinationsHelperTwo(nums, index + 1, current * nums.get(index), results);
    }

    private void generateCombinationsHelperThree(List<Long> nums, int index, long current, List<Long> results) {
        // Base case: If we've reached the end of the array, add the current result
        if (index == nums.size()) {
            results.add(current);
            return;
        }

        // Recursive case: Apply + and - to the current element with the next element
        generateCombinationsHelperThree(nums, index + 1, current + nums.get(index), results);
        generateCombinationsHelperThree(nums, index + 1, Long.valueOf("" + current + nums.get(index)), results);
        generateCombinationsHelperThree(nums, index + 1, current * nums.get(index), results);
    }

}
