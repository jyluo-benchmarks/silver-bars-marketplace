package com.silverbars.domain;

import units.qual.kg;
import static units.UnitsTools.kg;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

final public class OrderSummary {

    public static final @kg int Zero_Kg = 0 * kg;
    // public static final @kg long Zero_Kg = 0L * kg;
    // public static final @kg float Zero_Kg = 0.0f * kg;
    // public static final @kg Number Zero_Kg = 0.0d * kg;
    // public static final @kg Number Zero_Kg = new AtomicInteger(0);
    // public static final @kg Number Zero_Kg = new AtomicLong(0);
    // for DecimalQuantity
    // public static final @kg Number Zero_Kg = BigInteger.valueOf(0);
    // public static final @kg Number Zero_Kg = new BigDecimal(0.0);

    private final PricePerKg pricePerKg;
    private final Order.Type orderType;
    private final @kg int quantity;

    public OrderSummary(@kg int quantity, PricePerKg pricePerKg, Order.Type orderType) {
        this.quantity = quantity;
        this.orderType = orderType;
        this.pricePerKg = pricePerKg;
    }

    public OrderSummary(Bid bid, List<@kg Integer> quantities) {
        this(total(quantities), bid.pricePerKg(), bid.orderType());
    }

    private static @kg int mySum(@kg int acc, @kg int q) {
        @kg int result = (acc + q);
        return result;
    }

    private static @kg int total(List<@kg Integer> quantities) {
        return quantities.stream().reduce(Zero_Kg, (acc, q) -> mySum(acc, q));
    }

    public Order.Type orderType() {
        return this.orderType;
    }

    public PricePerKg pricePerKg() {
        return this.pricePerKg;
    }

    public @kg int quantity() {
        return this.quantity;
    }

    @Override public String toString() {
        return "OrderSummary{" + "pricePerKg=" + pricePerKg + ", orderType=" + orderType
                + ", quantity=" + quantity + '}';
    }

    @Override public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderSummary that = (OrderSummary) o;
        return Objects.equals(pricePerKg, that.pricePerKg) && orderType == that.orderType
                && Objects.equals(quantity, that.quantity);
    }

    @Override public int hashCode() {
        return Objects.hash(pricePerKg, orderType, quantity);
    }
}
