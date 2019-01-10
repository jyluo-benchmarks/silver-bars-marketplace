package benchmark;

import static org.joda.money.CurrencyUnit.CAD;

import java.util.List;

import org.openjdk.jmh.annotations.Benchmark;

import com.silverbars.LiveOrderBoard;
import com.silverbars.domain.Order;
import com.silverbars.domain.OrderSummary;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;

import units.UnitsTools;
import units.qual.*;

public class BenchmarkMain {

    public static final UserId Alice = UserId.of("user 1");

    public static final int repetitions = 1000000;
    public static final int pricePerBar = 1;

    public static final @kg double numOfBarsPerOrder = 0.5 * UnitsTools.kg;

    public static final LiveOrderBoard board = new LiveOrderBoard();

    public static List<OrderSummary> summaries;

    @Benchmark
    public static void insertOrders() {
        for (int i = 0; i < repetitions; i++) {
            board.register(new Order(Alice, numOfBarsPerOrder, PricePerKg.of(CAD, pricePerBar),
                    Order.Type.Buy));
        }
    }

    @Benchmark
    public static void makeSummary() {
        summaries = board.summary();
    }

    public static void main(String[] args) {
        System.out.println("Starting benchmark");

        long insertStart = System.currentTimeMillis();
        insertOrders();
        long insertEnd = System.currentTimeMillis();

        System.out.println("Insertion done");

        long summaryStart = System.currentTimeMillis();
        makeSummary();
        long summaryEnd = System.currentTimeMillis();

        System.out.println("insertion to board (ms): " + (insertEnd - insertStart));
        System.out.println("board summary (ms): " + (summaryEnd - summaryStart));

        assert summaries.size() == 1;

        OrderSummary summary = summaries.get(0);

        System.out.println(summary);

        @kg double quantity = summary.quantity();

        assert quantity == numOfBarsPerOrder * repetitions;
        assert summary.pricePerKg().equals(PricePerKg.of(CAD, pricePerBar * repetitions));

        System.out.println(" Total ordered: " + quantity);

        System.out.println("benchmark complete");
    }
}
