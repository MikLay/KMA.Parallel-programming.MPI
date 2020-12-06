package Task3;

import static Task3.Constants.THINKING_TIME;

import java.util.concurrent.atomic.AtomicInteger;

public abstract class Philosopher implements Runnable {
  protected final int id;

  protected final String name;
  protected final Fork leftFork;
  protected final Fork rightFork;

  public AtomicInteger timesEated = new AtomicInteger();

  public Philosopher(int id, String name, Fork leftFork, Fork rightFork) {
    this.id = id;
    this.name = name;
    this.leftFork = leftFork;
    this.rightFork = rightFork;
  }

  protected void think() throws InterruptedException {
    System.out.println(System.nanoTime() + ": " + getName() + " думає...");
    Thread.sleep(getRandomTime());
  }

  protected void eat() throws InterruptedException {
    System.out.println(System.nanoTime() + ": " + getName() + " їсть.");
    Thread.sleep(getRandomTime());
    timesEated.incrementAndGet();
  }

  public String getName() {
    return name;
  }

  public int getTimesEated() {
    return timesEated.get();
  }

  protected int getRandomTime() {
    return (int) (Math.random() * THINKING_TIME);
  }
}
