import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class StopWordLoader {
    public static Set<String> loadFromFile(File file) throws IOException {
        Set<String> stopWords = new TreeSet<>();
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                stopWords.add(scanner.nextLine().trim().toLowerCase());
            }
        }
        return stopWords;
    }
}
