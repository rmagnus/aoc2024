package de.akquinet.tas.aoc;

import java.io.IOException;
import java.lang.invoke.MethodHandles;
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

class TestDay0101 {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test
    void getDiffSum() throws IOException {
        LOG.info("getDiffSum()");

        List<String> lines = IOUtils.readLines(TestDay0101.class.getResourceAsStream("/test01_list.txt"));

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

    public static <L, R, T> Stream<T> zip(Stream<L> leftStream, Stream<R> rightStream, BiFunction<L, R, T> combiner) {
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
