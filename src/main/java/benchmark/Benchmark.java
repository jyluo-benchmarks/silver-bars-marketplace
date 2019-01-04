package benchmark;

import static org.joda.money.CurrencyUnit.CAD;
import static tec.uom.se.unit.Units.KILOGRAM;

import java.math.BigDecimal;
import java.util.List;

import javax.measure.Quantity;
import javax.measure.quantity.Mass;

import com.silverbars.LiveOrderBoard;
import com.silverbars.domain.Order;
import com.silverbars.domain.OrderSummary;
import com.silverbars.domain.PricePerKg;
import com.silverbars.domain.UserId;

import tec.uom.se.quantity.Quantities;

public class Benchmark {

    public static final UserId Alice = UserId.of("user 1");

    // java.lang.OutOfMemoryError: GC overhead limit exceeded:
    // 10000000
    // 100000000
    // 1000000000

    // time for successful benchmarks:
    // 1000000 -> 789 insert, 79 summary with DoubleQuantity
    // 1000000 -> 866 insert, 122 summary with DecimalQuantity + BigDecimal for each
    // value

    public static final int repetitions = 1000000;
    public static final int pricePerBar = 1;

    // public static final double numOfBarsPerOrder = 0.5; // for DoubleQuantity
    // all quantities share a reference to this concrete value, but many quantity
    // objects created
    public static final BigDecimal numOfBarsPerOrder = new BigDecimal(0.5); // for DecimalQuantity

    public static void main(String[] args) {
        System.out.println("Starting benchmark");

        LiveOrderBoard board = new LiveOrderBoard();

        Quantity<Mass> orderQuantity = null;

        long insertStart = System.currentTimeMillis();
        for (int i = 0; i < repetitions; i++) {
            orderQuantity = Quantities.getQuantity(numOfBarsPerOrder, KILOGRAM);
            board.register(new Order(Alice, orderQuantity, PricePerKg.of(CAD, pricePerBar),
                    Order.Type.Buy));
        }
        long insertEnd = System.currentTimeMillis();

        System.out.println("Insertion done");

        long summaryStart = System.currentTimeMillis();
        List<OrderSummary> summaries = board.summary();
        long summaryEnd = System.currentTimeMillis();

        System.out.println("insertion to board (ms): " + (insertEnd - insertStart));
        System.out.println("board summary (ms): " + (summaryEnd - summaryStart));

        assert summaries.size() == 1;

        OrderSummary summary = summaries.get(0);

        System.out.println(summary);

        Quantity<Mass> quantity = summary.quantity();

        System.out.println("concrete order quantity class: " + orderQuantity.getClass());
        System.out.println("concrete summary quantity class: " + quantity.getClass());

        assert quantity.getValue().doubleValue() == numOfBarsPerOrder.doubleValue() * repetitions;
        assert summary.pricePerKg().equals(PricePerKg.of(CAD, pricePerBar * repetitions));

        System.out.println("benchmark complete");
    }
}
