package com.knb.view;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
* Theme class providing professional SBI-like styling for the banking application
* Contains colors, fonts, and styling methods for consistent UI appearance
*/
public class Theme {
    // ==================== FONT DEFINITIONS ====================
    public static final Font TITLE_FONT = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font SUBTITLE_FONT = new Font("Segoe UI", Font.PLAIN, 16);
    public static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font BUTTON_BOLD_FONT = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font TABLE_FONT = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font HEADER_FONT = new Font("Segoe UI", Font.BOLD, 26);

    // ==================== COLOR DEFINITIONS (SBI-LIKE) ====================
    public static final Color PRIMARY_COLOR = new Color(0, 51, 102); // Deep blue
    public static final Color SECONDARY_COLOR = new Color(0, 123, 255); // Light blue
    public static final Color ACCENT_COLOR = new Color(255, 204, 51); // Golden yellow

    // Status colors
    public static final Color DANGER_COLOR = new Color(220, 53, 69);
    public static final Color SUCCESS_COLOR = new Color(40, 167, 69);
    public static final Color WARNING_COLOR = new Color(255, 193, 7);
    public static final Color INFO_COLOR = new Color(23, 162, 184);

    // Background and text colors
    public static final Color BACKGROUND_COLOR = new Color(245, 247, 250);
    public static final Color CARD_BACKGROUND = new Color(255, 255, 255);
    public static final Color TEXT_COLOR = new Color(30, 30, 30);
    public static final Color LIGHT_TEXT_COLOR = new Color(108, 117, 125);

    // Navigation colors
    public static final Color NAV_BACKGROUND = new Color(248, 249, 250);
    public static final Color NAV_HOVER = new Color(233, 236, 239);
    public static final Color NAV_SELECTED = new Color(0, 123, 255);

    // Text component colors
    public static final Color TEXTFIELD_BACKGROUND = Color.WHITE;
    public static final Color TEXTFIELD_TEXT_COLOR = Color.BLACK;
    public static final Color TEXTFIELD_BORDER = new Color(206, 212, 218);
    public static final Color TEXTFIELD_FOCUS_BORDER = new Color(0, 123, 255);

    // Text area colors
    public static final Color TEXTAREA_BACKGROUND = Color.WHITE;
    public static final Color TEXTAREA_TEXT_COLOR = Color.BLACK;

    // ==================== STYLING METHODS ====================
    public static void styleNavButton(JButton button) {
        button.setFont(BUTTON_BOLD_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK); // FORCED BLACK
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        button.setOpaque(true);
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(NAV_HOVER);
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
        });
    }

    public static void stylePrimaryButton(JButton button) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.BLACK); // FORCED BLACK
        button.setFont(BUTTON_BOLD_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR.darker());
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(PRIMARY_COLOR);
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
        });
    }

    public static void styleDangerButton(JButton button) {
        button.setBackground(DANGER_COLOR);
        button.setForeground(Color.BLACK); // FORCED BLACK
        button.setFont(BUTTON_BOLD_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER_COLOR.darker());
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(DANGER_COLOR);
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
        });
    }

    public static void styleSuccessButton(JButton button) {
        button.setBackground(SUCCESS_COLOR);
        button.setForeground(Color.BLACK); // FORCED BLACK
        button.setFont(BUTTON_BOLD_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR.darker());
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(SUCCESS_COLOR);
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
        });
    }

    public static void styleSecondaryButton(JButton button) {
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK); // FORCED BLACK
        button.setFont(BUTTON_BOLD_FONT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(8, 18, 8, 18)
        ));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(Theme.NAV_HOVER); // A light grey for hover
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(Color.WHITE);
                button.setForeground(Color.BLACK); // FORCED BLACK
            }
        });
    }

    public static void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setMargin(new Insets(15, 15, 15, 15));
        textArea.setEditable(false);
        textArea.setBackground(TEXTAREA_BACKGROUND);
        textArea.setForeground(TEXTAREA_TEXT_COLOR);
        textArea.setCaretColor(TEXTAREA_TEXT_COLOR);
        textArea.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXTFIELD_BORDER),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
    }

    public static void styleTextField(JTextField textField) {
        textField.setFont(LABEL_FONT);
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXTFIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        textField.setBackground(TEXTFIELD_BACKGROUND);
        textField.setForeground(TEXTFIELD_TEXT_COLOR);
        textField.setCaretColor(TEXTFIELD_TEXT_COLOR);
        textField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEXTFIELD_FOCUS_BORDER, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                textField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEXTFIELD_BORDER),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    public static void stylePasswordField(JPasswordField passwordField) {
        passwordField.setFont(LABEL_FONT);
        passwordField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TEXTFIELD_BORDER),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        passwordField.setBackground(TEXTFIELD_BACKGROUND);
        passwordField.setForeground(TEXTFIELD_TEXT_COLOR);
        passwordField.setCaretColor(TEXTFIELD_TEXT_COLOR);
        passwordField.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEXTFIELD_FOCUS_BORDER, 2),
                    BorderFactory.createEmptyBorder(7, 11, 7, 11)
                ));
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                passwordField.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(TEXTFIELD_BORDER),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
    }

    public static void styleComboBox(JComboBox<?> comboBox) {
        comboBox.setFont(LABEL_FONT);
        comboBox.setBackground(TEXTFIELD_BACKGROUND);
        comboBox.setForeground(TEXTFIELD_TEXT_COLOR);
        comboBox.setBorder(BorderFactory.createLineBorder(TEXTFIELD_BORDER));
        if (comboBox.getUI() instanceof javax.swing.plaf.basic.BasicComboBoxUI) {
            comboBox.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                    Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (!isSelected) {
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                    }
                    return c;
                }
            });
        }
    }

    public static void styleTable(JTable table) {
        table.setFont(TABLE_FONT);
        table.setRowHeight(35);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        table.setSelectionBackground(new Color(232, 240, 254));
        table.setSelectionForeground(TEXT_COLOR);
        table.setBackground(Color.WHITE);
        table.setForeground(Color.BLACK);

        JTableHeader tableHeader = table.getTableHeader();
        tableHeader.setBackground(PRIMARY_COLOR);
        tableHeader.setForeground(Color.BLACK); // This was your original request for the table
        tableHeader.setFont(BUTTON_BOLD_FONT);
        tableHeader.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        table.setIntercellSpacing(new Dimension(1, 1));
    }

    public static JPanel createCard() {
        JPanel card = new JPanel();
        card.setBackground(CARD_BACKGROUND);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        return card;
    }

    public static JPanel createHeaderPanel(String title) {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(PRIMARY_COLOR);
        header.setPreferredSize(new Dimension(0, 80));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(HEADER_FONT);
        titleLabel.setForeground(Color.BLACK); // FORCED BLACK
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        header.add(titleLabel, BorderLayout.WEST);
        return header;
    }

    public static JPanel createMetricCard(String title, String value, Color valueColor) {
        JPanel card = createCard();
        card.setLayout(new BorderLayout(10, 10));
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(SUBTITLE_FONT);
        titleLabel.setForeground(LIGHT_TEXT_COLOR);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        valueLabel.setForeground(valueColor != null ? valueColor : PRIMARY_COLOR);
        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);
        return card;
    }
}

