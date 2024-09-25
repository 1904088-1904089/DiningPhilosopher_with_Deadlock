import java.util.concurrent.*;
import java.util.Random;
import java.util.List;

public class Philosopher implements Runnable {
    private final int tableNumber;
    private final int philosopherNumber;
    private final Fork leftFork;
    private final Fork rightFork;
    private final List<Philosopher> sixthTable;
    private final Random random = new Random();
    final char philosopherLabel; // package-private access modifier

    private boolean movedToSixthTable = false;
    
    public Philosopher(int tableNumber, int philosopherNumber, Fork leftFork, Fork rightFork, List<Philosopher> sixthTable, char philosopherLabel) {
        this.tableNumber = tableNumber;
        this.philosopherNumber = philosopherNumber;
        this.leftFork = leftFork;
        this.rightFork = rightFork;
        this.sixthTable = sixthTable;
        this.philosopherLabel = philosopherLabel;
    }

    @Override
    public void run() {
        while (true) {
            try {
                think();
                pickUpLeftFork();
                pickUpRightFork();
                eat();
                putDownLeftFork();
                putDownRightFork();
            } catch (InterruptedException e) {
                System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " interrupted due to deadlock.");
                dropForks();
                moveToEmptyTable();
            }
        }
    }

    private void think() throws InterruptedException {
        Thread.sleep(random.nextInt(DiningPhilosophers.THINK_TIME_MAX - DiningPhilosophers.THINK_TIME_MIN + 1) + DiningPhilosophers.THINK_TIME_MIN);
    }

    private void pickUpLeftFork() throws InterruptedException {
        leftFork.acquire();
        System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " picked up left fork.");
    }

    private void pickUpRightFork() throws InterruptedException {
        Thread.sleep(DiningPhilosophers.WAIT_TIME);
        rightFork.acquire();
        System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " picked up right fork.");
    }

    private void eat() throws InterruptedException {
        System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " is eating.");
        Thread.sleep(random.nextInt(DiningPhilosophers.EAT_TIME_MAX - DiningPhilosophers.EAT_TIME_MIN + 1) + DiningPhilosophers.EAT_TIME_MIN);
    }

    private void putDownLeftFork() {
        leftFork.release();
        System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " put down left fork.");
    }

    private void putDownRightFork() {
        rightFork.release();
        System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " put down right fork.");
    }

    private void dropForks() {
        if (leftFork.isHeldByCurrentThread()) {
            leftFork.release();
        }
        if (rightFork.isHeldByCurrentThread()) {
            rightFork.release();
        }
        System.out.println("Philosopher " + philosopherLabel + " on table " + tableNumber + " dropped forks.");
    }

    private void moveToEmptyTable() {
        synchronized (sixthTable) {
            if (!movedToSixthTable && sixthTable.size() < DiningPhilosophers.NUM_PHILOSOPHERS_PER_TABLE) {
                sixthTable.add(this);
                movedToSixthTable = true;
                System.out.println("Philosopher " + philosopherLabel + " moved to sixth table.");
                DiningPhilosophers.checkSixthTableDeadlock();
            }
        }
    }
}
