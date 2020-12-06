package Task3.monitor;

import Task3.Fork;
import Task3.Philosopher;
import java.util.concurrent.atomic.AtomicBoolean;

public class MonitorPhilosopher extends Philosopher implements Runnable {
  private final State state;
  private final AtomicBoolean stopped = new AtomicBoolean();

  public MonitorPhilosopher(
    int id,
    String name,
    Fork leftFork,
    Fork rightFork,
    State state
  ) {
    super(id, name, leftFork, rightFork);
    this.state = state;
  }

  @Override
  public void run() {
    try {
      while (!stopped.get()) {
        think();
        state.takeForks(id, leftFork, rightFork);
        eat();
        state.putForks(id, leftFork, rightFork);
      }
    } catch (InterruptedException ignored) {}
  }

  public void stop() {
    stopped.set(true);
  }

  public AtomicBoolean isStopped() {
    return stopped;
  }
}
