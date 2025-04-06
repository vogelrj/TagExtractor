import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TagFileWriter {
    public static void saveToFile(Map<String, Integer> tagMap, File file) throws IOException {
        try (PrintWriter writer = new PrintWriter(file)) {
            List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tagMap.entrySet());
            sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            for (Map.Entry<String, Integer> entry : sortedEntries) {
                writer.println(entry.getKey() + ": " + entry.getValue());
            }
        }
    }
}
