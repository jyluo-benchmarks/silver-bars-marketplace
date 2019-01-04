package com.silverbars.domain;

import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import static tec.uom.se.unit.Units.KILOGRAM;

final public class OrderSummary {

    // public static final Number ZEROVAL = new Byte((byte) 0);
    // public static final Number ZEROVAL = new Short((short) 0);
    public static final Number ZEROVAL = new Integer(0);
    // public static final Number ZEROVAL = new Long(0);
    // public static final Number ZEROVAL = new Float(0.0);
    // public static final Number ZEROVAL = new Double(0.0);
    // public static final Number ZEROVAL = new AtomicInteger(0);
    // public static final Number ZEROVAL = new AtomicLong(0);
    // to exercise DecimalQuantity
    // static private final Number ZEROVAL = BigInteger.valueOf(0);
    // private static final Number ZEROVAL = new BigDecimal(0.0);

    private static final Quantity<Mass> Zero_Kg = Quantities.getQuantity(ZEROVAL, KILOGRAM);
    // static private final Quantity<Mass> Zero_Kg = Quantities.getQuantity(0.0,
    // KILOGRAM);

    static {
        System.out.println("zero constant quantity class: " + Zero_Kg.getClass());
        System.out.println("zero constant number class: " + Zero_Kg.getValue().getClass());
    }

    private final PricePerKg pricePerKg;
    private final Order.Type orderType;
    private final Quantity<Mass> quantity;

    public OrderSummary(Quantity<Mass> quantity, PricePerKg pricePerKg, Order.Type orderType) {
        this.quantity = quantity;
        this.orderType = orderType;
        this.pricePerKg = pricePerKg;
    }

    public OrderSummary(Bid bid, List<Quantity<Mass>> quantities) {
        this(total(quantities), bid.pricePerKg(), bid.orderType());
    }

    private static Quantity<Mass> mySum(Quantity<Mass> acc, Quantity<Mass> q) {
        Quantity<Mass> result = acc.add(q);
        // System.out.println("zero constant quantity class: " + acc.getClass());
        // System.out.println("zero constant number class: " +
        // acc.getValue().getClass());
        return result;
    }

    private static Quantity<Mass> total(List<Quantity<Mass>> quantities) {
        return quantities.stream().reduce(Zero_Kg, (acc, q) -> mySum(acc, q));
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
