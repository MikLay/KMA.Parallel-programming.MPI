package Task2;

// Для кращою демонстрації модифікував Consumer.
// Producer відправляє товар на склад, щоб було не менше двох товарів
// Consumer бере товар зі складу, якщо він наявний на складі та обробляє товар залежно від швидкості своєї роботи.
public class ProducerDemo {

  public static void main(String[] args) {
    RealQueue myQueue = new RealQueue();

    Producer producer = new Producer(myQueue);
    Consumer consumer = new Consumer(myQueue, 2000);
    Consumer consumer1 = new Consumer(myQueue, 6000);

    Thread t1 = new Thread(producer);
    Thread t2 = new Thread(consumer);
    Thread t3 = new Thread(consumer1);

    t1.start();
    t2.start();
    t3.start();
  }
}
/*
Відправлено на склад: 0
Відправлено на склад: 1
Отримано зі складу [390b570]: 0
Отримано зі складу [2f8b1fa3]: 1
Відправлено на склад: 2
Відправлено на склад: 3
Отримано зі складу [2f8b1fa3]: 2
Відправлено на склад: 4
Отримано зі складу [2f8b1fa3]: 3
Відправлено на склад: 5
Отримано зі складу [390b570]: 4
Відправлено на склад: 6
Отримано зі складу [2f8b1fa3]: 5
Відправлено на склад: 7
Отримано зі складу [2f8b1fa3]: 6
Відправлено на склад: 8
Отримано зі складу [2f8b1fa3]: 7
Відправлено на склад: 9
Отримано зі складу [390b570]: 8
Відправлено на склад: 10
Отримано зі складу [2f8b1fa3]: 9
Відправлено на склад: 11
Отримано зі складу [2f8b1fa3]: 10
Відправлено на склад: 12
Отримано зі складу [2f8b1fa3]: 11
Відправлено на склад: 13
Отримано зі складу [390b570]: 12
Відправлено на склад: 14
Отримано зі складу [2f8b1fa3]: 13
Відправлено на склад: 15
Отримано зі складу [2f8b1fa3]: 14
Відправлено на склад: 16
Отримано зі складу [2f8b1fa3]: 15
Відправлено на склад: 17
Отримано зі складу [390b570]: 16
Відправлено на склад: 18
Отримано зі складу [2f8b1fa3]: 17
Відправлено на склад: 19
Отримано зі складу [2f8b1fa3]: 18

і так далі...

Очевидно, що перший Consumer обробляє товар у 3 рази швидше ніж перший (зважаючи на час обробки товару)
*/
