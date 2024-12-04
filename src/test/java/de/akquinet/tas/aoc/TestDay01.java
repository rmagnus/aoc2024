package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.commons.collections4.bag.TreeBag;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class TestDay01 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    void getPart1DiffSum() throws IOException {
        LOG.info("getPart1DiffSum()");

        List<String> lines = IOUtils.readLines(TestDay01.class.getResourceAsStream("/test01_list.txt"));

        Assertions.assertThat(lines).isNotEmpty();

        var b1 = new TreeBag<Integer>();
        var b2 = new TreeBag<Integer>();
        for (String line : lines) {
            String[] split = line.split("[ ]+");
            b1.add(Integer.valueOf(split[0]));
            b2.add(Integer.valueOf(split[1]));
        }

        Assertions.assertThat(b1).hasSameSizeAs(b2);

        Stream<Integer> zip = zip(b1.stream(), b2.stream(), (i1, i2) -> Math.abs(i2-i1));

        Integer distance = zip.reduce(0, (a, b) -> a + b);

        Assertions.assertThat(distance).isPositive();

        LOG.info("Distance: {}", distance);
    }

    @Test
    void getPart2Similarity() throws IOException {
        LOG.info("getPart2Similarity()");
        
        List<String> lines = IOUtils.readLines(TestDay01.class.getResourceAsStream("/test01_list.txt"));
        
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

    private <L, R, T> Stream<T> zip(Stream<L> leftStream, Stream<R> rightStream, BiFunction<L, R, T> combiner) {
        Spliterator<L> lefts = leftStream.spliterator();
        Spliterator<R> rights = rightStream.spliterator();
        return StreamSupport.stream(new AbstractSpliterator<T>(Long.min(lefts.estimateSize(), rights.estimateSize()), lefts.characteristics() & rights.characteristics()) {
            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return lefts.tryAdvance(left->rights.tryAdvance(right->action.accept(combiner.apply(left, right))));
            }
        }, leftStream.isParallel() || rightStream.isParallel());
    }

}
