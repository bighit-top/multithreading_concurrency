package threadcreation;

public class CreateThread2 {

    public static void main(String[] args) throws InterruptedException {
        Thread thread = new NewThread();
        thread.start();
    }

    // 2. Thread 클래스 상속으로 스레드를 생성
    // 클래스에 스레드를 확장한다는 로직을 넣은 것과 같고,
    // 현재 스레드와 직접적으로 관련된 많은 데이터와 메서드에 액세스 할 수 있음
    private static class NewThread extends Thread {
        @Override
        public void run() {
            System.out.println("Hello from " + this.getName());
        }
    }

}
