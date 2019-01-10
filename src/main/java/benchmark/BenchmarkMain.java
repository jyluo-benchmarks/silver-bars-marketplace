package benchmark;

import static org.joda.money.CurrencyUnit.CAD;
import static tec.uom.se.unit.Units.KILOGRAM;

import java.util.List;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

import org.openjdk.jmh.annotations.Benchmark;

import com.silverbars.LiveOrderBoard;
import com.silverbars.domain.Order;
import com.silverbars.domain.OrderSummary;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;

import tec.uom.se.quantity.Quantities;

public class BenchmarkMain {

    public static final UserId Alice = UserId.of("user 1");

    // 1000000

    public static final int repetitions = 1000000;
    public static final int pricePerBar = 1;

    public static final Number numOfBarsPerOrder = new Double(0.5);

    public static final LiveOrderBoard board = new LiveOrderBoard();

    public static List<OrderSummary> summaries;

    @Benchmark
    public static void insertOrders() {
        for (int i = 0; i < repetitions; i++) {
            board.register(new Order(Alice, Quantities.getQuantity(numOfBarsPerOrder, KILOGRAM), PricePerKg.of(CAD, pricePerBar),
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

        Quantity<Mass> quantity = summary.quantity();

        assert quantity.getValue().doubleValue() == numOfBarsPerOrder.doubleValue() * repetitions;
        assert summary.pricePerKg().equals(PricePerKg.of(CAD, pricePerBar * repetitions));

        System.out.println(" Total ordered: " + quantity);

        System.out.println("benchmark complete");
    }
}
