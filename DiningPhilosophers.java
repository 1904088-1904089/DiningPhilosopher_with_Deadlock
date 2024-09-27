public class DiningPhilosophers {
    public static void main(String[] args) {
        // Create tables and philosophers
        Table[] tables = new Table[5];
        Table sixthTable = new Table(6);  // Sixth table with no philosophers initially

        // Initialize 5 tables with 5 philosophers each
        for (int i = 0; i < 5; i++) {
            tables[i] = new Table(i);
            for (int j = 0; j < 5; j++) {
                Fork leftFork = tables[i].getLeftFork(j);
                Fork rightFork = tables[i].getRightFork(j);
                Philosopher philosopher = new Philosopher(Character.toString((char) ('A' + j + i * 5)), leftFork, rightFork, i, sixthTable);
                tables[i].addPhilosopher(philosopher);
            }
        }

        // Start philosopher threads
        for (Table table : tables) {
            table.startPhilosophers();
        }

        // Start deadlock detection threads
        for (Table table : tables) {
            new Thread(() -> table.detectDeadlock()).start();
        }

        // Run the simulation for 60 seconds
        try {
            Thread.sleep(60000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // Stop all philosophers
        for (Table table : tables) {
            table.stopPhilosophers();
        }

        System.out.println("Simulation ended.");
    }
}
