import org.w3c.dom.ls.LSOutput;

import java.lang.reflect.Executable;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private static final AtomicInteger countOf_3 = new AtomicInteger();
    private static final AtomicInteger countOf_4 = new AtomicInteger();
    private static final AtomicInteger countOf_5 = new AtomicInteger();
    private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    private static final int COUNT_OF_THREADS = 3;

    public static boolean isPalindrome(String text) {
        for (int i = 0; i < (text.length() / 2); i++) {
            if (text.charAt(i) == text.charAt(text.length() - i - 1)) {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static boolean isSingleLetter(String text) {
        if (text == null || text.equals("")) {
            return false;
        }
        char letter = text.charAt(0);
        for (int i = 0; i < text.length() - 1; i++) {
            if (text.charAt(i + 1) == letter) {
                continue;
            }
            else {
                return false;
            }
        }
        return true;
    }

    public static boolean isSorted(String text) {
        for (int i = 0; i < text.length() - 1; i++) {
            if (alphabet.indexOf(text.charAt(i)) != alphabet.indexOf(text.charAt(i)) + 1) {
                return false;
            }
        }
        return true;
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static String[] getText() {
        Random random = new Random();
        String[] texts = new String[100_000];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("abc", 3 + random.nextInt(3));
        }
        return texts;
    }

    public static void counter(int length) {
        switch (length) {
            case 3:
                countOf_3.getAndAdd(1);
                break;
            case 4:
                countOf_4.getAndAdd(1);
                break;
            case 5:
                countOf_5.getAndAdd(1);
            default:
                break;
        }
    }

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(3);

        executorService.execute(new Thread(() -> {
            String[] texts = getText();
            for (String text: texts) {
                if (isPalindrome(text)) {
                    counter(text.length());
                }
            }
        }));

        executorService.execute(new Thread(() -> {
            String[] texts = getText();
            for (String text: texts) {
                if (isSingleLetter(text)) {
                    counter(text.length());
                }
            }
        }));

        executorService.execute(new Thread(() -> {
            String[] texts = getText();
            for (String text: texts) {
                if (isSorted(text)) {
                    counter(text.length());
                }
            }
        }));

        executorService.awaitTermination(10, TimeUnit.SECONDS);
        executorService.shutdown();

        System.out.println("Красивых слов с длиной 3: " + countOf_3);
        System.out.println("Красивых слов с длиной 4: " + countOf_4);
        System.out.println("Красивых слов с длиной 5: " + countOf_5);
    }
}
