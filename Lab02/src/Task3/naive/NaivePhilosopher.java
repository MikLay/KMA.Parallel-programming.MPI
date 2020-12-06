package Task3.naive;

import Task3.Fork;
import Task3.Philosopher;

public class NaivePhilosopher extends Philosopher implements Runnable {

  protected NaivePhilosopher(
    int id,
    String name,
    Fork leftFork,
    Fork rightFork
  ) {
    super(id, name, leftFork, rightFork);
  }

  @Override
  public void run() {
    try {
      while (!Thread.currentThread().isInterrupted()) {
        think();
        synchronized (leftFork) {
          leftFork.take();
          synchronized (rightFork) {
            rightFork.take();
            eat();
            rightFork.put();
            leftFork.put();
          }
        }
      }
    } catch (InterruptedException e) {
      System.out.println(getName() + " був зупинений");
    }
  }
}
