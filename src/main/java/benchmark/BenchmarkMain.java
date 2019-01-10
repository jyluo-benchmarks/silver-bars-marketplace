package benchmark;

import static org.joda.money.CurrencyUnit.CAD;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import units.qual.kg;
import static units.UnitsTools.kg;

import com.silverbars.LiveOrderBoard;
import com.silverbars.domain.Order;
import com.silverbars.domain.OrderSummary;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;

import org.openjdk.jmh.annotations.Benchmark;

public class BenchmarkMain {

    public static final UserId Alice = UserId.of("user 1");

    // 1000000

    public static final int repetitions = 1000000;
    public static final int pricePerBar = 1;

    public static final @kg double numOfBarsPerOrder = 1 * kg;
    // public static final @kg long numOfBarsPerOrder = 1L * kg;
    // public static final @kg float numOfBarsPerOrder = 0.5f * kg;
    // public static final @kg Number numOfBarsPerOrder = 0.5d * kg;
    // public static final @kg Number numOfBarsPerOrder = new AtomicInteger(1);
    // public static final @kg Number numOfBarsPerOrder = new AtomicLong(1);
    // for DecimalQuantity
    // public static final @kg Number numOfBarsPerOrder = BigInteger.valueOf(1);
    // public static final @kg Number numOfBarsPerOrder = new BigDecimal(0.5);

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
