package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

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
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day09/day09_structure.txt"));

        int id = -1;
        int place = 0;
        for (int i = 0; i < lines.get(0).toCharArray().length; i++) {
            
            // char -> int
            int integer = (int) lines.get(0).toCharArray()[i] - 48;

            if (integer <= 0) {
                continue;
            }

            if (i % 2 == 0) {
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

//        Assertions.assertThat(sum).isEqualTo(1928);
        Assertions.assertThat(sum).isEqualTo(6430446922192l);
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

        Assertions.assertThat(sum).isEqualTo(6460170593016l);
    }

    private long getChecksum(List<Integer> list) {
        long l = 0;
        for (int pos = 0; pos < list.size(); pos++) {
            Integer id = list.get(pos);
            if (id < 0) { continue; }
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
                        
            // haben wir einen freien Block dieser Größe
            Optional<List<Integer>> freeBlock = getFreeBlock(disc, indizies.size());

            // und liegt er davor
            if (freeBlock.isPresent() && (freeBlock.get().getLast() < indizies.getFirst())) {
                // verschieben
                moveBlock(disc, id, indizies, freeBlock.get());    
            }

            // ansonsten nächste Id
        }

        
        return disc;
    }

    private void moveBlock(List<Integer> disc, int id, List<Integer> block, List<Integer> freeBlock)
    {
        IntStream.range(freeBlock.get(0), freeBlock.get(0) + block.size())
            .forEach(i -> disc.set(i, id));
        block.stream().forEach(i -> disc.set(i, -1));
    }

    private Optional<List<Integer>> getFreeBlock(List<Integer> numbers, int blockSizeId)
    {

        List<List<Integer>> result = new ArrayList<>();

        int i = 0;
        while (i < numbers.size()) {
            // If the current element is -1, start collecting a sublist
            if (numbers.get(i) == -1) {
                List<Integer> sublist = new ArrayList<>();
                while (i < numbers.size() && numbers.get(i) == -1) {
                    sublist.add(i);
                    i++;
                }
                result.add(sublist);
            } else {
                i++;
            }
        }

        return result.stream()
            .filter(l -> l.size() >= blockSizeId)
            .findFirst();
    }
    
    private List<Integer> getPositionForId(List<Integer> disc, int i) {
        List<Integer> indizies = new ArrayList<>();
        for (int j = 0; j < disc.size(); j++) {
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
