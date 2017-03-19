package com.silverbars.domain;

import org.joda.money.Money;
import org.junit.Test;
import tec.uom.se.quantity.Quantities;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joda.money.CurrencyUnit.GBP;
import static tec.uom.se.unit.Units.KILOGRAM;

public class OrderTest {

    @Test
    public void should_be_compared_by_value() throws Exception {

        Order firstOrder = new Order(
                new UserId("some id"),
                Quantities.getQuantity(3.5, KILOGRAM),
                new PricePerKg(Money.of(GBP, 306)),
                Order.Type.Buy
        );

        Order secondOrder = new Order(
                new UserId("some id"),
                Quantities.getQuantity(3.5, KILOGRAM),
                new PricePerKg(Money.of(GBP, 306)),
                Order.Type.Buy
        );

        assertThat(firstOrder).isEqualTo(secondOrder);
    }
}