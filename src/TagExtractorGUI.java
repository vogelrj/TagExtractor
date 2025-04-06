import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Map.Entry;

public class TagExtractorGUI extends JFrame {
    private JTextArea textArea;
    private JLabel statusLabel;
    private File textFile;
    private File stopWordFile;
    private Set<String> stopWords;
    private Map<String, Integer> tagMap;
    private JScrollPane scrollPane;

    public TagExtractorGUI() {
        setTitle("Tag Extractor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 700);
        setLayout(new BorderLayout());

        initializeComponents();
        loadDefaultStopWords();
        setVisible(true);
    }

    private void loadDefaultStopWords() {
        stopWordFile = new File("data/stopwords.txt");
        if (stopWordFile.exists()) {
            try {
                stopWords = StopWordLoader.loadFromFile(stopWordFile);
                updateStatusBar();
            } catch (IOException e) {
                System.err.println("Failed to load default stop words file.");
            }
        }
    }

    private void initializeComponents() {
        JPanel topPanel = new JPanel();
        JButton loadTextButton = new JButton("Load Text File");
        JButton extractButton = new JButton("Extract Tags");
        JButton saveButton = new JButton("Save Output");

        statusLabel = new JLabel("No file loaded | Stop Words: 0 | Tags: 0");

        loadTextButton.addActionListener(this::loadTextFile);
        extractButton.addActionListener(this::extractTags);
        saveButton.addActionListener(this::saveOutput);

        topPanel.add(loadTextButton);
        topPanel.add(extractButton);
        topPanel.add(saveButton);

        add(topPanel, BorderLayout.NORTH);

        textArea = new JTextArea();
        textArea.setEditable(false);
        scrollPane = new JScrollPane(textArea);
        add(scrollPane, BorderLayout.CENTER);

        add(statusLabel, BorderLayout.SOUTH);
    }

    private void loadTextFile(ActionEvent e) {
        JFileChooser chooser = new JFileChooser(new File("data"));
        if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            textFile = chooser.getSelectedFile();
            setTitle("Tag Extractor - " + textFile.getName());
            updateStatusBar();
        }
    }

    private void extractTags(ActionEvent e) {
        if (textFile == null || stopWords == null) {
            JOptionPane.showMessageDialog(this, "Please load a text file. Stop words must be loaded from data/stopwords.txt.");
            return;
        }

        tagMap = TagProcessor.extractTags(textFile, stopWords);
        displayTags();
    }

    private void displayTags() {
        StringBuilder builder = new StringBuilder();
        List<Map.Entry<String, Integer>> sortedEntries = new ArrayList<>(tagMap.entrySet());
        sortedEntries.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        for (Map.Entry<String, Integer> entry : sortedEntries) {
            builder.append(entry.getKey()).append(" : ").append(entry.getValue()).append("\n");
        }
        textArea.setText(builder.toString());
        textArea.setCaretPosition(0); // Scroll to top
        updateStatusBar();
    }

    private void saveOutput(ActionEvent e) {
        if (tagMap == null || tagMap.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No tags to save. Please extract first.");
            return;
        }

        JFileChooser chooser = new JFileChooser(new File("data"));

        // Generate default file name using source filename and timestamp
        if (textFile != null) {
            String baseName = textFile.getName().replaceAll("\\.txt$", "").replaceAll("[^a-zA-Z0-9_-]", "_");
            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String suggestedName = String.format("extracted_tags_%s_%s.txt", baseName, timestamp);
            chooser.setSelectedFile(new File(chooser.getCurrentDirectory(), suggestedName));
        }

        if (chooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            File saveFile = chooser.getSelectedFile();
            try {
                TagFileWriter.saveToFile(tagMap, saveFile);
                JOptionPane.showMessageDialog(this, "Tags saved successfully.");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Failed to save file.");
            }
        }
    }

    private void updateStatusBar() {
        String fileName = (textFile != null) ? textFile.getName() : "No file loaded";
        int stopWordCount = (stopWords != null) ? stopWords.size() : 0;
        int tagCount = (tagMap != null) ? tagMap.size() : 0;
        statusLabel.setText(String.format("%s | Stop Words: %d | Tags: %d", fileName, stopWordCount, tagCount));
    }
}
