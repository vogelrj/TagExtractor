import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TagProcessor {
    public static Map<String, Integer> extractTags(File file, Set<String> stopWords) {
        List<String> words = TagReader.readWordsFromFile(file);
        return TagFrequencyCounter.countFrequencies(words, stopWords);
    }
}
