package com.knb.view.common;

import com.knb.view.Theme;

import javax.swing.*;
import java.awt.*;

/**
 * LogoPanel provides branded logo display component
 * Professional KNB banking logo with consistent styling
 */
public class LogoPanel extends JPanel {
    
    public LogoPanel() {
        initializePanel();
    }
    
    private void initializePanel() {
        setBackground(Theme.PRIMARY_COLOR);
        setLayout(new GridBagLayout());
        setPreferredSize(new Dimension(400, 80));
        
        // Main logo container
        JPanel logoContainer = new JPanel();
        logoContainer.setLayout(new BoxLayout(logoContainer, BoxLayout.X_AXIS));
        logoContainer.setOpaque(false);
        
        // Bank logo text
        JLabel logoLabel = new JLabel("KNB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 42));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 15));
        
        // Tagline panel
        JPanel taglinePanel = new JPanel();
        taglinePanel.setLayout(new BoxLayout(taglinePanel, BoxLayout.Y_AXIS));
        taglinePanel.setOpaque(false);
        
        JLabel bankingLabel = new JLabel("Banking");
        bankingLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        bankingLabel.setForeground(Color.WHITE);
        bankingLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel solutionsLabel = new JLabel("Solutions");
        solutionsLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        solutionsLabel.setForeground(new Color(255, 255, 255, 180));
        solutionsLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        taglinePanel.add(bankingLabel);
        taglinePanel.add(Box.createVerticalStrut(2));
        taglinePanel.add(solutionsLabel);
        
        logoContainer.add(logoLabel);
        logoContainer.add(taglinePanel);
        
        add(logoContainer);
    }
    
    /**
     * Create a smaller version of the logo for headers
     */
    public static JPanel createCompactLogo() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panel.setOpaque(false);
        
        JLabel logoLabel = new JLabel("KNB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        logoLabel.setForeground(Color.WHITE);
        
        JLabel tagLabel = new JLabel(" Banking");
        tagLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        tagLabel.setForeground(new Color(255, 255, 255, 180));
        
        panel.add(logoLabel);
        panel.add(tagLabel);
        
        return panel;
    }
    
    /**
     * Create a minimal logo for small spaces
     */
    public static JLabel createMinimalLogo() {
        JLabel logoLabel = new JLabel("KNB");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(Color.WHITE);
        logoLabel.setOpaque(true);
        logoLabel.setBackground(Theme.PRIMARY_COLOR);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        logoLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        return logoLabel;
    }
}
