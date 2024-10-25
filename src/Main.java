import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        String[] parts = getStrings();
        Map<String, Integer> wordCountMap = process(parts);
        wordCountMap.forEach((word, count) -> System.out.println(word + ": " + count));
    }

    private static String[] getStrings() {
        String text = "In a small town, there lived a wise old man. The townspeople often visited him to seek advice on various matters. The old man always spoke with kindness and wisdom, making sure everyone felt welcome. \"Remember,\" he would say, \"patience is key to finding answers.\" The townspeople valued his words and shared them with others. They would often gather around him, eager to hear his stories and life lessons. One day, a young girl asked, \"What is the secret to happiness?\" The old man smiled and replied, \"Happiness is found in the little things, like friendship and laughter.\" The townspeople nodded in agreement, realizing the truth in his words. They left feeling uplifted and inspired.";
        return text.split("\\. ");
    }

    private static ConcurrentHashMap<String, Integer> process(String[] parts) {
        // ConcurrentHashMap для збереження результатів
        ConcurrentHashMap<String, Integer> wordCountMap = new ConcurrentHashMap<>();

        // Пул потоків, де один потік обробляє одне речення тексту
        try (ExecutorService executor = Executors.newFixedThreadPool(parts.length)) {
            List<Future<Map<String, Integer>>> futures = new ArrayList<>();

            // Кожне речення передаємо в Callable WordCountTask
            for (String part : parts) {
                Future<Map<String, Integer>> future = executor.submit(new WordCountTask(part));
                futures.add(future);
            }

            // Обробимо результати
            for (Future<Map<String, Integer>> future : futures) {
                Map<String, Integer> wordCount = future.get();
                wordCount.forEach((String word, Integer count) -> {
                    wordCountMap.put(word, wordCountMap.getOrDefault(word,0) + count);
                } );
            }

            // Закриваємо пул потоків
            executor.shutdown();
        } catch (InterruptedException ie) {
            System.out.println("One of the threads was interrupted");
        } catch (ExecutionException ee) {
            System.out.println("Computation in one of the threads invoked exception");
        }

        return wordCountMap;
    }
}
