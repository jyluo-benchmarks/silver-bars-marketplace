package com.silverbars.support;

import com.silverbars.domain.Order;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;
import tec.uom.se.quantity.Quantities;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

import static org.joda.money.CurrencyUnit.GBP;
import static tec.uom.se.unit.Units.KILOGRAM;

public class DSL {

    public static Order buy(Quantity<Mass> quantity, PricePerKg price, UserId userId) {
        return new Order(userId, quantity, price, Order.Type.Buy);
    }

    public static Order sell(Quantity<Mass> quantity, PricePerKg price, UserId userId) {
        return new Order(userId, quantity, price, Order.Type.Sell);
    }

    public static Quantity<Mass> kg(Number value) {
        return Quantities.getQuantity(value, KILOGRAM);
    }

    public static PricePerKg £(double amount) {
        return PricePerKg.of(GBP, amount);
    }
}
