
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Table {
    private final int tableId;
    private final Fork[] forks = new Fork[5];
    private final List<Philosopher> philosophers = new ArrayList<>();

    public Table(int id) {
        this.tableId = id;
        for (int i = 0; i < 5; i++) {
            forks[i] = new Fork();
        }
    }

    public void addPhilosopher(Philosopher philosopher) {
        philosophers.add(philosopher);
    }

    public void startPhilosophers() {
        for (Philosopher philosopher : philosophers) {
            philosopher.start();
        }
    }

    public void stopPhilosophers() {
        for (Philosopher philosopher : philosophers) {
            philosopher.stopPhilosopher();
        }
    }

    public void detectDeadlock() {
        while (true) {
            try {
                Thread.sleep(5000);  // Periodic deadlock check
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (philosophers) {
                boolean allWaiting = philosophers.stream().allMatch(philosopher -> philosopher.getState() == Thread.State.TIMED_WAITING);
                if (allWaiting) {
                    System.out.println("Deadlock detected at Table " + tableId);
                    Philosopher movingPhilosopher = philosophers.get(new Random().nextInt(philosophers.size()));
                    philosophers.remove(movingPhilosopher);
                    movingPhilosopher.moveToSixthTable();
                    break;
                }
            }
        }
    }

    public Fork getLeftFork(int philosopherIndex) {
        return forks[philosopherIndex];
    }

    public Fork getRightFork(int philosopherIndex) {
        return forks[(philosopherIndex + 1) % 5];
    }

    public List<Philosopher> getPhilosophers() {
        return philosophers;
    }
}
