package com.silverbars.support;

import com.silverbars.domain.Order;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;

import static org.joda.money.CurrencyUnit.GBP;

public class DSL {

    public static Order buy(double quantity, PricePerKg price, UserId userId) {
        return new Order(userId, quantity, price, Order.Type.Buy);
    }

    public static Order sell(double quantity, PricePerKg price, UserId userId) {
        return new Order(userId, quantity, price, Order.Type.Sell);
    }

    public static double kg(double value) {
        return value;
    }

    public static PricePerKg £(double amount) {
        return PricePerKg.of(GBP, amount);
    }
}
