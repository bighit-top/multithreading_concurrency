package threadcoordination;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreadCoordination {

    public static void main(String[] args) throws InterruptedException {
        List<Long> inputNumbers = Arrays.asList(10000000L, 3435L, 35435L, 2324L, 4656L, 23L, 2435L, 5566L);

        List<FactorialThread> threads = new ArrayList<>();

        for (long inputNumber : inputNumbers) {
            threads.add(new FactorialThread(inputNumber));
        }

        // 스레드를 실행하는 시점
        for (Thread thread : threads) {
            thread.start();
        }

        // 스레드를 실행하는 시점과, 스레드를 확인하는 시점 사이에 경쟁 조건(race condition)이 생김
        // thread.join() 메서드로 factorial 스레드가 작업을 마칠 때까지 main 스레드가 기다리도록 함
        // join 메서드는 스레드가 종료되어야만 반환되므로
        // main 스레드가 루프를 완료할 때까지 모든 factorial 스레드가 무조건 작업을 마침
        for (Thread thread : threads) {

//            thread.join();

            // 2초가 지나도 스레드가 종료되지 않으면 thread.join() 메서드가 반환
            thread.join(2000);
        }

        // 스레드를 확인하는 시점
        for (int i = 0; i <inputNumbers.size(); i++) {
            FactorialThread factorialThread = threads.get(i);
            if (factorialThread.isFinished()) {
                System.out.println("Factorial of " + inputNumbers.get(i) + " is " + factorialThread.getResult());
            } else {
                System.out.println("The calculation for " + inputNumbers.get(i) + " is still in progress");
            }
        }
    }

    // FactorialThread 스레드에
    // isFinished() 와 getResult() 메서드를 만들어
    // 메인스레드(외부)에서 스레드의 상태를 확인할 수 있도록 한다
    public static class FactorialThread extends Thread {
        private long inputNumber;
        private BigInteger result = BigInteger.ZERO;
        private boolean isFinished = false;

        public FactorialThread(long inputNumber) {
            this.inputNumber = inputNumber;
        }

        @Override
        public void run() {
            this.result = factorial(inputNumber);
            this.isFinished = true;
        }

        private BigInteger factorial(long n) {
            BigInteger tempResult = BigInteger.ONE;

            for (long i = n; i > 0; i--) {
                tempResult = tempResult.multiply(new BigInteger(Long.toString(i)));
            }
            return tempResult;
        }

        public boolean isFinished() {
            return isFinished;
        }

        public BigInteger getResult() {
            return result;
        }
    }
}
