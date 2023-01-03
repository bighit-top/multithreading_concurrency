package threadcreation;

public class ThreadException {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                throw new RuntimeException("Intentional Exception");
            }
        });

        // Exception 처리: 전체 스레드에 해당되는 예외 핸들러 지정
        thread.setUncaughtExceptionHandler((t, e) ->
                System.out.println("A critical error happened in thread " + t.getName()
                        + "the error is " + e.getMessage()));

        thread.start();
    }

}
