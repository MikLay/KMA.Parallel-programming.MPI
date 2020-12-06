package Task2;

public class Producer implements Runnable {
  private final RealQueue myQueue;

  public Producer(RealQueue myQueue) {
    this.myQueue = myQueue;
  }

  @Override
  public void run() {
    for (int i = 0; true; i++) {
      myQueue.put(i);
    }
  }
}
