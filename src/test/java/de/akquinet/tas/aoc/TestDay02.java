package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay02 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Test
    void getPart1SafeReports() throws IOException {
        LOG.info("getPart1SafeReports()");
        
        List<String> lines = IOUtils.readLines(TestDay02.class.getResourceAsStream("/day02_list.txt"));
        
        Assertions.assertThat(lines).isNotEmpty();
        
        long count = lines.stream()
            .filter(l -> checkSaveReport(l))
            .count();
            
        LOG.info("Safe reports: {}", count);
        
        Assertions.assertThat(count).isEqualTo(549);
    }

    @Test
    void getPart2SafeReports() throws IOException {
        LOG.info("getPart2SafeReports()");
        
        List<String> lines = IOUtils.readLines(TestDay02.class.getResourceAsStream("/day02_list.txt"));
        
        Assertions.assertThat(lines).isNotEmpty();
        
        long count = lines.stream()
            .filter(l -> checkSaveReportWithDampener(l))
            .count();
            
        LOG.info("Safe reports: {}", count);
        
        Assertions.assertThat(count).isEqualTo(589);
    }

    private boolean checkSaveReport(String line) 
    {
        List<Integer> list = Arrays.stream(line.split(" "))
            .map(s -> Integer.valueOf(s))
            .toList();
        
        return checkStetigkeit(list) && checkDifference(list);
    }

    private boolean checkSaveReportWithDampener(String line) 
    {
        List<Integer> list = Arrays.stream(line.split(" "))
            .map(s -> Integer.valueOf(s))
            .toList();
        
        if (checkStetigkeit(list) && checkDifference(list)) { 
            return true;
        }

        for (int i = 0; i < list.size(); i++) {
            var l = new ArrayList<Integer>(list);
            l.remove(i);
            
            if (checkStetigkeit(l) && (checkDifference(l))) {
                return true;
            }
        }
        
        return false;
    }

    private boolean checkDifference(List<Integer> list)
    {
        return IntStream.range(0, list.size() - 1)
                .allMatch(i -> {
                    int diff = Math.abs(list.get(i) - list.get(i + 1));
                    // größer 0 wird schon durch die Stetigkeit geprüft
                    return (diff < 4);
                });
    }

    private boolean checkStetigkeit(List<Integer> list) 
    {
        boolean match = IntStream.range(0, list.size() - 1)
            .allMatch(i -> list.get(i) > list.get(i + 1));
        
        if (match) { return true; }
        
        return IntStream.range(0, list.size() - 1)
                .allMatch(i -> list.get(i) < list.get(i + 1));
    }
        
}
