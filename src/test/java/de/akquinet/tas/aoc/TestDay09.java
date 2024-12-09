package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay09 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<Integer> disc = new ArrayList<>();

    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day09/day09_structure_test.txt"));

        int id = -1;
        int place = 0;
        boolean isFile = false;
        for (int i = 0; i < lines.get(0).toCharArray().length; i++) {
            isFile = !isFile;
            
            // char -> int
            int integer = (int) lines.get(0).toCharArray()[i] - 48;

            if (integer <= 0) {
                continue;
            }

            if (isFile) {
                id = id + 1;
                place = id;
            } else {
                place = -1;
            }
            
            for (int j = 1; j <= integer; j++) {
                disc.add(place);
            }

        }

    }

    @Test
    void getPart1Count()
    {
        LOG.info("getPart1Count()");

        int sum = 0;

        // defragmentieren
        List<Integer> l = defragment(disc);
        
        // berechne Checksum
        
        LOG.info("count: {}", sum);

        Assertions.assertThat(sum).isEqualTo(269);
    }

    private List<Integer> defragment(List<Integer> disc2) {
        // TODO Auto-generated method stub
        return null;
    }

}
