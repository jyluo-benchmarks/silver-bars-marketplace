package com.silverbars.domain;

import java.util.List;
import java.util.Objects;




final public class OrderSummary {

    public static final double ZEROVAL = 0.0 ;

    private static final double Zero_Kg = ZEROVAL;

    private final PricePerKg pricePerKg;
    private final Order.Type orderType;
    private final double quantity;

    public OrderSummary(double quantity, PricePerKg pricePerKg, Order.Type orderType) {
        this.quantity = quantity;
        this.orderType = orderType;
        this.pricePerKg = pricePerKg;
    }

    public OrderSummary(Bid bid, List<Double> quantities) {
        this(total(quantities), bid.pricePerKg(), bid.orderType());
    }

    private static double mySum(double acc, double q) {
        double result = acc + q;
        return result;
    }

    private static double total(List<Double> quantities) {
        return quantities.stream().reduce(Zero_Kg, (acc, q) -> mySum(acc, q));
    }

    public Order.Type orderType() {
        return this.orderType;
    }

    public PricePerKg pricePerKg() {
        return this.pricePerKg;
    }

    public double quantity() {
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
