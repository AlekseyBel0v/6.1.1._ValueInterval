import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws InterruptedException, ExecutionException {

//    Time: 72195ms
        String[] texts = new String[25];
        for (int i = 0; i < texts.length; i++) {
            texts[i] = generateText("aab", 30_000);
        }
        List<Callable <Integer>> listByTheLongestInterval = new ArrayList<>();
        for (String text : texts) {
            listByTheLongestInterval.add(() -> {
                int maxSize = 0;
                for (int i = 0; i < text.length(); i++) {
                    for (int j = 0; j < text.length(); j++) {
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
                return maxSize;
            });
        }

        final ExecutorService threadPool = Executors.newFixedThreadPool(25);
        long startTs = System.currentTimeMillis(); // start time
        List<Future<Integer>> futureList = threadPool.invokeAll(listByTheLongestInterval);
        List<Integer> listMax = new ArrayList<>();
        for (Future<Integer> future : futureList) {
            listMax.add(future.get());
        }
        int max = listMax.stream().max(Integer::compareTo).get();
        long endTs = System.currentTimeMillis();
        System.out.println("Time: " + (endTs - startTs) + "ms");
        System.out.println("Max value = " + max);
        threadPool.awaitTermination(10, TimeUnit.SECONDS);      //Почему программа не завершается?
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