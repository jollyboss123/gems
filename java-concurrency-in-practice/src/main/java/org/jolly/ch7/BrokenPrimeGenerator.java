package org.jolly.ch7;

import net.jcip.annotations.NotThreadSafe;
import net.jcip.annotations.ThreadSafe;

import java.math.BigInteger;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

@NotThreadSafe
public class BrokenPrimeGenerator extends Thread {
    private final BlockingQueue<BigInteger> queue;
    private volatile boolean cancelled = false;

    public BrokenPrimeGenerator(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!cancelled) {
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException consumed) {
        }
    }

    public void cancel() {
        cancelled = true;
    }
}

@ThreadSafe
class PrimeProducer extends Thread {
    private final BlockingQueue<BigInteger> queue;

    PrimeProducer(BlockingQueue<BigInteger> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            BigInteger p = BigInteger.ONE;
            while (!Thread.currentThread().isInterrupted()) {
                queue.put(p = p.nextProbablePrime());
            }
        } catch (InterruptedException consumed) {
            // allow thread to exit
        }
    }

    public void cancel() {
        interrupt();
    }
}

class TestBrokenPrimeGenerator {
    private static final int MAX_PRIME_COUNT = 99;
    private static int primeCount = 0;

    public static void main(String[] args) throws InterruptedException {
        consumePrimes();
    }

    static void consumePrimes() throws InterruptedException {
        BlockingQueue<BigInteger> primes = new ArrayBlockingQueue<>(100);
        BrokenPrimeGenerator producer = new BrokenPrimeGenerator(primes);
        producer.start();
        try {
            while (needMorePrimes()) {
                consume(primes.take());
            }
        } finally {
            producer.cancel();
        }
    }

    static boolean needMorePrimes() throws InterruptedException {
        return primeCount < MAX_PRIME_COUNT;
    }

    static void consume(BigInteger p) {
        System.out.println(p);
        primeCount++;
    }
}
