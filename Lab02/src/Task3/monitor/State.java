package Task3.monitor;

import Task3.Fork;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class State {
  private final int philosophersAmount;

  private enum PhilosopherState {
    HUNGRY,
    EATING,
    THINKING,
  }

  private PhilosopherState[] philosopherStates;

  private Lock lock;
  private Condition[] cond;

  public State(int philosophersAmount) {
    this.philosophersAmount = philosophersAmount;
    lock = new ReentrantLock();
    philosopherStates = new PhilosopherState[this.philosophersAmount];
    cond = new Condition[this.philosophersAmount];

    for (int i = 0; i < this.philosophersAmount; i++) {
      philosopherStates[i] = PhilosopherState.THINKING;
      cond[i] = lock.newCondition();
    }
  }

  public void takeForks(int id, Fork l, Fork r) {
    lock.lock();
    try {
      setState(id, PhilosopherState.HUNGRY);

      while (!l.isAvailable() || !r.isAvailable()) {
        cond[id].await();
      }

      l.setAvailable(false);
      r.setAvailable(false);
      setState(id, PhilosopherState.EATING);
      printState(id);
    } catch (InterruptedException e) {
      e.printStackTrace();
    } finally {
      lock.unlock();
    }
  }

  public void putForks(int id, Fork l, Fork r) {
    lock.lock();
    try {
      setState(id, PhilosopherState.THINKING);
      printState(id);
      l.setAvailable(true);
      r.setAvailable(true);
      cond[(id + 1) % philosophersAmount].signalAll();
      cond[(id + philosophersAmount - 1) % philosophersAmount].signalAll();
    } finally {
      lock.unlock();
    }
  }

  private void setState(int id, PhilosopherState s) {
    philosopherStates[id] = s;
  }

  private void printState(int id) {
    StringBuffer line = new StringBuffer();
    for (int i = 0; i < philosophersAmount; i++) {
      switch (philosopherStates[i]) {
        case THINKING:
          line.append("O ");
          break;
        case HUNGRY:
          line.append("- ");
          break;
        case EATING:
          line.append("X ");
          break;
      }
    }
    System.out.println(line + "(" + (id + 1) + ")");
  }
}
