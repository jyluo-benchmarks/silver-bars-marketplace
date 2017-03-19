package com.silverbars.domain;

import java.util.Objects;

final public class Bid {
    private final PricePerKg pricePerKg;
    private final Order.Type type;

    public static Bid forOrder(Order order) {
        return new Bid(order.pricePerKg(), order.type());
    }

    public Bid(PricePerKg pricePerKg, Order.Type type) {
        this.pricePerKg = pricePerKg;
        this.type = type;
    }

    public PricePerKg pricePerKg() {
        return pricePerKg;
    }

    public Order.Type orderType() {
        return type;
    }

    @Override
    public String toString() {
        return "Bid{" +
                "pricePerKg=" + pricePerKg +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bid bid = (Bid) o;
        return Objects.equals(pricePerKg, bid.pricePerKg) &&
                type == bid.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pricePerKg, type);
    }
}