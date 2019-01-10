package com.silverbars.domain;

import org.joda.money.Money;
import org.junit.Test;




import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.joda.money.CurrencyUnit.GBP;

public class OrderTest {

    @Test
    public void should_be_compared_by_value() throws Exception {

        Order firstOrder = new Order(
                new UserId("some id"),
                3.5,
                new PricePerKg(Money.of(GBP, 306)),
                Order.Type.Buy
        );

        Order secondOrder = new Order(
                new UserId("some id"),
                3.5,
                new PricePerKg(Money.of(GBP, 306)),
                Order.Type.Buy
        );

        assertThat(firstOrder).isEqualTo(secondOrder);
    }
}
