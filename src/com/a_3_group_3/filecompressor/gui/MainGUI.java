package com.a_3_group_3.filecompressor.gui;

import com.a_3_group_3.filecompressor.analysis.ByteFrequencyCounter;
import com.a_3_group_3.filecompressor.compression.*;
import com.a_3_group_3.filecompressor.io.FileReaderUtil;
import com.a_3_group_3.filecompressor.utils.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * FIXED VERSION: Consolidated Single-File GUI for Huffman Compression.
 * Corrected SwingWorker implementation and automated output path.
 */
public class MainGUI extends JFrame {

    private JTextField inputField;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JCheckBox parallelToggle;
    private JLabel originalSizeLabel, compressedSizeLabel, ratioLabel;
    private JButton compressBtn, decompressBtn;

    // Session-based tree storage (required for decompression as backend does not
    // store tree in file)
    private static HuffmanNode lastRootNode;

    private static final Color BACK_COLOR = new Color(33, 37, 43);
    private static final Color CARD_COLOR = new Color(40, 44, 52);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    private static final Color TEXT_COLOR = new Color(171, 178, 191);
    private static final Color SUCCESS_COLOR = new Color(152, 195, 121);

    // ROUNDNESS FOR BUTTONS ONLY
    private static final int BUTTON_ARC = 15;

    public MainGUI() {
        setTitle("BytePress");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(750, 550));
        getContentPane().setBackground(BACK_COLOR);
        setLayout(new BorderLayout());

        setupHeader();
        setupMainContent();
        setupDragAndDrop();

        pack();
        setLocationRelativeTo(null);
    }

    private void setupHeader() {
        JPanel header = new JPanel();
        header.setBackground(BACK_COLOR);
        header.setBorder(new EmptyBorder(25, 20, 15, 20));
        JLabel title = new JLabel("BytePress");
        title.setFont(new Font("Segoe UI", Font.BOLD, 26));
        title.setForeground(ACCENT_COLOR);
        header.add(title);
        add(header, BorderLayout.NORTH);
    }

    private void setupMainContent() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(BACK_COLOR);
        mainPanel.setBorder(new EmptyBorder(0, 30, 20, 30));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 5, 10, 5);

        // Input Selector
        JLabel inputLabel = new JLabel("Input File:");
        inputLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        mainPanel.add(inputLabel, gbc);

        inputField = new JTextField();
        inputField.setBackground(CARD_COLOR);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        mainPanel.add(inputField, gbc);

        JButton browseBtn = createStyledButton("Browse", new Color(72, 77, 86));
        browseBtn.addActionListener(e -> chooseFile());
        gbc.gridx = 2;
        gbc.weightx = 0;
        mainPanel.add(browseBtn, gbc);

        // Options
        parallelToggle = new JCheckBox("Use Parallel Encoding (Multi-core)");
        parallelToggle.setForeground(TEXT_COLOR);
        parallelToggle.setBackground(BACK_COLOR);
        gbc.gridx = 1;
        gbc.gridy = 1;
        mainPanel.add(parallelToggle, gbc);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);
        originalSizeLabel = createStatLabel("Original: -");
        compressedSizeLabel = createStatLabel("Compressed: -");
        ratioLabel = createStatLabel("Ratio: -");
        statsPanel.add(originalSizeLabel);
        statsPanel.add(compressedSizeLabel);
        statsPanel.add(ratioLabel);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        mainPanel.add(statsPanel, gbc);

        // Progress
        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBackground(CARD_COLOR);
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBorderPainted(false);
        gbc.gridy = 3;
        mainPanel.add(progressBar, gbc);

        // Log
        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);
        logArea.setBackground(CARD_COLOR);
        logArea.setForeground(TEXT_COLOR);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setBorder(BorderFactory.createLineBorder(CARD_COLOR.brighter()));
        gbc.gridy = 4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollLog, gbc);

        // Actions
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        actionPanel.setOpaque(false);
        compressBtn = createStyledButton("COMPRESS", SUCCESS_COLOR);
        decompressBtn = createStyledButton("DECOMPRESS", ACCENT_COLOR);
        compressBtn.addActionListener(e -> processFile(true));
        decompressBtn.addActionListener(e -> processFile(false));
        actionPanel.add(compressBtn);
        actionPanel.add(decompressBtn);
        gbc.gridy = 5;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        mainPanel.add(actionPanel, gbc);

        add(mainPanel, BorderLayout.CENTER);
    }

    private JButton createStyledButton(String t, Color bg) {
        JButton b = new JButton(t) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), BUTTON_ARC, BUTTON_ARC);
                FontMetrics fm = g2.getFontMetrics();
                int x = (getWidth() - fm.stringWidth(getText())) / 2;
                int y = (getHeight() + fm.getAscent()) / 2 - 2;
                g2.setColor(getForeground());
                g2.drawString(getText(), x, y);
                g2.dispose();
            }
        };
        b.setBackground(bg);

        // Improve contrast: Use dark text for light backgrounds (Compress/Decompress)
        // and white text for darker backgrounds (Browse)
        if (bg.getRed() + bg.getGreen() + bg.getBlue() > 400) {
            b.setForeground(new Color(20, 20, 20)); // High contrast dark text
        } else {
            b.setForeground(Color.WHITE);
        }

        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Force the button to show the background color correctly
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setBorderPainted(false);
        b.setPreferredSize(new Dimension(180, 40));

        return b;
    }

    private JLabel createStatLabel(String t) {
        JLabel l = new JLabel(t);
        l.setForeground(TEXT_COLOR);
        l.setHorizontalAlignment(SwingConstants.CENTER);
        l.setOpaque(true);
        l.setBackground(CARD_COLOR);
        l.setBorder(new EmptyBorder(15, 10, 15, 10));
        return l;
    }

    private void chooseFile() {
        JFileChooser jfc = new JFileChooser();
        if (jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            inputField.setText(jfc.getSelectedFile().getAbsolutePath());
            log("Selected file: " + jfc.getSelectedFile().getName());
        }
    }

    private void log(String m) {
        logArea.append(m + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void processFile(boolean compress) {
        String inputPath = inputField.getText();
        if (inputPath.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please select an input file first.");
            return;
        }

        File inputFile = new File(inputPath);
        if (!inputFile.exists()) {
            JOptionPane.showMessageDialog(this, "The selected file does not exist.");
            return;
        }

        String outputPath;
        if (compress) {
            // Standard practice: append .huff to original name (e.g., sample.txt ->
            // sample.txt.huff)
            outputPath = inputPath + ".huff";
        } else {
            // Requirement: Only accept .huff files for decompression
            if (!inputPath.toLowerCase().endsWith(".huff")) {
                JOptionPane.showMessageDialog(this, "Decompression Error: Please select a '.huff' file.",
                        "Invalid File Type", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // User Specific Requirement: Use .txt.decoded.txt as filename
            outputPath = inputPath.substring(0, inputPath.length() - 5) + ".decoded.txt";
        }

        setUIEnabled(false);
        progressBar.setValue(0);
        log("--- Starting " + (compress ? "COMPRESSION" : "DECOMPRESSION") + " ---");

        new SwingWorker<Void, Object[]>() {
            @Override
            protected Void doInBackground() throws Exception {
                if (compress) {
                    publish(new Object[] { 5, "--- TECHNICAL ANALYSIS ---" });
                    publish(new Object[] { 10, "Reading file: " + inputPath });
                    byte[] data = FileReaderUtil.readFile(inputPath);
                    if (data.length == 0)
                        throw new Exception("File is empty.");

                    publish(new Object[] { 20, "Byte Frequency Analysis: Calculating..." });
                    int[] freq = ByteFrequencyCounter.countFrequencies(data);

                    // Display FULL Frequency Map
                    publish(new Object[] { 25, "--- FULL FREQUENCY MAP (Non-zero counts) ---" });
                    for (int i = 0; i < freq.length; i++) {
                        if (freq[i] > 0) {
                            publish(new Object[] { 25,
                                    " [Byte " + String.format("%3d", i) + "] Frequency: " + freq[i] });
                        }
                    }

                    publish(new Object[] { 30, "Huffman Tree: Building nodes..." });
                    lastRootNode = HuffmanTreeBuilder.buildTree(freq);
                    publish(new Object[] { 35, "Tree Root Total Frequency: " + lastRootNode.frequency });

                    publish(new Object[] { 40, "Generating Huffman Codes..." });
                    Map<Integer, String> codes = HuffmanCodeGenerator.generateCodes(lastRootNode);

                    // Display ALL Codes
                    publish(new Object[] { 45,
                            "--- FULL HUFFMAN CODES TABLE (" + codes.size() + " unique bytes) ---" });
                    for (Map.Entry<Integer, String> entry : codes.entrySet()) {
                        publish(new Object[] { 45, " [Byte " + String.format("%3d", entry.getKey()) + "] Binary Code: "
                                + entry.getValue() });
                    }

                    int threads = Runtime.getRuntime().availableProcessors();
                    publish(new Object[] { 60, "--- EXHAUSTIVE MULTITASKING & THREADING INFO ---" });
                    publish(new Object[] { 60, "System Architecture: " + threads + " Logical Processors detected." });
                    publish(new Object[] { 60, "Execution Mode: "
                            + (parallelToggle.isSelected() ? "PARALLEL (Thread Pool)" : "SEQUENTIAL") });

                    if (parallelToggle.isSelected()) {
                        int chunkSize = data.length / threads;
                        publish(new Object[] { 65, "Task Distribution Strategy (ParallelHuffmanEncoder Simulation):" });
                        for (int i = 0; i < threads; i++) {
                            int start = i * chunkSize;
                            int end = (i == threads - 1) ? data.length : (i + 1) * chunkSize;
                            publish(new Object[] { 65, " > Thread [" + i + "] assigned chunk: " + start + " to " + end
                                    + " (" + (end - start) + " bytes)" });
                        }
                        publish(new Object[] { 65, "Status: Spawning " + threads
                                + " worker threads to process chunks simultaneously..." });
                    } else {
                        publish(new Object[] { 65,
                                "Status: Processing all " + data.length + " bytes on the main worker thread..." });
                    }

                    publish(new Object[] { 70, "Encoding data bitstream..." });
                    long startTime = System.currentTimeMillis();
                    String encoded = parallelToggle.isSelected() ? ParallelHuffmanEncoder.parallelEncode(data, codes)
                            : HuffmanEncoder.encode(data, codes);
                    long duration = System.currentTimeMillis() - startTime;

                    publish(new Object[] { 80, "Encoding Result Metrics:" });
                    publish(new Object[] { 80, " - Total Bits Generated: " + encoded.length() });
                    publish(new Object[] { 80, " - Processing Time: " + duration + "ms" });
                    publish(new Object[] { 80, " - Average Throughput: "
                            + String.format("%.2f", (data.length / (duration / 1000.0 + 0.001)) / 1024.0) + " KB/s" });

                    publish(new Object[] { 90, "Writing bitstream to disk via BitWriter..." });
                    BitWriter.writeCompressedFile(encoded, outputPath);

                    long origSize = data.length;
                    long compSize = new File(outputPath).length();
                    double ratio = ((double) (origSize - compSize) / origSize) * 100;

                    SwingUtilities.invokeLater(() -> {
                        originalSizeLabel.setText("Original: " + origSize + " B");
                        compressedSizeLabel.setText("Compressed: " + compSize + " B");
                        ratioLabel.setText("Ratio: " + String.format("%.2f", ratio) + "%");
                    });
                } else {
                    if (lastRootNode == null) {
                        throw new Exception(
                                "Error: Huffman Tree missing for decompression.\nNote: Because the backend does not store the tree in the file, you must compress a file in this same session before decompressing.");
                    }
                    publish(new Object[] { 50, "Decoding file using stored tree..." });
                    HuffmanDecoder.decodeFile(inputPath, lastRootNode, outputPath);
                }
                return null;
            }

            @Override
            protected void process(List<Object[]> chunks) {
                for (Object[] chunk : chunks) {
                    progressBar.setValue((Integer) chunk[0]);
                    log((String) chunk[1]);
                }
            }

            @Override
            protected void done() {
                try {
                    get();
                    progressBar.setValue(100);
                    log("SUCCESS: " + (compress ? "Compressed" : "Decompressed") + " file saved successfully.");
                    JOptionPane.showMessageDialog(MainGUI.this, "Success! File saved to:\n" + outputPath);
                } catch (Exception e) {
                    log("FAILED: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()));
                    JOptionPane.showMessageDialog(MainGUI.this,
                            "Error: " + (e.getCause() != null ? e.getCause().getMessage() : e.getMessage()),
                            "Task Failed", JOptionPane.ERROR_MESSAGE);
                }
                setUIEnabled(true);
            }
        }.execute();
    }

    private void setUIEnabled(boolean e) {
        compressBtn.setEnabled(e);
        decompressBtn.setEnabled(e);
        inputField.setEnabled(e);
        parallelToggle.setEnabled(e);
    }

    private void setupDragAndDrop() {
        new DropTarget(this, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> files = (List<File>) dtde.getTransferable()
                            .getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) {
                        inputField.setText(files.get(0).getAbsolutePath());
                        log("File loaded: " + files.get(0).getName());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainGUI().setVisible(true);
        });
    }
}
