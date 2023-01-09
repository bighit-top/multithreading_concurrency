package concurrencychallengesandsolutions;

import java.util.Random;

public class Deadlock {
    public static class Intersection {

        public static void main(String[] args) {
            Intersection intersection = new Intersection();

            Thread trainAThread = new Thread(new TrainA(intersection));
            Thread trainBThread = new Thread(new TrainB(intersection));

            trainAThread.start();
            trainBThread.start();
        }

        // B 기차
        public static class TrainB implements Runnable {
            private Intersection intersection;
            private Random random = new Random();

            private TrainB(Intersection intersection) {
                this.intersection = intersection;
            }

            @Override
            public void run() {
                while (true) {
                    long sleepingTime = random.nextInt(5);
                    try {
                        Thread.sleep(sleepingTime);
                    } catch (InterruptedException e) {
                    }

                    intersection.takeRoadB();
                }
            }
        }

        // A 기차
        public static class TrainA implements Runnable {
            private Intersection intersection;
            private Random random = new Random();

            private TrainA(Intersection intersection) {
                this.intersection = intersection;
            }

            @Override
            public void run() {
                while (true) {
                    long sleepingTime = random.nextInt(5);
                    try {
                        Thread.sleep(sleepingTime);
                    } catch (InterruptedException e) {
                    }

                    intersection.takeRoadA();
                }
            }
        }

        // 철도
        private Object roadA = new Object();
        private Object roadB = new Object();

        // a 가 사용중일 때 b는 대기하고, 충돌을 피함
//        public void takeRoadA() {
//            synchronized (roadA) {
//                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());
//
//                synchronized (roadB) {
//                    System.out.println("Train is passing through road A");
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }
//
//        // b 가 사용중일 때 a는 대기하고, 충돌을 피함
//        public void takeRoadB() {
//            synchronized (roadB) {
//                System.out.println("Road B is locked by thread " + Thread.currentThread().getName());
//
//                synchronized (roadA) {
//                    System.out.println("Train is passing through road B");
//                    try {
//                        Thread.sleep(1);
//                    } catch (InterruptedException e) {
//                    }
//                }
//            }
//        }

        // 순환 대기 방지로 deadlock 방지 : 공유 리소스 접근 순서를 동일하게 함
        public void takeRoadA() {
            synchronized (roadA) {
                System.out.println("Road A is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road A");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }

        // b 가 사용중일 때 a는 대기하고, 충돌을 피함
        public void takeRoadB() {
            synchronized (roadA) {
                System.out.println("Road B is locked by thread " + Thread.currentThread().getName());

                synchronized (roadB) {
                    System.out.println("Train is passing through road B");
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                    }
                }
            }
        }
    }
}
