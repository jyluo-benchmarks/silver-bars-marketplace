package com.silverbars.domain;

import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;
import java.util.List;
import java.util.Objects;

import static tec.uom.se.unit.Units.KILOGRAM;

final public class OrderSummary {

    static private final Quantity<Mass> Zero_Kg = Quantities.getQuantity(0.0, KILOGRAM);

    private final PricePerKg pricePerKg;
    private final Order.Type orderType;
    private final Quantity<Mass> quantity;

    public OrderSummary(Quantity<Mass> quantity, PricePerKg pricePerKg, Order.Type orderType) {
        this.quantity   = quantity;
        this.orderType  = orderType;
        this.pricePerKg = pricePerKg;
    }

    public OrderSummary(Bid bid, List<Quantity<Mass>> quantities) {
        this(total(quantities), bid.pricePerKg(), bid.orderType());
    }

    private static Quantity<Mass> total(List<Quantity<Mass>> quantities) {
        return quantities.stream().reduce(Zero_Kg, (acc, q) -> acc.add(q));
    }

    public Order.Type orderType() {
        return this.orderType;
    }

    public PricePerKg pricePerKg() {
        return this.pricePerKg;
    }

    public Quantity<Mass> quantity() {
        return this.quantity;
    }

    @Override
    public String toString() {
        return "OrderSummary{" +
                "pricePerKg=" + pricePerKg +
                ", orderType=" + orderType +
                ", quantity=" + quantity +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderSummary that = (OrderSummary) o;
        return Objects.equals(pricePerKg, that.pricePerKg) &&
                orderType == that.orderType &&
                Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pricePerKg, orderType, quantity);
    }
}
