package Task3.monitor;

import Task3.Constants;
import Task3.Fork;
import Task3.Philosopher;
import Task3.Starter;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MonitorStart extends Starter {

  @Override
  protected List<? extends Philosopher> setPhilosophers(List<Fork> forks) {
    State state = new State(Constants.PHILOSOPHERS_AMOUNT);
    return IntStream
      .range(0, Constants.PHILOSOPHERS_AMOUNT)
      .mapToObj(
        i ->
          new MonitorPhilosopher(
            i,
            Constants.names[i],
            forks.get(i),
            forks.get((i + 1) % forks.size()),
            state
          )
      )
      .collect(Collectors.toList());
  }

  @Override
  protected void stopProcesses(List<Thread> threads) {
    stopPhilosophers((List<MonitorPhilosopher>) getPhilosophers());
    stopThreads(threads);
  }

  private static void stopPhilosophers(List<MonitorPhilosopher> philosophers) {
    philosophers.forEach(
      philosopher -> {
        if (!philosopher.isStopped().get()) philosopher.stop();
      }
    );
    waitForA(200);
  }
}
/*
================================== (1) ====================================
---------------------------------------------------------------------------
Дана реалізація використовує ReentrantLock для котнролю входу в критичну
секцію (takeForks(), putForks()). Використовується умовна змінна Condition
для повідомлення іншим потокам про звільненні ресурси. Використовується клас
State для того, щоб контроювати отримання доступу до виделок.

1888194794102400: Початок вечері
1888194796279900: Сократ[4] думає...
1888194795960700: Спіноза[2] думає...
1888194795777000: Конфуцій[1] думає...
1888194796123300: Декарт[3] думає...
1888194794952700: Платон[0] думає...
O O O O X (5)
O O X O X (3)
1888194812181600: Сократ[4] їсть.
1888194812261900: Спіноза[2] їсть.
- - X - O (5)
1888194812659700: Сократ[4] думає...
- - O - O (3)
1888194812983900: Спіноза[2] думає...
- - O - X (5)
1888194813284800: Сократ[4] їсть.
- X O - X (2)
......................
......................
1888195301164400: Спіноза[2] їсть.
1888195301169900: Декарт[3] думає...
- - X O O (5)
X - X O O (1)
1888195301233800: Платон[0] їсть.
1888195301355500: Сократ[4] думає...
X - O O O (3)
1888195301576100: Спіноза[2] думає...
X - O X O (4)
1888195301942900: Декарт[3] їсть.
X - - O - (4)
X - X O - (3)
1888195302111400: Спіноза[2] їсть.
O - X O - (1)
O - X O X (5)
1888195302281100: Сократ[4] їсть.
O - O O X (3)
O X O O X (2)
1888195302380700: Конфуцій[1] їсть.
O X O O O (5)
O O O O O (2)
1888195621684300: Кінець вечері
--------------------
Статистика вечері:
Загалом: 4829
Філософ Платон[0]: 19.90060053841375%
Філософ Конфуцій[1]: 20.21122385587078%
Філософ Спіноза[2]: 19.79705943259474%
Філософ Декарт[3]: 20.045558086560366%
Філософ Сократ[4]: 20.045558086560366%

Process finished with exit code 0

 */
