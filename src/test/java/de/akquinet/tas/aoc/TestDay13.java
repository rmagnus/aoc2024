package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

class TestDay13 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    List<ClawMachine> machines = new ArrayList<>();
    
    @BeforeEach
    public void beforeEach() throws IOException
    {
        List<String> lines = IOUtils.readLines(TestDay05.class.getResourceAsStream("/day13/day13.txt"));
        
        List<String> conf = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            if (StringUtils.isBlank(line)) {
                machines.add(new ClawMachine(conf));
                conf.clear();
            } else {
                conf.add(line);
            }
        }
        machines.add(new ClawMachine(conf));
    }

    
    @Test
    void getPart1Price() {
        LOG.info("getPart1Price()");
        
        long price = machines.stream()
            .map(m -> getPossibleSolution(m))
            .filter(s -> s.isPresent())
            .map(s -> 3 * s.get().getLeft() + s.get().getRight())
            .reduce(0l, (a,b) -> a + b);

        LOG.info("Price: {}", price);
        
        Assertions.assertThat(price).isEqualTo(27105);
    }

    @Test
    void getPart2Price() {
        LOG.info("getPart2Price()");
        
        long price = machines.stream()
            .map(m -> {
                Pair<Long, Long> p = m.getPrice();
                m.setPrice(Pair.of(p.getLeft() + 10000000000000l, p.getRight() + 10000000000000l));
                return m;
                })
            .map(m -> getPossibleSolution(m))
            .filter(s -> s.isPresent())
            .map(s -> 3 * s.get().getLeft() + s.get().getRight())
            .reduce(0l, (a,b) -> a + b);

        LOG.info("Price: {}", price);
        
        Assertions.assertThat(price).isEqualTo(101726882250942l);
    }


    // Lösung nach der Determinanten Methode (Cramer's Regel)
    private Optional<Pair<Long, Long>> getPossibleSolution(ClawMachine m)
    {
        
        long a1 = m.getVectorA().getLeft(), b1 = m.getVectorB().getLeft(), n1 = m.getPrice().getLeft();
        long a2 = m.getVectorA().getRight(), b2 = m.getVectorB().getRight(), n2 = m.getPrice().getRight();

        // Calculate determinant of A
        long detA = a1 * b2 - a2 * b1;

        if (detA == 0) {
            return Optional.empty();
        } else {
            // Determinants for x and y
            long detAx = n1 * b2 - n2 * b1;
            long detAy = a1 * n2 - a2 * n1;

            // Solve for x and y
            long x = detAx / detA;
            long y = detAy / detA;
            
            // cross check
            if ((x*a1+y*b1 == n1) && (x*a2+y*b2 == n2)) {
                return Optional.of(Pair.of(x, y));
            }

        }

        return Optional.empty();

    }


    @Getter
    @Setter
    @ToString
    class ClawMachine {
        Pair<Integer, Integer> vectorA;
        Pair<Integer, Integer> vectorB;
        Pair<Long, Long> price;
        
        public ClawMachine(List<String> conf)
        {
            vectorA = getVector(conf.get(0));
            vectorB = getVector(conf.get(1));
            price = getPrice(conf.get(2));
        }

        private Pair<Long, Long> getPrice(String string) {
            String[] split = string.split(" ");
            String[] split2 = split[1].split("=");
            Long x = Long.valueOf(split2[1].substring(0, split2[1].indexOf(',')));
            String[] split3 = split[2].split("=");
            Long y = Long.valueOf(split3[1]);

            return Pair.of(x, y);
        }

        private Pair<Integer, Integer> getVector(String string) {
            
            String[] split = string.split(" ");
            String[] split2 = split[2].split("\\+");
            Integer x = Integer.valueOf(split2[1].substring(0, split2[1].indexOf(',')));
            String[] split3 = split[3].split("\\+");
            Integer y = Integer.valueOf(split3[1]);

            return Pair.of(x, y);
        }
    }
}
