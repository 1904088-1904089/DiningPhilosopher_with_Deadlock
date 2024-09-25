import java.util.concurrent.*;
import java.util.ArrayList;
import java.util.List;

public class DiningPhilosophers {
    public static final int NUM_TABLES = 5;
    public static final int NUM_PHILOSOPHERS_PER_TABLE = 5;
    public static final int THINK_TIME_MIN = 0;
    public static final int THINK_TIME_MAX = 1000;  // in milliseconds
    public static final int EAT_TIME_MIN = 0;
    public static final int EAT_TIME_MAX = 500;  // in milliseconds
    public static final int WAIT_TIME = 4000;  // in milliseconds
    private static final List<Philosopher> sixthTable = new ArrayList<>();
    private static final Object lock = new Object();

    public static void main(String[] args) {
        Fork[][] forks = new Fork[NUM_TABLES][NUM_PHILOSOPHERS_PER_TABLE];
        Thread[][] philosophers = new Thread[NUM_TABLES][NUM_PHILOSOPHERS_PER_TABLE];

        // Create forks
        for (int i = 0; i < NUM_TABLES; i++) {
            for (int j = 0; j < NUM_PHILOSOPHERS_PER_TABLE; j++) {
                forks[i][j] = new Fork();
            }
        }

        // Create philosophers and start them
        char philosopherLabel = 'A';
        for (int i = 0; i < NUM_TABLES; i++) {
            for (int j = 0; j < NUM_PHILOSOPHERS_PER_TABLE; j++) {
                Fork leftFork = forks[i][j];
                Fork rightFork = forks[i][(j + 1) % NUM_PHILOSOPHERS_PER_TABLE];
                Philosopher philosopher = new Philosopher(i, j, leftFork, rightFork, sixthTable, philosopherLabel);
                philosophers[i][j] = new Thread(philosopher);
                philosophers[i][j].start();
                philosopherLabel++;
            }
        }
    }

    public static void checkSixthTableDeadlock() {
        synchronized (lock) {
            if (sixthTable.size() == NUM_PHILOSOPHERS_PER_TABLE) {
                System.out.println("Sixth table deadlocked. Last philosopher to join: " + sixthTable.get(NUM_PHILOSOPHERS_PER_TABLE - 1).philosopherLabel);
                System.exit(0);  // Terminate the simulation
            }
        }
    }
}

class Fork {
    private final Semaphore semaphore = new Semaphore(1);

    public void acquire() throws InterruptedException {
        semaphore.acquire();
    }

    public void release() {
        semaphore.release();
    }

    public boolean isHeldByCurrentThread() {
        return semaphore.availablePermits() == 0;
    }
}
