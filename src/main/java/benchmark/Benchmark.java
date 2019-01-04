package benchmark;

import static org.joda.money.CurrencyUnit.CAD;
import static tec.uom.se.unit.Units.KILOGRAM;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

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

    // 1000000

    public static final int repetitions = 3000000;
    public static final int pricePerBar = 1;

    // public static final Number barValue = new Byte((byte) 1);
    // public static final Number barValue = new Short((short) 1);
    public static final Number barValue = new Integer(1);
    // public static final Number barValue = new Long(1);
    // public static final Number barValue = new Float(0.5);
    // public static final Number barValue = new Double(0.5);
    // public static final Number barValue = new AtomicInteger(1);
    // public static final Number barValue = new AtomicLong(1);
    // for DecimalQuantity
    // public static final Number barValue = BigInteger.valueOf(1);
    // public static final Number barValue = new BigDecimal(0.5);

    // all quantities share a reference to this concrete value, but many quantity
    // objects created
    public static final Number numOfBarsPerOrder = barValue;

    public static void main(String[] args) {
        System.out.println("Starting benchmark");

        LiveOrderBoard board = new LiveOrderBoard();

        Quantity<Mass> orderQuantity = null;

        // List<Quantity<Mass>> orders = new ArrayList<>(repetitions);
        // for (int i = 0; i < repetitions; i++) {
        // orders.add(orderQuantity);
        // }

        long insertStart = System.currentTimeMillis();
        for (int i = 0; i < repetitions; i++) {
            orderQuantity = Quantities.getQuantity(numOfBarsPerOrder, KILOGRAM);
            board.register(new Order(Alice, orderQuantity, PricePerKg.of(CAD, pricePerBar),
                    Order.Type.Buy));
        }
        long insertEnd = System.currentTimeMillis();

        System.out.println("Insertion done");

        // Quantity<Mass> sum = Quantities.getQuantity(new Byte((byte) 0), KILOGRAM);
        //// for(Quantity<Mass> i : orders) {
        //// sum = sum.add(i);
        //// }
        // sum = sum.add(orders.get(0));
        //
        // System.out.println("sum quantity class: " + sum.getClass());
        // System.out.println("sum number class: " + sum.getValue().getClass());

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
        System.out.println("concrete order number class: " + orderQuantity.getValue().getClass());
        System.out.println("concrete summary quantity class: " + quantity.getClass());
        System.out.println("concrete summary number class: " + quantity.getValue().getClass());

        assert quantity.getValue().doubleValue() == numOfBarsPerOrder.doubleValue() * repetitions;
        assert summary.pricePerKg().equals(PricePerKg.of(CAD, pricePerBar * repetitions));

        System.out.println("benchmark complete");
    }
}
