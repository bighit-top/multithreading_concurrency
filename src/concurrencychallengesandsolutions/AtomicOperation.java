package concurrencychallengesandsolutions;

import java.util.Random;

public class AtomicOperation {

    public static void main(String[] args) {
        Metrics metrics = new Metrics();

        Thread businessLogicThread1 = new BusinessLogic(metrics);
        Thread businessLogicThread2 = new BusinessLogic(metrics);

        Thread metricsPrinter = new MetricsPrinter(metrics);

        businessLogicThread1.start();
        businessLogicThread2.start();
        metricsPrinter.start();
    }

    // 원자적 메서드 호출 스레드
    public static class MetricsPrinter extends Thread {
        private Metrics metrics;

        public MetricsPrinter(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                }

                double currentAverage = metrics.getAverage();

                System.out.println("Current Average is " + currentAverage);
            }
        }
    }

    // 비원자적 메서드 호출 스레드
    public static class BusinessLogic extends Thread {
        private Metrics metrics;
        private Random random = new Random();

        public BusinessLogic(Metrics metrics) {
            this.metrics = metrics;
        }

        @Override
        public void run() {

            while (true) {
                long start = System.currentTimeMillis();

                try {
                    Thread.sleep(random.nextInt(10));
                } catch (InterruptedException e) {
                }

                long end = System.currentTimeMillis();

                metrics.addSample(end - start);
            }
        }
    }

    public static class Metrics {
        // primitive 중 long, double 은 원자적 연산이 아님 => volatile
        private volatile long count = 0;
        private volatile double average = 0.0;

        // critical section - 원자적 연산이 아님 => synchronized
        public synchronized void addSample(long sample) {
            double currentSum = average * count;
            count++;
            average = (currentSum + sample) / count;
        }

        // 원자적 연산에 해당 : reference get/set, primitive
        public double getAverage() {
            return average;
        }
    }
}
