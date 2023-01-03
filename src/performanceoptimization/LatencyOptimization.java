package performanceoptimization;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LatencyOptimization {

    public static final String SOURCE_FILE = "./resources/many-flowers.jpg";
    public static final String DESTINATION_FILE = "./out/many-flowers.jpg";

    public static void main(String[] args) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File(SOURCE_FILE));
        BufferedImage resultImage = new BufferedImage(originalImage.getWidth(), originalImage.getHeight(), BufferedImage.TYPE_INT_RGB);

        long startTime = System.currentTimeMillis();

//        recolorSingleThreaded(originalImage, resultImage);

        int numberOfThreads = 6;
        recolorMultithreaded(originalImage, resultImage, numberOfThreads);

        long endTime = System.currentTimeMillis();

        long duration = endTime - startTime;

        File outputFile = new File(DESTINATION_FILE);
        ImageIO.write(resultImage, "jpg", outputFile);

        System.out.println(String.valueOf(duration));
    }

    // 원본 이미지와 결과 이미지를 가짐
    public static void recolorMultithreaded(BufferedImage originalImage, BufferedImage resultImage, int numberOfThreads) {
        List<Thread> threads = new ArrayList<>();
        int width = originalImage.getWidth();
        int height = originalImage.getHeight() / numberOfThreads;

        for (int i = 0; i < numberOfThreads; i++) {
            final int threadMultiplier = i;
            Thread thread  = new Thread(() -> {
                int leftCorner = 0;
                int topCorner = height * threadMultiplier;

                recolorImage(originalImage, resultImage, leftCorner, topCorner, width, height);
            });
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.start();
        }

        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    // 왼쪽 상단부터 색상을 칠함
    public static void recolorSingleThreaded(BufferedImage originalImage, BufferedImage resultImage) {
        recolorImage(originalImage, resultImage, 0, 0, originalImage.getWidth(), originalImage.getHeight());
    }

    // 전체 이미지를 반복하고 recoloring을 적용
    public static void recolorImage(BufferedImage originalImage, BufferedImage resultImage,
                                    int leftCorner, int topCorner,
                                    int width, int height) {
        for (int x = leftCorner; x < leftCorner + width && x < originalImage.getWidth(); x++) {
            for (int y = topCorner; y < topCorner + height && y < originalImage.getHeight(); y++) {
                recolorPixel(originalImage, resultImage, x, y);
            }
        }
    }

    // 개별 픽셀 색상을 다시 칠하는 메서드
    public static void recolorPixel(BufferedImage originalImage, BufferedImage resultImage, int x, int y) {
        int rgb = originalImage.getRGB(x, y);

        // 픽셀 RGB 값 분리
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);

        // 결과 이미지에서 새로운 색상 값을 지니는 변수
        int newRed;
        int newGreen;
        int newBlue;

        // 회색계열 확인 해서 새로운 색상 값 부여
        if (isShadeOfGray(red, green, blue)) {
            newRed = Math.min(255, red + 10);
            newGreen = Math.max(0, green - 80);
            newBlue = Math.max(0, blue - 20);
        } else {
            newRed = red;
            newGreen = green;
            newBlue = blue;
        }

        // 결정된 값을 하나로 합침
        int newRGB = createRGBFromColors(newRed, newGreen, newBlue);
        setRGB(resultImage, x, y, newRGB);
    }

    // 생성한 RGB 값을 BufferedImage 객체에 할당
    public static void setRGB(BufferedImage image, int x, int y, int rgb) {
        image.getRaster().setDataElements(x, y, image.getColorModel().getDataElements(rgb, null));
    }

    // 픽셀의 특정 색상 값을 취하고 픽셀에 넣을 회색을 결정 : 모든 컴포넌트가 같은 색상 강도를 갖는지 확
    public static boolean isShadeOfGray(int red, int green, int blue) {

        // 특정 컴포넌트가 나머지 보다 강하지 않으면 red, green, blue 가 균등하게 섞여있는 경우 회색
        // 색상 간의 근접성을 확인하기 위해 임의로 거리를 지정함 - 30
        return Math.abs(red - green) < 30 && Math.abs(red - blue) < 30 && Math.abs(green - blue) < 30;

    }
    // RGB의 개별 값을 합쳐서 픽셀에 RGB 값을 넣음
    public static int createRGBFromColors(int red, int green, int blue) {
        int rgb = 0;

        rgb |= blue;
        rgb |= green << 8;
        rgb |= red << 16;

        // 픽셀을 불투명 하게 만듬 : Alpha 값을 최대로 설정 - 16진수에서 255
        rgb |= 0xFF000000;

        return rgb;
    }

    // RGB 중 빨강만 픽셀에서 추출
    public static int getRed(int rgb) {
        return (rgb & 0x00FF0000) >> 16;
    }

    // RGB 중 초록만 픽셀에서 추출
    public static int getGreen(int rgb) {
        return (rgb & 0x0000FF00) >> 8; // 왼쪽으로 8비트 이동
    }

    // RGB 중 파랑만 픽셀에서 추출
    public static int getBlue(int rgb) {
        // 픽셀에 비트마스크 적용해서 컴포넌트를 모두 0으로 설정하고 0x000000FF 를 적용
        return rgb & 0x000000FF;
    }

}
