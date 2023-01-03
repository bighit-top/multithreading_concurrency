package threadcoordination;

import java.math.BigInteger;

public class InterruptThread {
    public static void main(String[] args) throws InterruptedException {
        // 1. exception 발생하는 경우 조작
        Thread thread1 = new Thread(new BlockingTask());
        thread1.start();
        // 중단: InterruptedException 발생
        thread1.interrupt();

        // 2. 명시적 처리: 처리 로직으로 조작
        Thread thread2 = new Thread(new LongComputationTask(new BigInteger("20000"), new BigInteger("1000000")));
//        thread2.setDaemon(true); // Daemon thread
        thread2.start();
        Thread.sleep(100);
        thread2.interrupt();

    }

    // 1: thread.interrupt() 호출로 InterruptedException 발생
    private static class BlockingTask implements Runnable {
        @Override
        public void run() {
            try {
                Thread.sleep(500000);
            // 현재 스레드가 외부에서 인터럽트되면 예외 처리가 발생함
            } catch (InterruptedException e) {
                System.out.println("Exiting blocking thread");
            }
        }
    }

    // 2: thread.interrupt() 호출로도 thread 중단 안됨. 처리할 메서드나 로직이 없기 때문
    // 코드내에 시간이 오래 걸리는 핫스팟을 찾아 외부에서 인터럽트 당했는지 확인하는 각각의 반복문에 if 문을 추가
    private static class LongComputationTask implements Runnable {

        private BigInteger base;
        private BigInteger power;

        public LongComputationTask(BigInteger base, BigInteger power) {
            this.base = base;
            this.power = power;
        }

        @Override
        public void run() {
            System.out.println(base + "^" + power + " = " + pow(base, power));
        }

        private BigInteger pow(BigInteger base, BigInteger power) {
            BigInteger result = BigInteger.ONE;

            // 핫스팟
            for (BigInteger i = BigInteger.ZERO; i.compareTo(power) != 0; i = i.add(BigInteger.ONE)) {
                // if 조건문을 추가해서 interrupted 상태 확인해서 처리
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Prematurely interrupted computation");
                    return BigInteger.ZERO;
                }
                result = result.multiply(base);
            }
            return result;
        }
    }

}
