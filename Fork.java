<<<<<<< HEAD

import java.util.concurrent.Semaphore;

public class Fork {
    private final Semaphore semaphore = new Semaphore(1);

    public boolean pickUp() {
        return semaphore.tryAcquire();
    }

    public void putDown() {
        semaphore.release();
    }
}
=======
import java.util.concurrent.locks.ReentrantLock;

public class Fork extends ReentrantLock {
    // Fork is just a ReentrantLock that philosophers use to synchronize
}
>>>>>>> 70b9bb5 (Update Table class)
