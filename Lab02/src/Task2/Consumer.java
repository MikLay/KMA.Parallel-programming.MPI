package Task2;

public class Consumer implements Runnable {
  private final RealQueue myQueue;

  // Уявний час на обробку одного товару зі складу
  private final int speedOfWork;

  public Consumer(RealQueue myQueue, int speedOfWork) {
    this.myQueue = myQueue;
    this.speedOfWork = speedOfWork;
  }

  @Override
  public void run() {
    while (true) {
      myQueue.get(Integer.toHexString(hashCode()));

      try {
        Thread.sleep(speedOfWork);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
