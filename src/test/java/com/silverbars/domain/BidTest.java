package com.silverbars.domain;

import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.silverbars.support.DSL.*;
import static com.silverbars.support.Users.Alice;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.groupingBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class BidTest {

    @Test
    public void should_make_grouping_orders_easier() {

        List<Order> orders = asList(
                buy(kg(1.0), £(300), Alice),
                buy(kg(5.0), £(300), Alice)
        );

        Map<Bid, List<Order>> grouped = orders.stream().collect(groupingBy(Bid::forOrder));

        assertThat(grouped).isEqualTo(aMap(
            new Bid(£(300), Order.Type.Buy), orders
        ));
    }

    private static <K, V> Map<K, V> aMap(K key, V value) {
        return new HashMap<K, V>() {{
            put(key, value);
        }};
    }
}
