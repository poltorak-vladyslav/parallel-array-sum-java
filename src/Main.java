import java.util.Arrays;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class Main {

    public static void main(String[] args) {
        int arraySize = 500000;
        int numberOfThreads = 5;

        // Ініціалізація масиву зі значеннями 1
        int[] array = new int[arraySize];
        Arrays.fill(array, 1);

        ForkJoinPool pool = new ForkJoinPool(numberOfThreads);
        SumTask task = new SumTask(array, 0, array.length);
        Long result = pool.invoke(task);
        System.out.println("Загальна сума: " + result);
    }

    static class SumTask extends RecursiveTask<Long> {

        private final int[] array;
        private final int start;
        private final int end;

        public SumTask(int[] array, int start, int end) {
            this.array = array;
            this.start = start;
            this.end = end;
        }

        @Override
        protected Long compute() {
            if (end - start <= 10) {
                // Якщо розмір масиву менший за 10 - виконується стандартний пошук суми
                long sum = 0;
                for (int i = start; i < end; i++) {
                    sum += array[i];
                }
                return sum;
            } else {
                // Якщо розмір масиву більший за 10 - завдання розділяється на підзавдання,
                // які виконуються паралельно
                int mid = (start + end) / 2;
                SumTask left = new SumTask(array, start, mid);
                SumTask right = new SumTask(array, mid, end);
                left.fork();
                Long rightResult = right.compute();
                Long leftResult = left.join();
                return leftResult + rightResult;
            }
        }
    }
}