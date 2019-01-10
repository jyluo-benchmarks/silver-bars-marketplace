package com.silverbars.domain;

import java.util.List;
import java.util.Objects;

import units.UnitsTools;
import units.qual.*;

final public class OrderSummary {

    public static final @kg double ZEROVAL = 0.0 * UnitsTools.kg;

    private static final @kg double Zero_Kg = ZEROVAL;

    private final PricePerKg pricePerKg;
    private final Order.Type orderType;
    private final @kg double quantity;

    public OrderSummary(@kg double quantity, PricePerKg pricePerKg, Order.Type orderType) {
        this.quantity = quantity;
        this.orderType = orderType;
        this.pricePerKg = pricePerKg;
    }

    public OrderSummary(Bid bid, List<@kg Double> quantities) {
        this(total(quantities), bid.pricePerKg(), bid.orderType());
    }

    private static @kg double mySum(@kg double acc, @kg double q) {
        @kg double result = acc + q;
        return result;
    }

    private static @kg double total(List<@kg Double> quantities) {
        return quantities.stream().reduce(Zero_Kg, (acc, q) -> mySum(acc, q));
    }

    public Order.Type orderType() {
        return this.orderType;
    }

    public PricePerKg pricePerKg() {
        return this.pricePerKg;
    }

    public @kg double quantity() {
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
