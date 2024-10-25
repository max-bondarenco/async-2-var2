import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

// Callable для підрахунку частоти слів у частині тексту
public class WordCountTask implements Callable<Map<String, Integer>> {
    private final String text;

    public WordCountTask(String text) {
        this.text = text;
    }

    @Override
    public Map<String, Integer> call() {
        String[] words = text.split("\\W+");
        Map<String, Integer> wordCount = new HashMap<>();

        for (String word : words) {
            word = word.toLowerCase();
            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
        }

        return wordCount;
    }
}
