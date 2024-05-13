package org.jolly.ch5;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CellularAutomata {
    private final Board mainBoard;
    private final CyclicBarrier barrier;
    private final Worker[] workers;

    public CellularAutomata(Board board) {
        this.mainBoard = board;
        int count = Runtime.getRuntime().availableProcessors();
        this.barrier = new CyclicBarrier(count, () -> mainBoard.commitNewValues());
        this.workers = new Worker[count];
        for (int i = 0; i < count; i++) {
            workers[i] = new Worker(mainBoard.getSubBoard(count, i));
        }
    }

    public void start() {
        for (Worker worker : workers) {
            new Thread(worker).start();
        }
        mainBoard.waitForConvergence();
    }

    private class Worker implements Runnable {
        private final Board board;

        private Worker(Board board) {
            this.board = board;
        }

        @Override
        public void run() {
            while (!board.hasConverged()) {
                for (int x = 0; x < board.getMaxX(); x++) {
                    for (int y = 0; y < board.getMaxY(); y++) {
                        board.setNewValue(x, y, computeValue(x, y));
                    }
                }
                try {
                    barrier.await();
                } catch (BrokenBarrierException e) {
                    return;
                } catch (InterruptedException e) {
                    return;
                }
            }
        }

        private static int[] computeValue(int x, int y) {
            return new int[]{};
        }
    }

    public interface Board {
        void commitNewValues();
        Board getSubBoard(int count, int workerId);
        boolean hasConverged();
        int getMaxX();
        int getMaxY();
        void setNewValue(int x, int y, int[] newValues);
        void waitForConvergence();
    }
}
