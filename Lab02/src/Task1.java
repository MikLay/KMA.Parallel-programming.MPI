public class Task1 extends Thread {
  private final StringBuilder stringBuilder;

  public Task1(StringBuilder stringBuilder) {
    this.stringBuilder = stringBuilder;
  }

  @Override
  public void run() {
    synchronized (this) {
      for (int i = 0; i < 100; i++) {
        System.out.println(stringBuilder);
      }
      char a = stringBuilder.charAt(0);
      stringBuilder.replace(0, stringBuilder.length(), String.valueOf(++a));
    }
  }

  public static void main(String[] args) {
    StringBuilder stringB = new StringBuilder("a");
    Task1 task = new Task1(stringB);
    Thread thread1 = new Thread(task);
    Thread thread2 = new Thread(task);
    Thread thread3 = new Thread(task);
    thread1.start();
    thread2.start();
    thread3.start();
  }
}
