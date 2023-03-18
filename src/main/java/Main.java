import java.util.*;

public class Main {
    public static void main(String[] args) {

//    Time: 72195ms
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        ThreadGroup threadGroup = new ThreadGroup("group1");
        List<Thread> tasksByTheLongestInterval = new ArrayList<>();
        for (String text : texts) {
            tasksByTheLongestInterval.add(new Thread(threadGroup, () -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {               //1-30_000
                    for (int j = 0; j < text.length(); j++) {           //1-30_000
                        if (i >= j) {
                            continue;
                        }
                        boolean bFound = false;
                        for (int k = i; k < j; k++) {
                            if (text.charAt(k) == 'b') {
                                bFound = true;
                                break;
                            }
                        }
                        if (!bFound && maxSize < j - i) {
                            maxSize = j - i;
                        }
                    }
                }
                System.out.println(text.substring(0, 100) + " -> " + maxSize);
            }));
        }

        long startTs = System.currentTimeMillis(); // start time
        tasksByTheLongestInterval.forEach(Thread::start);
        while (threadGroup.activeCount() > 0) {
        }
        long endTs = System.currentTimeMillis();
        System.out.println("Time: " + (endTs - startTs) + "ms");
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }
}