package gui;

import java.awt.*;
import javax.swing.*;

/**
 * Settings dialog that allows players to customize board colors, size,
 * and chess piece color style.
 * Changes are applied in real-time when the Apply button is clicked.
 */
public class SettingsDialog extends JDialog {

    /** Reference to the board panel to apply settings. */
    private ChessBoardPanel boardPanel;

    /** Color chooser buttons. */
    private Color chosenLight;
    private Color chosenDark;

    /** Preview panels for selected colors. */
    private JPanel lightPreview;
    private JPanel darkPreview;

    /** Combo box for piece style selection. */
    private JComboBox<String> pieceStyleBox;

    /**
     * Constructs the settings dialog.
     *
     * @param parent     the parent ChessFrame
     * @param boardPanel the board panel to apply settings to
     */
    public SettingsDialog(ChessFrame parent, ChessBoardPanel boardPanel) {
        super(parent, "Settings", true);
        this.boardPanel  = boardPanel;
        this.chosenLight = new Color(240, 217, 181);
        this.chosenDark  = new Color(181, 136, 99);

        setLayout(new BorderLayout(10, 10));
        setResizable(false);

        JPanel content = new JPanel(new GridLayout(0, 1, 8, 8));
        content.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        content.setBackground(new Color(50, 50, 50));

        // ── Light square color ────────────────────────────────────────
        JLabel lightLabel = styledLabel("Light Square Color:");
        content.add(lightLabel);

        lightPreview = new JPanel();
        lightPreview.setBackground(chosenLight);
        lightPreview.setPreferredSize(new Dimension(60, 30));

        JButton lightBtn = styledButton("Choose...");
        lightBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Light Square Color", chosenLight);
            if (c != null) { chosenLight = c; lightPreview.setBackground(c); }
        });

        JPanel lightRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lightRow.setBackground(new Color(50, 50, 50));
        lightRow.add(lightPreview);
        lightRow.add(lightBtn);
        content.add(lightRow);

        // ── Dark square color ─────────────────────────────────────────
        content.add(styledLabel("Dark Square Color:"));

        darkPreview = new JPanel();
        darkPreview.setBackground(chosenDark);
        darkPreview.setPreferredSize(new Dimension(60, 30));

        JButton darkBtn = styledButton("Choose...");
        darkBtn.addActionListener(e -> {
            Color c = JColorChooser.showDialog(this, "Choose Dark Square Color", chosenDark);
            if (c != null) { chosenDark = c; darkPreview.setBackground(c); }
        });

        JPanel darkRow = new JPanel(new FlowLayout(FlowLayout.LEFT));
        darkRow.setBackground(new Color(50, 50, 50));
        darkRow.add(darkPreview);
        darkRow.add(darkBtn);
        content.add(darkRow);

        // ── Piece color/style ─────────────────────────────────────────
        content.add(styledLabel("Chess Piece Style:"));

        String[] pieceStyles = {"Classic (White & Black)", "Blue & Red", "Green & Purple", "Gold & Silver"};
        pieceStyleBox = new JComboBox<>(pieceStyles);
        pieceStyleBox.setBackground(new Color(70, 70, 70));
        pieceStyleBox.setForeground(Color.WHITE);
        pieceStyleBox.setFont(new Font("SansSerif", Font.PLAIN, 12));
        content.add(pieceStyleBox);

        // ── Board size ────────────────────────────────────────────────
        content.add(styledLabel("Board Size:"));

        String[] sizes = {"Small (60px)", "Medium (80px)", "Large (100px)"};
        JComboBox<String> sizeBox = new JComboBox<>(sizes);
        sizeBox.setSelectedIndex(1); // default medium
        sizeBox.setBackground(new Color(70, 70, 70));
        sizeBox.setForeground(Color.WHITE);
        content.add(sizeBox);

        add(content, BorderLayout.CENTER);

        // ── Buttons ───────────────────────────────────────────────────
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.setBackground(new Color(40, 40, 40));

        JButton applyBtn = styledButton("Apply");
        applyBtn.setBackground(new Color(60, 140, 60));
        applyBtn.addActionListener(e -> {
            boardPanel.setLightColor(chosenLight);
            boardPanel.setDarkColor(chosenDark);
            int[] pixelSizes = {60, 80, 100};
            boardPanel.setSquareSize(pixelSizes[sizeBox.getSelectedIndex()]);
            boardPanel.setPieceStyle(pieceStyleBox.getSelectedIndex());
            parent.pack();
        });

        JButton cancelBtn = styledButton("Cancel");
        cancelBtn.addActionListener(e -> dispose());

        btnPanel.add(applyBtn);
        btnPanel.add(cancelBtn);
        add(btnPanel, BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(parent);
    }

    /**
     * Creates a styled label for the settings panel.
     *
     * @param text the label text
     * @return styled JLabel
     */
    private JLabel styledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("SansSerif", Font.BOLD, 12));
        return label;
    }

    /**
     * Creates a styled button for the settings panel.
     *
     * @param text the button text
     * @return styled JButton
     */
    private JButton styledButton(String text) {
        JButton btn = new JButton(text);
        btn.setBackground(new Color(80, 80, 80));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }
}