package Task3;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Starter {
  private final List<Fork> forks;
  private final List<? extends Philosopher> philosophers;

  protected Starter() {
    this.forks = setForks();
    this.philosophers = setPhilosophers(forks);
  }

  protected abstract List<? extends Philosopher> setPhilosophers(
    List<Fork> forks
  );

  protected abstract void stopProcesses(List<Thread> threads);

  public List<Fork> getForks() {
    return forks;
  }

  public List<? extends Philosopher> getPhilosophers() {
    return philosophers;
  }

  public void starter() {
    List<Thread> threads = setThreads(philosophers);

    threads.forEach(Thread::start);

    System.out.println(System.nanoTime() + ": Початок вечері");

    waitForA(Constants.DINNER_TIME);

    stopProcesses(threads);

    System.out.println(System.nanoTime() + ": Кінець вечері");
    printStats(philosophers);
  }

  private static List<Fork> setForks() {
    return IntStream
      .range(0, Constants.PHILOSOPHERS_AMOUNT)
      .mapToObj(Fork::new)
      .collect(Collectors.toList());
  }

  protected static List<Thread> setThreads(
    List<? extends Philosopher> philosophers
  ) {
    return philosophers.stream().map(Thread::new).collect(Collectors.toList());
  }

  protected void stopThreads(List<Thread> threads) {
    threads.forEach(
      thread -> {
        if (!thread.isInterrupted()) {
          thread.interrupt();
        }
      }
    );
    waitForA(100);
  }

  protected static void waitForA(long ms) {
    if (ms <= 0) {
      return;
    }
    try {
      Thread.sleep(ms);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }

  protected static void printStats(List<? extends Philosopher> philosophers) {
    int totalCount = philosophers
      .stream()
      .map(Philosopher::getTimesEated)
      .reduce(0, Integer::sum);

    if (totalCount > 0) {
      System.out.println("--------------------\nСтатистика вечері:");
      System.out.println("Загалом: " + totalCount);
      philosophers.forEach(
        philosopher ->
          System.out.println(
            "Філософ " +
            philosopher.getName() +
            ": " +
            (100.0 * philosopher.getTimesEated() / totalCount) +
            "%"
          )
      );
    }
  }
}
