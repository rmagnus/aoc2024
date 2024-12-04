package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay03 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    
    @Test
    void getPart1Mull() throws IOException {
        LOG.info("getPart1SafeReports()");
        
        List<String> lines = IOUtils.readLines(TestDay03.class.getResourceAsStream("/day03_list.txt"));
        
        Assertions.assertThat(lines).isNotEmpty();
        
        Pattern pattern = Pattern.compile("(mul\\(\\d+,\\d+\\))");
        
        final AtomicLong result = new AtomicLong();
        result.set(0);
        
        lines.stream().forEach(l -> {
            Matcher matcher = pattern.matcher(l);
            while (matcher.find()) {
                String group = matcher.group();
                String[] split = group.split("[(),]");
                result.addAndGet(Integer.valueOf(split[1]) * Integer.valueOf(split[2]));
            }
        });
        
        LOG.info("result: {}", result.get());
            
    }

    @Test
    void getPart2Mull() throws IOException {
        LOG.info("getPart1SafeReports()");
        
        List<String> lines = IOUtils.readLines(TestDay03.class.getResourceAsStream("/day03_list.txt"));
        
        Assertions.assertThat(lines).isNotEmpty();
        
        Pattern pattern = Pattern.compile("(mul\\(\\d+,\\d+\\))|(don\'t)|(do)");
        
        final AtomicLong result = new AtomicLong();
        result.set(0);
        final AtomicBoolean valid = new AtomicBoolean();
        valid.set(true);
        
        lines.stream().forEach(l -> {
            Matcher matcher = pattern.matcher(l);
            while (matcher.find()) {
                String group = matcher.group();
                
                if ("do".equals(group)) {
                    valid.set(true);
                    continue;
                }

                if ("don't".equals(group)) {
                    valid.set(false);
                    continue;
                }

                if (valid.get()) {
                    String[] split = group.split("[(),]");
                    result.addAndGet(Integer.valueOf(split[1]) * Integer.valueOf(split[2]));
                }
            }
        });
        
        LOG.info("result: {}", result.get());
            
    }

}
