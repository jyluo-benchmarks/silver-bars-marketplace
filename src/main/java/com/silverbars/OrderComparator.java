package com.silverbars;

import com.silverbars.domain.Order;
import com.silverbars.domain.OrderSummary;

import java.util.Comparator;
import static java.util.Comparator.comparing;

class OrderComparator implements Comparator<OrderSummary> {
    static private final Comparator<OrderSummary> byType            = comparing(OrderSummary::orderType);
    static private final Comparator<OrderSummary> byPriceAscending  = comparing(OrderSummary::pricePerKg);
    static private final Comparator<OrderSummary> byPriceDescending = byPriceAscending.reversed();

    @Override
    public int compare(OrderSummary left, OrderSummary right) {
        if (left.orderType() != right.orderType()) {
            return byType.compare(left, right);
        }

        // the types are identical, it's enough to check the type of one of the elements

        if (left.orderType() == Order.Type.Sell) {
            return byPriceAscending.compare(left, right);
        }

        if (left.orderType() == Order.Type.Buy) {
            return byPriceDescending.compare(left, right);
        }

        return 0;
    }
}
