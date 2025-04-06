import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class TagFrequencyCounter {
    public static Map<String, Integer> countFrequencies(List<String> words, Set<String> stopWords) {
        Map<String, Integer> frequencyMap = new TreeMap<>();
        for (String word : words) {
            if (!stopWords.contains(word)) {
                frequencyMap.put(word, frequencyMap.getOrDefault(word, 0) + 1);
            }
        }
        return frequencyMap;
    }
}
