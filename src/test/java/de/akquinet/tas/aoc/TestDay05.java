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

class TestDay05 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<Pair<String, String>> ruleList = new ArrayList<>();
    
    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> ruleLines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day05_rules.txt"));
        for (String rule : ruleLines) {
            String[] split = rule.split("\\|");
            Pair<String, String> pair = Pair.of(split[0], split[1]);
            ruleList.add(pair);
        }
    }

    @Test
    void getPart1XSearchValidMiddlePage() throws IOException {
        LOG.info("getPart1XSearchValidMiddlePage()");

        int result = 0;
        List<String> pageLines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day05_pages.txt"));

        result = pageLines.stream()
            .map(l -> Arrays.asList(l.split(",")))
            .filter(array -> checkAllRules(array))
            .map(list -> {
                int pos = (list.size() / 2);
                Integer integer = Integer.valueOf(list.get(pos));
                return integer;
                })
            .reduce(0, (a,b) -> a+b);
        
            
        LOG.info("result: {}", result);
        
        Assertions.assertThat(result).isPositive();
    }

    private boolean checkAllRules(List<String> array)
    {
        boolean b = true;
        for (Pair<String, String> rule : ruleList) {
            b = b && checkRule(array, rule);
            if (!b) { break; }
        }

        return b;
    }

    private boolean checkRule(List<String> pages, Pair<String, String> rule)
    {
        String left = rule.getLeft();
        String right = rule.getRight();
        
        if (!pages.contains(left) || !pages.contains(right)) {
            return true;
        }
        
        return pages.indexOf(left) < pages.indexOf(right);
    }


}
