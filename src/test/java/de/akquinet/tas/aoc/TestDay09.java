package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay09 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<Integer> disc = new ArrayList<>();
    int maxId = 0;

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
        maxId = id;

    }

    @Test
    void getPart1Sum()
    {
        LOG.info("getPart1Sum()");

        // defragmentieren
        List<Integer> list = defragmentBlock(new ArrayList<Integer>(disc));
        
        // berechne Checksum
        long sum = getChecksum(list);
        
        LOG.info("sum: {}", sum);

        Assertions.assertThat(sum).isEqualTo(1928);
//        Assertions.assertThat(sum).isEqualTo(6430446922192l);
    }

    @Test
    void getPart2Sum()
    {
        LOG.info("getPart2Sum()");

        // defragmentieren
        List<Integer> list = defragmentFile(new ArrayList<Integer>(disc));
        
        // berechne Checksum
        long sum = getChecksum(list);
        
        LOG.info("count: {}", sum);

        Assertions.assertThat(sum).isEqualTo(6430446922192l);
    }

    private long getChecksum(List<Integer> list) {
        long l = 0;
        for (int pos = 0; pos < list.size(); pos++) {
            Integer id = list.get(pos);
            if (id < 0) { return l; }
            l = l + pos * id;
        }
        return l;
    }

    private List<Integer> defragmentBlock(List<Integer> disc)
    {
        
        int l1 = 0;
        int l2 = disc.size() - 1;
        
        while (l1 < l2) {
            l1 = getFirstFreeIndex(disc);
            l2 = getLastUsed(disc);
            if (l1 < l2) {
                disc.set(l1, disc.get(l2));
                disc.set(l2, -1);
            }
        }

        return disc;
    }

    private List<Integer> defragmentFile(List<Integer> disc)
    {
        // pro Id absteigend
        for (int id = maxId; id >=0; id--) {
            // wie groß ist der Fileblock
            List<Integer> indizies = getPositionForId(disc, id);
            int blockSizeId = indizies.size();
                        
            // haben wir einen freien Block dieser Größe
            Optional<List<Integer>> block = getFreeBlock(disc, blockSizeId);

            // verschieben

            // ansonsten nächste Id
        }

        
        int l1 = 0;
        int l2 = disc.size() - 1;
        
        while (l1 < l2) {
            l1 = getFirstFreeIndex(disc);
            l2 = getLastUsed(disc);
            if (l1 < l2) {
                disc.set(l1, disc.get(l2));
                disc.set(l2, -1);
            }
        }

        return disc;
    }

    private Optional<List<Integer>> getFreeBlock(List<Integer> disc, int blockSizeId) {

        return Optional.empty();
    }

    private List<Integer> getPositionForId(List<Integer> disc, int i) {
        List<Integer> indizies = new ArrayList<>();
        for (int j = 0; j < disc.size()-1; j++) {
            if (i == disc.get(j)) {
                indizies.add(j);
            }
        }
        return indizies;
    }

    private int getLastUsed(List<Integer> disc) {
        for (int i = disc.size() -1 ; i >= 0; i--) {
            if (disc.get(i) >= 0) { return i; }
        }
        return 0;
    }

    private int getFirstFreeIndex(List<Integer> disc) {
        return disc.indexOf(-1);
    }

}
