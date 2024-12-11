package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
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
        
        for (int i = 1; i <= blinks; i++) {
            LOG.debug("blink: {} stones: {}", i, stones);
            stones = blink(stones);
        }

        int size = stones.size();

        LOG.info("size: {}", size);

        Assertions.assertThat(size).isEqualTo(183484);
    }

    private List<Long> blink(List<Long> list) {
        List<Long> newStones = new ArrayList<>();
        
        for (Long stone : list) {
            if (stone == 0) {
                newStones.add(1l);
            } else if (evenDigitnumber(stone)) {
                String string = String.valueOf(stone);
                newStones.add(Long.valueOf(string.substring(0, string.length() / 2)));
                newStones.add(Long.valueOf(string.substring(string.length() / 2)));
            } else {
                newStones.add(stone * 2024);
            }
        }
        
        return newStones;
        
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
