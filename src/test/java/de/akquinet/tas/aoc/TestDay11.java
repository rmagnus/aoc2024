package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay11 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    private Map<Long, Long> stones = new HashMap<>();


    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day11/day11_stones.txt"));
        String[] split = lines.get(0).split(" ");
        Arrays.stream(split)
            .map(s -> Long.valueOf(s))
            .forEach(l -> stones.merge(l , 1l, Long::sum));
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

    private long getNumberOfStones(int blinks) 
    {
        Map<Long, Long> map = new HashMap<>(stones);
        
        for (int i = 1; i <= blinks; i++) {
            map = blink(map);
            LOG.info("blink: {}, numer of distinct stones: {}", i, map.size());
        }
        
        Long count =  map.values().stream()
            .reduce(0l, (a, b) -> a + b);
        
        LOG.info("count: {}", count);
        
        return count;
    }

    private Map<Long, Long> blink(Map<Long, Long> stones) {

        Map<Long, Long> map = new ConcurrentHashMap<>();

        stones.entrySet().parallelStream().forEach(entry -> {
            for (int j = 0; j < entry.getValue(); j++) {
                Long key = entry.getKey();
                String str = key.toString();

                if (key == 0) {
                    updateOccurences(map, 1l);
                } else if (evenDigitnumber(str)) {
                    int half = str.length() / 2;
                    updateOccurences(map, Long.parseLong(str.substring(0, half)));
                    updateOccurences(map, Long.parseLong(str.substring(half)));

                } else {
                    updateOccurences(map, key * 2024);
                }

            }

        });

        return map;
    }

    private void updateOccurences(Map<Long, Long> stones, long stone) {
            Long l = stones.get(stone);
            Long newValue = (l == null) ? 1 : l + 1;
            stones.put(stone, newValue);
    }

    private boolean evenDigitnumber(String str) {
        return (str.length() % 2 == 0);
    }

 }
