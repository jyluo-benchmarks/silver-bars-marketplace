package com.silverbars;

import com.silverbars.domain.Order;
import com.silverbars.domain.OrderSummary;
import com.silverbars.domain.PricePerKg;
import org.junit.Before;
import org.junit.Test;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

import static com.silverbars.support.DSL.*;
import static com.silverbars.support.Users.Alice;
import static com.silverbars.support.Users.Bob;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

public class LiveOrderBoardTest {

    private LiveOrderBoard board;

    @Before
    public void setUp() throws Exception {
        board = new LiveOrderBoard();
    }

    @Test
    public void should_start_with_no_orders_displayed() {

        assertThat(board.summary()).isEmpty();
    }

    @Test
    public void should_display_a_registered_order() {

        board.register(buy(kg(3.5), £(306), Alice));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(3.5), £(306))
        );
    }

    @Test
    public void should_display_a_summary_of_those_registered_orders_which_type_and_bid_price_match() {

        board.register(buy(kg(3.5), £(306), Alice));
        board.register(buy(kg(2.5), £(306), Bob));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(6.0), £(306))
        );
    }


    @Test
    public void should_allow_to_cancel_a_registered_order_when_requested_so_by_the_user_who_placed_it() {

        board.register(buy(kg(3.5), £(306), Alice));
        board.cancel(buy(kg(3.5), £(306), Alice));

        assertThat(board.summary()).isEmpty();
    }

    @Test
    public void should_not_cancel_a_registered_order_when_requested_so_by_a_user_who_did_not_place_it() {

        board.register(buy(kg(3.5), £(306), Alice));
        board.cancel(buy(kg(3.5), £(306), Bob));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(3.5), £(306))
        );
    }

    @Test
    public void should_allow_to_cancel_one_of_several_registered_orders() {

        board.register(buy(kg(3.5), £(306), Alice));
        board.register(buy(kg(2.5), £(306), Alice));
        board.register(buy(kg(1.5), £(306), Alice));

        board.cancel(buy(kg(3.5), £(306), Alice));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(4.0), £(306))
        );
    }

    @Test
    public void should_produce_distinct_summaries_for_orders_with_different_price() {

        board.register(buy(kg(3.5), £(306), Alice));
        board.register(buy(kg(7.0), £(250), Alice));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(3.5), £(306)),
                buyOrderSummary(kg(7.0), £(250))
        );
    }

    @Test
    public void should_sort_Sell_orders_in_ascending_order() {

        board.register(sell(kg(3.5), £(306), Alice));
        board.register(sell(kg(7.0), £(250), Alice));

        assertThat(board.summary()).containsExactly(
                sellOrderSummary(kg(7.0), £(250)),
                sellOrderSummary(kg(3.5), £(306))
        );
    }

    @Test
    public void should_sort_Buy_orders_in_descending_order() {

        board.register(buy(kg(7.0), £(250), Alice));
        board.register(buy(kg(3.5), £(306), Alice));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(3.5), £(306)),
                buyOrderSummary(kg(7.0), £(250))
        );
    }

    @Test
    public void should_display_Buy_orders_before_Sell_orders() {

        board.register(buy(kg(7.0), £(250), Alice));
        board.register(sell(kg(7.0), £(430), Alice));

        assertThat(board.summary()).containsExactly(
                buyOrderSummary(kg(7.0), £(250)),
                sellOrderSummary(kg(7.0), £(430))
        );
    }

    // a tiny DSL to improve the readability of the test scenarios

    private static OrderSummary buyOrderSummary(Quantity<Mass> quantity, PricePerKg pricePerKg) {
        return new OrderSummary(quantity, pricePerKg, Order.Type.Buy);
    }

    private static OrderSummary sellOrderSummary(Quantity<Mass> quantity, PricePerKg pricePerKg) {
        return new OrderSummary(quantity, pricePerKg, Order.Type.Sell);
    }
}
