package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay11 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private List<Long> stones;


    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day11/day11_stones.txt"));
        String[] split = lines.get(0).split(" ");
        stones = Arrays.stream(split)
            .map(s -> Long.valueOf(s))
            .toList();
    }

    @Test
    void getPart1Size()
    {
        LOG.info("getPart1Size()");
        
        int blinks = 25;
        
        long size = getNumberOfStones(blinks);

        Assertions.assertThat(size).isEqualTo(183484);
    }

    @Test
    void getPart2Size()
    {
        LOG.info("getPart2Size()");
        
        int blinks = 75;
        
        long size = getNumberOfStones(blinks);

        Assertions.assertThat(size).isEqualTo(183484);
    }

    private long getNumberOfStones(int blinks) {
        
        Long size = stones.parallelStream()
            .map(s -> blink(s, blinks))
            .reduce(0l, (a,b) -> a + b);

        LOG.info("size: {}", size);
        return size;
    }

    private long blink(Long stone, int level) 
    {
        if (level == 0) {
            return 1;
            
        }

        if (stone == 0) {
                return blink(1l, level - 1);
            } else if (evenDigitnumber(stone)) {
                
                String string = String.valueOf(stone);
                return blink(Long.valueOf(string.substring(0, string.length() / 2)), level - 1) + 
                        blink(Long.valueOf(string.substring(string.length() / 2)), level - 1);
            } else {
                return blink(stone * 2024, level - 1);
            }
    }

    private boolean evenDigitnumber(Long stone) {
        return (String.valueOf(stone).length() % 2 == 0);
    }

    @Test
    void getPart2Count()
    {
        LOG.info("getPart2Count()");
        
    }

 }
