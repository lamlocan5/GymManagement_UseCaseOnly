package view;

import java.awt.*;
import java.awt.event.ActionListener;
import javax.swing.*;

public class ManageHomeView extends JFrame {
    private JButton updateRoomButton;
    
    public ManageHomeView() {
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Gym Management System - Home");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        // Header
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(66, 139, 202));
        headerPanel.setPreferredSize(new Dimension(600, 60));
        headerPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        
        JLabel titleLabel = new JLabel("Gym Management System");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel);
        
        // Content panel with Update Room button
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout());
        
        updateRoomButton = new JButton("Update Room");
        updateRoomButton.setFont(new Font("Arial", Font.BOLD, 16));
        updateRoomButton.setPreferredSize(new Dimension(150, 50));
        contentPanel.add(updateRoomButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel();
        footerPanel.setPreferredSize(new Dimension(600, 30));
        JLabel footerLabel = new JLabel("© 2025 Gym Management System");
        footerLabel.setFont(new Font("Arial", Font.ITALIC, 12));
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
    }
    
    public void setManageRoomButtonListener(ActionListener listener) {
        updateRoomButton.addActionListener(listener);
    }
}
