package Task2;

import java.util.ArrayDeque;
import java.util.Queue;

public class RealQueue extends MyQueue {
  private final Queue<Integer> n = new ArrayDeque<>();

  @Override
  public synchronized int get(String consumer) {
    while (n.size() <= 0) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    int element = n.remove();

    System.out.println("Отримано зі складу [" + consumer + "]: " + element);
    notify();
    return element;
  }

  @Override
  public synchronized void put(int n) {
    while (this.n.size() >= 2) {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

    this.n.add(n);
    System.out.println("Відправлено на склад: " + n);
    notify();
  }
}
