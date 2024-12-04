package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay0102 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Test
    void getSimilarity() throws IOException {
        LOG.info("getSimilarity()");
        
        List<String> lines = IOUtils.readLines(TestDay0102.class.getResourceAsStream("/test01_list.txt"));
        
        Assertions.assertThat(lines).isNotEmpty();
        
        var l1 = new HashSet<Integer>();
        var l2 = new ArrayList<Integer>();
        
        for (String line : lines) {
            String[] split = line.split("[ ]+");
            l1.add(Integer.valueOf(split[0]));
            l2.add(Integer.valueOf(split[1]));
        }

        var map = new HashMap<Integer, Integer>();
        
        l2.stream()
            .forEach(i -> {
                if (map.containsKey(i)) {
                    map.put(i, map.get(i)+1);
                } else {
                    map.put(i, 1);
                }
            });
        
        Integer similarity = l1.stream()
            .map(i -> {
                if (map.containsKey(i)) {
                    return i * map.get(i);
                }
                return 0;
                })
            .reduce(0, (a, b) -> a + b);
        
        
        Assertions.assertThat(similarity).isPositive();
        
        LOG.info("Similarity: {}", similarity);
    }
    

}
