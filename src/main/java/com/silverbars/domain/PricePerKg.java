package com.silverbars.domain;

import org.joda.money.CurrencyUnit;
import org.joda.money.Money;

import java.util.Objects;

final public class PricePerKg implements Comparable<PricePerKg> {

    private final Money amount;

    public static PricePerKg of(CurrencyUnit currency, double amount) {
        return new PricePerKg(Money.of(currency, amount));
    }

    public PricePerKg(Money amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PricePerKg{" +
                "amount=" + amount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PricePerKg that = (PricePerKg) o;
        return Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }

    @Override
    public int compareTo(PricePerKg o) {
        return amount.compareTo(o.amount);
    }
}
