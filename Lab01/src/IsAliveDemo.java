class MyThread1 extends Thread {
    public void run() {
        System.out.println("Important job running in " + getName());
    }
}

/*
Important job running in Thread-0
*/

class MyThread extends Thread {
    public void run() {
        System.out.println("Important job running in MyThread");
    }
    public void run(String s) {
        System.out.println("String in run is " + s);
    }
}

/*
Important job running in MyThread
*/

class MyRunnable implements Runnable {
    public void run() {
        System.out.println("Important job running");
    }
}
/*public class MyRunnableDemo {
    public static void main(String[] args) {
        MyRunnable myRunnable = new MyRunnable();

        Thread thread1 = new Thread(myRunnable);
        Thread thread2 = new Thread(myRunnable);
        Thread thread3 = new Thread(myRunnable);

        thread1.start();
        thread2.start();
        thread3.start();
    }
}*/

/*
Important job running
Important job running
Important job running
*/

class NameRunnable implements Runnable {
    public void run() {
        System.out.println("NameRunnable running");
        System.out.println("Run by "
                + Thread.currentThread().getName());
    }
}

/*
public class NameThreadDemo {
    public static void main(String[] args) {
        NameRunnable nr = new NameRunnable();
        Thread tread1 = new Thread(nr);
        tread1.setName("First Thread");
        tread1.start();

        Thread tread2 = new Thread(nr, "Second Thread");
        tread2.start();
    }
}
*/


/*
NameRunnable running
NameRunnable running
Run by First Thread
Run by Second Thread
*/

class SleepRunnable implements Runnable {
    public void run() {
        for (int x = 1; x < 4; x++) {
            System.out.println("Run by "
                    + Thread.currentThread().getName()
                    + ", x is " + x);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }
}

/*
public class SleepRunnableDemo {
    public static void main(String[] args) {
        SleepRunnable sleepRunnable = new SleepRunnable();

        Thread one = new Thread(sleepRunnable);
        one.setName("Fred");
        Thread two = new Thread(sleepRunnable);
        two.setName("Lucy");
        Thread three = new Thread(sleepRunnable);
        three.setName("Ricky");

        one.start();
        two.start();
        three.start();
    }
}
*/

/*
Run by Ricky, x is 1
Run by Lucy, x is 1
Run by Fred, x is 1
Run by Ricky, x is 2
Run by Fred, x is 2
Run by Lucy, x is 2
Run by Ricky, x is 3
Run by Fred, x is 3
Run by Lucy, x is 3
*/

class JoinRunnable extends Thread {
    public JoinRunnable(String name) {
        super(name);
    }

    @Override
    public void run() {
        String currentThreadName = Thread.currentThread().getName();
        for (int i = 0; i < 10; i++) {
            System.out.println(currentThreadName + " is running!" + i);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(currentThreadName + " completed");
    }
}

/*
public class JoinDemo {
    public static void main(String[] args) {
        JoinRunnable a = new JoinRunnable("A");
        JoinRunnable b = new JoinRunnable("B");
        JoinRunnable c = new JoinRunnable("C");

        a.start();
        try {
            a.join();
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
        b.start();
        c.start();
    }
}
*/

/*
A is running!0
A is running!1
A is running!2
A is running!3
A is running!4
A is running!5
A is running!6
A is running!7
A is running!8
A is running!9
A completed
B is running!0
C is running!0
C is running!1
B is running!1
B is running!2
C is running!2
B is running!3
C is running!3
C is running!4
B is running!4
C is running!5
B is running!5
C is running!6
B is running!6
C is running!7
B is running!7
C is running!8
B is running!8
C is running!9
B is running!9
C completed
B completed
*/


public class IsAliveDemo {
    public static void main(String[] args) throws InterruptedException {
        MyRunnable myRunnable = new MyRunnable();

        Thread thread = new Thread(myRunnable);
        System.out.println("Before starting: " + thread.isAlive());
        thread.start();
        System.out.println("After starting: " + thread.isAlive());
        thread.join();
        System.out.println("After thread completed: " + thread.isAlive());
    }
}

/*

Before starting: false
After starting: true
Important job running
After thread completed: false

*/
