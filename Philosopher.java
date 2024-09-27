
import java.util.Random;

public class Philosopher extends Thread {
    private final String name;
    private final Fork leftFork;
    private final Fork rightFork;
    private final int tableId;
    private final Table sixthTable;
    private final Random random = new Random();
    private volatile boolean isStopped = false;

    public Philosopher(String name, Fork leftFork, Fork rightFork, int tableId, Table sixthTable) {
        this.name = name;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.tableId = tableId;
        this.sixthTable = sixthTable;
    }

    public void stopPhilosopher() {
        isStopped = true;
    }

    @Override
    public void run() {
        while (!isStopped) {
            think();
            if (pickUpForks()) {
                eat();
                putDownForks();
            }
        }
    }

    private void think() {
        System.out.println(name + " is thinking at Table " + tableId);
        try {
            Thread.sleep(random.nextInt(2000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private boolean pickUpForks() {
        System.out.println(name + " at Table " + tableId + " is trying to pick up forks");
        if (leftFork.pickUp()) {
            System.out.println(name + " picked up left fork at Table " + tableId);
            try {
                Thread.sleep(100);  // Simulate waiting for right fork
                if (rightFork.pickUp()) {
                    System.out.println(name + " picked up right fork at Table " + tableId);
                    return true;
                } else {
                    leftFork.putDown();
                    System.out.println(name + " couldn't pick up right fork at Table " + tableId);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        return false;
    }

    private void eat() {
        System.out.println(name + " is eating at Table " + tableId);
        try {
            Thread.sleep(random.nextInt(1000));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    private void putDownForks() {
        System.out.println(name + " is putting down forks at Table " + tableId);
        leftFork.putDown();
        rightFork.putDown();
    }

    public void moveToSixthTable() {
        sixthTable.addPhilosopher(this);
        System.out.println(name + " has moved to Table 6.");
    }

    public String getPhilosopherName() {
        return name;
    }

    public int getTableId() {
        return tableId;
    }
}
