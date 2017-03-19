# The Live Order Board

In this exercise we're asked to design a "Live Order Board" for a company called "Silver Bars Marketplace".
The responsibility of the Live Order Board is to tell the users how much demand for silver bars there is on the market.

To make the exercise more challenging I decided to apply some of the rules outlined by [Jeff Bay](http://www.xpteam.com/) 
in ["Object Calisthenics"](http://www.xpteam.com/jeff/writings/objectcalisthenics.rtf)
 published as part of the ["The ThoughtWorks Anthology"](http://amzn.to/2nARzzx), namely:
 
1. Use only one level of indentation per method. 
2. Don’t use the else keyword.
3. Wrap all primitives and strings.
4. Use only one dot per line.
5. Don’t abbreviate.
6. Keep all entities small.
7. Don’t use any classes with more than two instance variables.
8. Use first-class collections.
9. Don’t use any getters/setters/properties.

The article can be [downloaded here](http://www.xpteam.com/jeff/writings/objectcalisthenics.rtf).

## Features

### Register an order

The [`LiveOrderBoard`](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/LiveOrderBoard.java) should allow the user to register an [`Order`](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/domain/Order.java), which, according to the requirements, 
_"must contain the following fields"_: 
user id, order quantity (e.g.: 3.5 kg), price per kg (e.g.: £303) as well as the type of order: either buy or sell.

Modelling the above using primitive numeric data types would 
[negatively affect the precision of our calculations](http://stackoverflow.com/questions/3730019/why-not-use-double-or-float-to-represent-currency),
while relying on objects such as `String` or `BigDecimal` would prevent us from retaining the semantic meaning
of the domain concepts.

To address those problems and avoid re-inventing the wheel where good open-source libraries exist, 
information contained in an `Order` is modelled as follows:
- **user id** - to retain the meaning of a domain concept, 
the user id is modelled using a ['Tiny Type'](http://www.markphelps.me/2014/12/09/tiny-types.html) mini-pattern.
- **order quantity (e.g.: 3.5 kg)** - to make the basic arithmetic operations easier and retain the meaning of the domain concept 
the quantity of the order is modelled using the  [`unitsofmeasurement`](https://github.com/unitsofmeasurement/uom-se) library, 
which is a reference implementation of the [JSR 363: Units of Measurement API](https://www.jcp.org/en/jsr/detail?id=363)
- **price per kg (e.g.: £303)** - to allow for precise comparison of orders, the price is modelled using the [Joda Money](http://www.joda.org/joda-money/) library, 
which `Money` type is wrapped in the `PricePerKg` to retain the domain concept
- **order type: BUY or SELL** - the type of the order is modelled using a nested enumeration, defined as part of the Order class

_To see how this design could be improved to better follow OOP, see my notes in the [Improving the design](#improving-the-design) section below._  

With the above in place, the registration of an `Order` [looks as follows](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/test/java/com/silverbars/domain/OrderTest.java):

```java
LiveOrderBoard board = new LiveOrderBoard();

board.register(new Order(
        new UserId("user 1"),
        Quantities.getQuantity(3.5, KILOGRAM),
        new PricePerKg(Money.of(GBP, 306)),
        Order.Type.Buy
));
```

However, the instantiation of an `Order` can be made more readable 
thanks to [static factory methods](https://sourcemaking.com/design_patterns/factory_method):

```java
LiveOrderBoard board = new LiveOrderBoard();

board.register(new Order(
        UserId.of("user 1"),
        Quantities.getQuantity(3.5, KILOGRAM),
        PricePerKg.of(GBP, 306),
        Order.Type.Buy
));
```

To simplify this further, I've created a simple DSL which you can [see being used in the automated tests](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/test/java/com/silverbars/LiveOrderBoardTest.java#L33):

```java
board.register(buy(kg(3.5), £(306), Alice));
```

[This tiny DSL](https://github.com/jan-molak/silver-bars-marketplace/tree/master/src/test/java/com/silverbars/support) helps to highlight the important details of the tests and avoid most of the boilerplate code.

_**Side note 1**: The `£(amount)` function is an alias for `PricePerKg.of(GBP, amount)`, which makes 
the test easier to read, but to be useful requires the developers working with such code to be able to use 
the `£` symbol on their keyboards. In an international setting, calling the function something like `gbp(amount)` 
would be more keyboard-friendly._

_**Side note 2**: The order quantity could've also been designed as a Tiny Type and use `unitsofmeasurement`
under the hood. This however felt like introducing unnecessary indirection as `Quantity<Mass>`
coming from `unitsofmeasurement` seems descriptive enough._

### Cancel a registered order

It seemed sensible that an `Order` can be cancelled only by the user who placed it. 

```java
LiveOrderBoard board = new LiveOrderBoard();

Order order = buy(kg(3.5), £(306), Alice);

board.register(order);
board.cancel(order);
```

To keep it simple, the current implementation does not throw an exception when a user tries to cancel someone else's order
(nor it will notify the authorities ;) ) - the operation will just not affect the state of the board.

### Summary information of live orders

The [`LiveOrderBoard`](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/LiveOrderBoard.java#L16) is backed by a simple `List<Order>` implementation, which makes the cost of inserting (registering)
and removing (cancelling) `Orders` linear - `O(n)`. 
 
To generate a "live summary", the orders in the list need to be [aggregated and mapped](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/LiveOrderBoard.java#L28) to instances of `OrderSummary`.
As this is done using Java 8 Streams API, the cost of aggregation and mapping is kept at `O(n)`, as the list only needs
to be processed once.

The list of `OrderSummary` objects is sorted using the [`OrderComparator`](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/OrderComparator.java#L9), 
which is a composition of three other comparators. The comparators in question fulfil the ordering requirements of the exercise, but  are [hard-coded in the `OrderComparator`](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/OrderComparator.java#L10-L12). If there was a need to make the ordering rules
more sophisticated the list of comparators could be made configurable, or an alternative comparator injected into 
the `LiveOrderBoard`.

## Design Considerations

### Scalability

The solution of the exercise is implemented to optimise for readability and low cost of maintenance of the code base.
The time complexity of `LiveOrderBoard` access operations is fairly good at `O(n)`, 
and all the domain objects are immutable and thread-safe.

However, the `List<Order>` backing the `LiveOrderBoard` would not be sufficient to support 
a real-world implementation of a metal market exchange, which presumably would be required to process large order volumes 
in parallel.

### Improving the design

The exercise puts a constraint on the design of the `Order` class saying that:
  
> Order **must contain these fields**:
> - user id  
> - order quantity (e.g.: 3.5 kg)
> - price per kg (e.g.: £303)
> - order type: BUY or SELL

If the requirement stated that the _`Order` class must contain the following **information**_ rather than **fields**, 
an alternative, more OOP-friendly design would be possible.

Instead of defining an `Order` as 

```java
new Order(
        UserId.of("user 1"),
        Quantities.getQuantity(10, KILOGRAM),
        PricePerKg.of(GBP, 203),
        Order.Type.Buy
);
```

we could introduce a `Bid` interface, with two concrete implementations: `Buy` and `Sell`, so that:

```java
new Order(
        UserId.of("user 1"),
        Quantities.getQuantity(10, KILOGRAM),
        new Buy(PricePerKg.of(GBP, 203)
);
```

The same approach would work with the `OrderSummary` as it also needs to know about the type of the order.

Instead of:

```java
new OrderSummary(Quantities.getQuantity(10, KILOGRAM), PricePerKg.of(GBP, 203), Order.Type.Buy);
```

we'd have:

```java
new OrderSummary(Quantities.getQuantity(10, KILOGRAM), new Buy(PricePerKg.of(GBP, 203)));
```

The above refactoring would allow us to use polymorphism instead of `if` statements in the `OrderComparator` and
make the `Bid` a part of both the `Order` and `OrderSummary`, rather than a [utility class](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/domain/Bid.java) used merely to [make the conversion from `Order` to `OrderSummary` easier](https://github.com/jan-molak/silver-bars-marketplace/blob/master/src/main/java/com/silverbars/LiveOrderBoard.java#L30).

----

-- [Jan Molak](https://janmolak.com)
