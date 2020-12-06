package Task3;

public class Fork {
  private final int id;
  private boolean available = true;

  public Fork(int id) {
    this.id = id;
  }

  public void take() {
    System.out.println(System.nanoTime() + ": Вилку " + id + " взяли");
  }

  public void put() {
    System.out.println(System.nanoTime() + ": Вилку " + id + " поклали");
  }

  public int getId() {
    return id;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}
