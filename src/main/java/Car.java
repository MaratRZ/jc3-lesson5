import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;

public class Car implements Runnable {
    private static int CARS_COUNT;
    private static boolean winnerFound;

    static {
        CARS_COUNT = 0;
    }
    private Race race;
    private int speed;
    private String name;

    private CountDownLatch cdPrepare;
    private CyclicBarrier cbStart;
    private CountDownLatch cdFinish;

    public String getName() {
        return name;
    }
    public int getSpeed() {
        return speed;
    }
    public Car(Race race, int speed, CountDownLatch cdPrepare, CyclicBarrier cbStart, CountDownLatch cdFinish) {
        this.race = race;
        this.speed = speed;
        this.cdPrepare = cdPrepare;
        this.cbStart = cbStart;
        this.cdFinish = cdFinish;
        CARS_COUNT++;
        this.name = "Участник #" + CARS_COUNT;
    }
    @Override
    public void run() {
        try {
            System.out.println(this.name + " готовится");
            Thread.sleep(500 + (int)(Math.random() * 800));
            cdPrepare.countDown();
            System.out.println(this.name + " готов");
            cbStart.await();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int i = 0; i < race.getStages().size(); i++) {
            race.getStages().get(i).go(this);
        }
        checkWinner(this);
        cdFinish.countDown();
    }

    private static synchronized void checkWinner(Car c) {
        if (!winnerFound) {
            winnerFound = true;
            System.out.println(c.name + " - WIN");
        }
    }
}
