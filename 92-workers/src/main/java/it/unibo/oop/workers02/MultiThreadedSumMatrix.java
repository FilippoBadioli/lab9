package it.unibo.oop.workers02;

import java.util.ArrayList;
import java.util.List;

public class MultiThreadedSumMatrix implements SumMatrix{

    private final int nThreads;

    MultiThreadedSumMatrix(int nThreads) {
        this.nThreads = nThreads;
    }

    private static class Worker extends Thread {

        private final int columns;
        private double res;
        private double[][] matrix;
        private final int currentRow;

        Worker(final double[][] matrix, int currentRow) {
            super();
            this.columns = matrix[0].length;
            this.matrix = matrix;
            this.currentRow = currentRow;
        }

        @Override
        public void run() {
            for(int j = 0; j < columns; j++) {
                res += matrix[currentRow][j];
            }
        }

        public double getResult() {
            return this.res;
        }
        
    }

    @Override
    public double sum(double[][] matrix) {

        final List<Worker> workers= new ArrayList<>();
        for(int i = 0; i < matrix.length; i++) {
            workers.add(new Worker(matrix, i));
        }
        for(final Worker w : workers) {
            w.start();
        }

        double sum = 0;
        for ( final Worker w : workers) {
            try{
                w.join();
                sum += w.getResult();
            } catch (InterruptedException e) {
                throw new IllegalStateException(e);
            }
        }
        return sum;
    }
    
}
