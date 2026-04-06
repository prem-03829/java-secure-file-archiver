package com.a_3_group_3.filecompressor.gui;

import com.a_3_group_3.filecompressor.analysis.CompressionReport;
import com.a_3_group_3.filecompressor.compression.*;
import com.a_3_group_3.filecompressor.io.FileReaderUtil;
import com.a_3_group_3.filecompressor.utils.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MainGUI extends JFrame {

    private JTextField inputField;
    private JProgressBar progressBar;
    private JTextArea logArea;
    private JCheckBox parallelToggle;
    private JLabel originalSizeLabel, compressedSizeLabel, ratioLabel;
    private JButton compressBtn, decompressBtn;

    private static final Color BACK_COLOR = new Color(33, 37, 43);
    private static final Color CARD_COLOR = new Color(40, 44, 52);
    private static final Color ACCENT_COLOR = new Color(97, 175, 239);
    private static final Color TEXT_COLOR = new Color(171, 178, 191);
    private static final Color SUCCESS_COLOR = new Color(152, 195, 121);
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

        JLabel inputLabel = new JLabel("Input File:");
        inputLabel.setForeground(TEXT_COLOR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        mainPanel.add(inputLabel, gbc);

        inputField = new JTextField();
        inputField.setBackground(CARD_COLOR);
        inputField.setForeground(Color.WHITE);
        inputField.setCaretColor(Color.WHITE);
        inputField.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        gbc.gridx = 1; gbc.weightx = 1.0;
        mainPanel.add(inputField, gbc);

        JButton browseBtn = createStyledButton("Browse", new Color(72, 77, 86));
        browseBtn.addActionListener(e -> chooseFile());
        gbc.gridx = 2; gbc.weightx = 0;
        mainPanel.add(browseBtn, gbc);

        parallelToggle = new JCheckBox("Use Parallel Encoding (Multi-core)");
        parallelToggle.setForeground(TEXT_COLOR);
        parallelToggle.setBackground(BACK_COLOR);
        gbc.gridx = 1; gbc.gridy = 1;
        mainPanel.add(parallelToggle, gbc);

        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        statsPanel.setOpaque(false);
        originalSizeLabel = createStatLabel("Original: -");
        compressedSizeLabel = createStatLabel("Compressed: -");
        ratioLabel = createStatLabel("Ratio: -");
        statsPanel.add(originalSizeLabel);
        statsPanel.add(compressedSizeLabel);
        statsPanel.add(ratioLabel);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 3;
        mainPanel.add(statsPanel, gbc);

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setBackground(CARD_COLOR);
        progressBar.setForeground(ACCENT_COLOR);
        progressBar.setBorderPainted(false);
        gbc.gridy = 3;
        mainPanel.add(progressBar, gbc);

        logArea = new JTextArea(8, 40);
        logArea.setEditable(false);
        logArea.setBackground(CARD_COLOR);
        logArea.setForeground(TEXT_COLOR);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        JScrollPane scrollLog = new JScrollPane(logArea);
        scrollLog.setBorder(BorderFactory.createLineBorder(CARD_COLOR.brighter()));
        gbc.gridy = 4; gbc.weighty = 1.0; gbc.fill = GridBagConstraints.BOTH;
        mainPanel.add(scrollLog, gbc);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 20));
        actionPanel.setOpaque(false);
        compressBtn = createStyledButton("COMPRESS", SUCCESS_COLOR);
        decompressBtn = createStyledButton("DECOMPRESS", ACCENT_COLOR);
        compressBtn.addActionListener(e -> processFile(true));
        decompressBtn.addActionListener(e -> processFile(false));
        actionPanel.add(compressBtn);
        actionPanel.add(decompressBtn);
        gbc.gridy = 5; gbc.weighty = 0; gbc.fill = GridBagConstraints.HORIZONTAL;
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
        if (bg.getRed() + bg.getGreen() + bg.getBlue() > 400) b.setForeground(new Color(20, 20, 20));
        else b.setForeground(Color.WHITE);
        b.setFocusPainted(false);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
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
        }
    }

    private void log(String m) {
        logArea.append(m + "\n");
        logArea.setCaretPosition(logArea.getDocument().getLength());
    }

    private void processFile(boolean compress) {
        String inputPath = inputField.getText();
        if (inputPath.isEmpty()) return;
        File inputFile = new File(inputPath);
        if (!inputFile.exists()) return;

        String outputPath;
        if (compress) {
            int lastDot = inputPath.lastIndexOf('.');
            // If dot exists and is not the very first char of the filename
            if (lastDot > inputPath.lastIndexOf(File.separator)) {
                outputPath = inputPath.substring(0, lastDot) + ".huff";
            } else {
                outputPath = inputPath + ".huff";
            }
        } else {
            outputPath = inputPath.replace(".huff", "") + "_restored.txt";
        }

        setUIEnabled(false);
        progressBar.setValue(0);
        originalSizeLabel.setText("Original: -");
        compressedSizeLabel.setText("Compressed: -");
        ratioLabel.setText("Ratio: -");
        log("--- Starting " + (compress ? "COMPRESSION" : "DECOMPRESSION") + " ---");

        new SwingWorker<Boolean, Object[]>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                long startTime = System.currentTimeMillis();
                try {
                    if (compress) {
                        publish(new Object[] { 5, "Initializing Compression Engine..." });
                        byte[] data = FileReaderUtil.readFile(inputPath);
                        
                        // REUSED
                        String fileName = FileUtils.getFileName(inputPath);
                        long originalSize = data.length;

                        publish(new Object[] { 10, "File Name: " + fileName });
                        publish(new Object[] { 15, "Input size: " + originalSize + " bytes" });

                        publish(new Object[] { 25, "Applying LZ77 (32KB Sliding Window)..." });
                        long lzStart = System.currentTimeMillis();
                        List<LZ77Encoder.Token> tokens = LZ77Encoder.encode(data);
                        long lzEnd = System.currentTimeMillis();
                        
                        // REUSED / EXTENDED
                        int literals = 0;
                        int matches = 0;
                        long totalMatchLen = 0;
                        for (LZ77Encoder.Token t : tokens) {
                            if (t.symbol < 256) literals++;
                            else {
                                matches++;
                                totalMatchLen += (t.symbol - 257 + 3);
                            }
                        }
                        publish(new Object[] { 40, "LZ77 Pass Complete." });

                        publish(new Object[] { 55, "Generating Canonical Huffman Codes..." });
                        // REUSED
                        int[] freqs = new int[1024];
                        int uniqueSymbols = 0;
                        for (LZ77Encoder.Token t : tokens) {
                            if (t.symbol >= 0 && t.symbol < freqs.length) {
                                if (freqs[t.symbol] == 0) uniqueSymbols++;
                                freqs[t.symbol]++;
                            }
                        }
                        byte[] litLens = new byte[1024];
                        HuffmanCodeGenerator.generateCanonicalCodes(freqs, litLens);
                        
                        // EXTENDED (Huffman stats)
                        int minLen = Integer.MAX_VALUE, maxLen = 0;
                        long totalLenSum = 0;
                        int symbolsWithCode = 0;
                        for (byte b : litLens) {
                            if (b > 0) {
                                if (b < minLen) minLen = b;
                                if (b > maxLen) maxLen = b;
                                totalLenSum += b;
                                symbolsWithCode++;
                            }
                        }

                        publish(new Object[] { 75, "Packing Bitstream & Writing to Disk..." });
                        BitWriter.writeCompressedFile(data, tokens, outputPath, litLens, new byte[0]);
                        
                        long endTime = System.currentTimeMillis();
                        long compressedSize = new File(outputPath).length();
                        
                        // NEW: Build the Full Report
                        CompressionReport report = new CompressionReport();
                        // [1] File Info (REUSED)
                        report.fileName = fileName;
                        report.fileType = FileUtils.getFileExtension(inputPath);
                        report.originalSize = originalSize;
                        report.compressedSize = compressedSize;
                        report.timestamp = new Date();
                        report.status = (compressedSize < originalSize) ? "COMPRESSED" : "STORED RAW";

                        // [2] Size Metrics (REUSED / EXTENDED)
                        report.compressionRatio = (double) originalSize / compressedSize;
                        report.spaceSaved = ((double) (originalSize - compressedSize) / originalSize) * 100;
                        report.bitsPerByte = (compressedSize * 8.0) / originalSize;

                        // [3] Frequency & Huffman (EXTENDED)
                        report.totalSymbols = tokens.size();
                        report.uniqueSymbols = uniqueSymbols;
                        report.minCodeLength = (minLen == Integer.MAX_VALUE) ? 0 : minLen;
                        report.maxCodeLength = maxLen;
                        report.avgCodeLength = (symbolsWithCode == 0) ? 0 : (double) totalLenSum / symbolsWithCode;

                        // [4] LZ77 (REUSED / EXTENDED)
                        report.matchesCount = matches;
                        report.literalsCount = literals;
                        report.avgMatchLength = (matches == 0) ? 0 : (double) totalMatchLen / matches;

                        // FIXED: Read actual padding bits from output file
                        int actualPadding = 0;
                        if (compressedSize < originalSize) {
                            try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(outputPath, "r")) {
                                raf.seek(1);
                                actualPadding = raf.readByte();
                            } catch(Exception e) {}
                        }

                        // [5] Encoding & Metadata (EXTENDED)
                        report.headerSize = 1; // Flag byte
                        report.huffmanTableSize = 4 + litLens.length; // int + table
                        report.totalMetadataSize = report.headerSize + report.huffmanTableSize + 1; // + padding byte
                        report.paddingBits = actualPadding;
                        report.totalBitsWritten = compressedSize * 8;

                        // [6] System (REUSED)
                        report.threadsUsed = parallelToggle.isSelected() ? Runtime.getRuntime().availableProcessors() : 1;
                        report.executionTime = endTime - startTime;
                        report.fallbackUsed = (compressedSize >= originalSize);
                        report.fallbackReason = report.fallbackUsed ? "Header overhead exceeds savings" : "";

                        publish(new Object[] { 100, report.toString() });

                        SwingUtilities.invokeLater(() -> {
                            originalSizeLabel.setText("Original: " + originalSize + " B");
                            compressedSizeLabel.setText("Compressed: " + compressedSize + " B");
                            ratioLabel.setText("Ratio: " + String.format("%.2f", report.spaceSaved) + "%");
                        });
                    } else {
                        publish(new Object[] { 20, "Analyzing Huffman Header..." });
                        publish(new Object[] { 50, "Decoding DEFLATE-like Bitstream..." });
                        long decodeStart = System.currentTimeMillis();
                        HuffmanDecoder.decodeFile(inputPath, outputPath);
                        long decodeEnd = System.currentTimeMillis();
                        
                        publish(new Object[] { 100, "\n--- Decompression Summary ---" });
                        publish(new Object[] { 100, "Input File    : " + FileUtils.getFileName(inputPath) });
                        publish(new Object[] { 100, "Output File   : " + FileUtils.getFileName(outputPath) });
                        publish(new Object[] { 100, "Process Time  : " + (decodeEnd - decodeStart) + "ms" });
                        publish(new Object[] { 100, "Status        : SUCCESS" });
                    }
                    return true;
                } catch (Exception ex) {
                    // FIXED: Show errors in GUI log panel
                    publish(new Object[] { 0, "ERROR: " + ex.getMessage() });
                    return false;
                }
            }
            @Override
            protected void process(List<Object[]> chunks) {
                for (Object[] chunk : chunks) { progressBar.setValue((Integer) chunk[0]); log((String) chunk[1]); }
            }
            @Override
            protected void done() {
                try {
                    boolean success = get();
                    if (success) {
                        progressBar.setValue(100); log("SUCCESS.");
                    } else {
                        log("FAILED.");
                    }
                } catch (Exception e) {
                    log("FAILED.");
                }
                setUIEnabled(true);
            }
        }.execute();
    }

    private void setUIEnabled(boolean e) {
        compressBtn.setEnabled(e); decompressBtn.setEnabled(e); inputField.setEnabled(e);
    }

    private void setupDragAndDrop() {
        new DropTarget(this, new DropTargetAdapter() {
            public void drop(DropTargetDropEvent dtde) {
                try {
                    dtde.acceptDrop(DnDConstants.ACTION_COPY);
                    List<File> files = (List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                    if (!files.isEmpty()) inputField.setText(files.get(0).getAbsolutePath());
                } catch (Exception ex) {}
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainGUI().setVisible(true));
    }
}
