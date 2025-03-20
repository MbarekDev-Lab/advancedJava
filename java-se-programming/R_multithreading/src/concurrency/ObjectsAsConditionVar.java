package concurrency;

import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.StringJoiner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Demonstrates a producer-consumer model using thread synchronization in Java.
 * This program reads matrices from a file, processes them using multiple threads,
 * and writes the multiplied result to an output file.
 */
public class ObjectsAsConditionVar {
    private static final String INPUT_FILE = "./out/matrices";
    private static final String OUTPUT_FILE = "./out/matrices_results.txt";
    private static final int N = 10;
    private static final Logger LOGGER = Logger.getLogger(ObjectsAsConditionVar.class.getName());

    public static void main(String[] args) {
        ThreadSafeQueue threadSafeQueue = new ThreadSafeQueue();

        try (FileReader fileReader = new FileReader(INPUT_FILE);
             FileWriter fileWriter = new FileWriter(OUTPUT_FILE)) {

            MatricesReaderProducer matricesReader = new MatricesReaderProducer(fileReader, threadSafeQueue);
            MatricesMultiplierConsumer matricesConsumer = new MatricesMultiplierConsumer(fileWriter, threadSafeQueue);

            matricesReader.start();
            matricesConsumer.start();

            matricesReader.join(); // Wait for producer to finish
            matricesConsumer.join(); // Wait for consumer to finish

        } catch (IOException | InterruptedException e) {
            LOGGER.log(Level.SEVERE, "Error occurred: ", e);
        }
    }

    /**
     * Consumer thread that retrieves matrices from the queue, multiplies them,
     * and writes the results to a file.
     */
    private static class MatricesMultiplierConsumer extends Thread {
        private final ThreadSafeQueue queue;
        private final FileWriter fileWriter;

        public MatricesMultiplierConsumer(FileWriter fileWriter, ThreadSafeQueue queue) {
            this.fileWriter = fileWriter;
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                while (true) {
                    MatricesPair matricesPair = queue.remove();
                    if (matricesPair == null) {
                        LOGGER.info("No more matrices to process. Consumer terminating.");
                        break;
                    }
                    float[][] result = multiplyMatrices(matricesPair.matrix1, matricesPair.matrix2);
                    saveMatrixToFile(fileWriter, result);
                }
            } catch (IOException e) {
                LOGGER.log(Level.SEVERE, "Error writing to file: ", e);
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    LOGGER.log(Level.WARNING, "Error closing file: ", e);
                }
            }
        }

        private float[][] multiplyMatrices(float[][] m1, float[][] m2) {
            float[][] result = new float[N][N];
            for (int r = 0; r < N; r++) {
                for (int c = 0; c < N; c++) {
                    for (int k = 0; k < N; k++) {
                        result[r][c] += m1[r][k] * m2[k][c];
                    }
                }
            }
            return result;
        }

        private static void saveMatrixToFile(FileWriter fileWriter, float[][] matrix) throws IOException {
            for (int r = 0; r < N; r++) {
                StringJoiner sj = new StringJoiner(", ");
                for (float value : matrix[r]) {
                    sj.add(String.format("%.2f", value));
                }
                fileWriter.write(sj.toString() + '\n');
            }
            fileWriter.write('\n');
        }
    }

    /**
     * Producer thread that reads matrices from a file and adds them to the queue.
     */
    private static class MatricesReaderProducer extends Thread {
        private final Scanner scanner;
        private final ThreadSafeQueue queue;

        public MatricesReaderProducer(FileReader reader, ThreadSafeQueue queue) {
            this.scanner = new Scanner(reader);
            this.queue = queue;
        }

        @Override
        public void run() {
            while (true) {
                float[][] matrix1 = readMatrix();
                float[][] matrix2 = readMatrix();
                if (matrix1 == null || matrix2 == null) {
                    queue.terminate();
                    LOGGER.info("No more matrices to read. Producer terminating.");
                    break;
                }
                queue.add(new MatricesPair(matrix1, matrix2));
            }
        }

        private float[][] readMatrix() {
            float[][] matrix = new float[N][N];
            for (int r = 0; r < N; r++) {
                if (!scanner.hasNextLine()) {
                    return null;
                }
                String[] line = scanner.nextLine().split(",");
                for (int c = 0; c < N; c++) {
                    matrix[r][c] = Float.parseFloat(line[c]);
                }
            }
            scanner.nextLine(); // Skip empty line
            return matrix;
        }
    }

    /**
     * A thread-safe queue for storing matrices, with synchronized methods for
     * adding and removing elements.
     */
    private static class ThreadSafeQueue {
        private final Queue<MatricesPair> queue = new LinkedList<>();
        private static final int CAPACITY = 5;
        private boolean isTerminate = false;

        public synchronized void add(MatricesPair matricesPair) {
            while (queue.size() == CAPACITY) {
                try {
                    wait();
                } catch (InterruptedException ignored) { }
            }
            queue.add(matricesPair);
            notify();
        }

        public synchronized MatricesPair remove() {
            while (queue.isEmpty() && !isTerminate) {
                try {
                    wait();
                } catch (InterruptedException ignored) { }
            }
            return queue.poll();
        }

        public synchronized void terminate() {
            isTerminate = true;
            notifyAll();
        }
    }

    /**
     * Data structure for storing two matrices.
     */
    private static class MatricesPair {
        public final float[][] matrix1;
        public final float[][] matrix2;

        public MatricesPair(float[][] matrix1, float[][] matrix2) {
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
        }
    }
}
