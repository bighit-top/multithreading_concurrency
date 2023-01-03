package threadcreation;

public class CreateThread {

    public static void main(String[] args) throws InterruptedException {

        // 1. Runnable 인터페이스 구현해서 Thread 생성
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("We are now in thread " + Thread.currentThread().getName());
                // priority 확인
                System.out.println("Current thread priority is " + Thread.currentThread().getPriority());
            }
        });

        // 스레드 이름 지정
        thread.setName("New Worker Thread");

        // 스레드의 동적 우선순위 설정: Max = 10
        thread.setPriority(Thread.MAX_PRIORITY);

        // 메서드 호출 전 현재 실행중인 스레드를 출력하려면
        // Thread.currentThread() 정적 메서드를 호출해서
        // 현재 스레드의 스레드 객체를 알아내야함
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " before starting a new thread");

        // Thread 시작 : JVM이 새 스레드를 생성해 운영체제에 전달함
        thread.start();
        System.out.println("We are in thread: " + Thread.currentThread().getName() + " after starting a new thread");

        // 현재 스레드를 주어진 밀리초만큼 멈춤
        // sleep 메서드는 반복 명령문이 아니라 OS에 지시하는 것
        Thread.sleep(10000);
    }

}
