package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;
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
        
        int price = machines.stream()
            .map(m -> getPossibleSolution(m))
            .filter(s -> s.isPresent())
            .map(s -> 3 * s.get().getX() + s.get().getY())
            .reduce(0, (a,b) -> a+ b);

        LOG.info("Price: {}", price);
        
        Assertions.assertThat(price).isEqualTo(27105);
    }


    private Optional<Coordinate> getPossibleSolution(ClawMachine m)
    {
        
        int a1 = m.getVectorA().getX(), b1 = m.getVectorB().getX(), n1 = m.getPrice().getX();
        int a2 = m.getVectorA().getY(), b2 = m.getVectorB().getY(), n2 = m.getPrice().getY();

        // Calculate determinant of A
        int detA = a1 * b2 - a2 * b1;

        if (detA == 0) {
            return Optional.empty();
        } else {
            // Determinants for x and y
            int detAx = n1 * b2 - n2 * b1;
            int detAy = a1 * n2 - a2 * n1;

            // Solve for x and y
            int x = detAx / detA;
            int y = detAy / detA;
            
            // cross check
            if ((x*a1+y*b1 == n1) && (x*a2+y*b2 == n2)) {
                return Optional.of(Coordinate.of(x, y));
            }

        }

        return Optional.empty();

    }


    @Getter
    @ToString
    class ClawMachine {
        Coordinate vectorA;
        Coordinate vectorB;
        Coordinate price;
        
        public ClawMachine(List<String> conf)
        {
            vectorA = getVector(conf.get(0));
            vectorB = getVector(conf.get(1));
            price = getPrice(conf.get(2));
        }

        private Coordinate getPrice(String string) {
            String[] split = string.split(" ");
            String[] split2 = split[1].split("=");
            Integer x = Integer.valueOf(split2[1].substring(0, split2[1].indexOf(',')));
            String[] split3 = split[2].split("=");
            Integer y = Integer.valueOf(split3[1]);

            return Coordinate.of(x, y);
        }

        private Coordinate getVector(String string) {
            
            String[] split = string.split(" ");
            String[] split2 = split[2].split("\\+");
            Integer x = Integer.valueOf(split2[1].substring(0, split2[1].indexOf(',')));
            String[] split3 = split[3].split("\\+");
            Integer y = Integer.valueOf(split3[1]);

            return Coordinate.of(x, y);
        }
    }
}
