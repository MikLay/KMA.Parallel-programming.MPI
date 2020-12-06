public class Task2 {
    public static void main(String[] args) {
        NewThread myThread = new NewThread();
        myThread.start();
    }
}

class NewThread extends Thread{
    @Override
    public void run() {
        for(int i = 0; i < 100; i++){
            System.out.println(i + ": +");
        }
    }
}
