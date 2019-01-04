package com.silverbars.support;

import com.silverbars.domain.Order;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;

import units.qual.kg;

import static org.joda.money.CurrencyUnit.GBP;

public class DSL {

    public static Order buy(@kg int quantity, PricePerKg price, UserId userId) {
        return new Order(userId, quantity, price, Order.Type.Buy);
    }

    public static Order sell(@kg int quantity, PricePerKg price, UserId userId) {
        return new Order(userId, quantity, price, Order.Type.Sell);
    }

    public static @kg int kg(Number value) {
        return value.byteValue();
    }

    public static PricePerKg £(double amount) {
        return PricePerKg.of(GBP, amount);
    }
}
