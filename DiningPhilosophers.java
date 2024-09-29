public class DiningPhilosophers {
    public static void main(String[] args) {
        Table[] tables = new Table[6];
        for (int i = 0; i < 5; i++) {
            tables[i] = new Table(false);
        }
        tables[5] = new Table(true);  // Sixth table

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Fork leftFork = tables[i].getFork(j);
                Fork rightFork = tables[i].getFork((j + 1) % 5);
                Philosopher philosopher = new Philosopher((i * 5) + j, leftFork, rightFork, tables[i]);
                tables[i].addPhilosopher(philosopher);
            }
        }

        // Start the simulation
        for (int i = 0; i < 5; i++) {
            tables[i].startPhilosophers();
        }
    }
}
