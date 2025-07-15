package view;

import controller.RoomController;
import model.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;

public class SearchRoomView extends JFrame {
    private final RoomController roomController;
    private final ManageHomeView homeView;
    
    private JPanel mainPanel;
    private JPanel searchPanel;
    private JPanel resultPanel;
    
    private JTextField roomIdField;
    private JButton searchButton;
    private JButton backButton;
    
    private JTable roomTable;
    private DefaultTableModel tableModel;
    
    private CardLayout cardLayout;
    
    public SearchRoomView(ManageHomeView homeView) {
        this.roomController = new RoomController();
        this.homeView = homeView;
        initializeUI();
    }
    
    private void initializeUI() {
        setTitle("Search Rooms");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        
        // Create panels
        searchPanel = createSearchPanel();
        resultPanel = new JPanel(new BorderLayout());
        
        // Add panels to main panel with card layout
        mainPanel.add(searchPanel, "search");
        mainPanel.add(resultPanel, "result");
        
        // Set initial panel
        cardLayout.show(mainPanel, "search");
        
        add(mainPanel);
        
        // Set action listeners
        setSearchButtonListener(e -> handleSearch());
        setBackButtonListener(e -> goBack());
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50));
        
        JLabel titleLabel = new JLabel("Search Rooms");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        backButton = new JButton("Back to Home");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backButton, BorderLayout.EAST);
        
        // Search form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.Y_AXIS));
        formPanel.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        
        JPanel inputPanel = new JPanel();
        JLabel roomIdLabel = new JLabel("Room ID:");
        roomIdLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        roomIdField = new JTextField(20);
        searchButton = new JButton("Search");
        searchButton.setFont(new Font("Arial", Font.PLAIN, 14));
        
        inputPanel.add(roomIdLabel);
        inputPanel.add(roomIdField);
        inputPanel.add(searchButton);
        
        formPanel.add(inputPanel);
        
        // Add components to panel
        panel.add(headerPanel, BorderLayout.NORTH);
        panel.add(formPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private void createResultPanel(List<Room> rooms) {
        resultPanel.removeAll();
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(800, 50));
        
        JLabel titleLabel = new JLabel("Search Results");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton backToSearchButton = new JButton("Back to Search");
        backToSearchButton.addActionListener(e -> showSearchPanel());
        
        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(backToSearchButton, BorderLayout.EAST);
        
        // Table
        String[] columns = {"Room ID", "Type", "PT ID", "PT Name", 
                          "Price", "Start Date", "Due Date", "Note", "Action"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 8; // Only the Action column is editable
            }
        };
        
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        
        for (Room room : rooms) {
            String startDateStr = room.getStartDate() != null ? 
                    dateFormat.format(room.getStartDate()) : "";
            String dueDateStr = room.getDueDate() != null ? 
                    dateFormat.format(room.getDueDate()) : "";
            
            Object[] rowData = {
                room.getRoomId(),
                room.getType(),
                room.getPtId(),
                room.getPtName(),
                room.getPrice(),
                startDateStr,
                dueDateStr,
                room.getNote(),
                "Update"
            };
            tableModel.addRow(rowData);
        }
        
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        
        // Add button renderer and editor for the Action column
        roomTable.getColumnModel().getColumn(8).setCellRenderer(new ButtonRenderer());
        roomTable.getColumnModel().getColumn(8).setCellEditor(
                new ButtonEditor(new JCheckBox(), this));
        
        resultPanel.add(headerPanel, BorderLayout.NORTH);
        resultPanel.add(scrollPane, BorderLayout.CENTER);
        
        resultPanel.revalidate();
        resultPanel.repaint();
    }
    
    private void handleSearch() {
        String roomId = roomIdField.getText().trim();
        
        if (roomId.isEmpty()) {
            displayErrorMessage("Please enter a Room ID");
            return;
        }
        
        List<Room> rooms = roomController.searchRoom(roomId);
        
        if (rooms == null || rooms.isEmpty()) {
            displayErrorMessage("No rooms found with ID containing: " + roomId);
            return;
        }
        
        createResultPanel(rooms);
        showResultPanel();
    }
    
    public void openUpdateView(String roomId) {
        Room room = roomController.searchRoomById(roomId);
        if (room != null) {
            UpdateRoomView updateView = new UpdateRoomView(this, room);
            updateView.setVisible(true);
            this.setVisible(false);
        } else {
            displayErrorMessage("Could not find room details for room: " + roomId);
        }
    }
    
    private void goBack() {
        this.dispose();
        homeView.setVisible(true);
    }
    
    public void refreshRoomTable(String roomId) {
        List<Room> rooms = roomController.searchRoom(roomId);
        if (rooms != null && !rooms.isEmpty()) {
            createResultPanel(rooms);
        }
    }
    
    public void showSearchPanel() {
        cardLayout.show(mainPanel, "search");
    }
    
    public void showResultPanel() {
        cardLayout.show(mainPanel, "result");
    }
    
    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }
    
    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }
    
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    // Custom Button Renderer
    private static class ButtonRenderer extends JButton implements javax.swing.table.TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                     boolean isSelected, boolean hasFocus,
                                                     int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }
    
    // Custom Button Editor
    private static class ButtonEditor extends DefaultCellEditor {
        protected JButton button;
        private String label;
        private boolean isPushed;
        private final SearchRoomView view;
        
        public ButtonEditor(JCheckBox checkBox, SearchRoomView view) {
            super(checkBox);
            this.view = view;
            button = new JButton();
            button.setOpaque(true);
            
            button.addActionListener(e -> fireEditingStopped());
        }
        
        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                   boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            button.setText(label);
            isPushed = true;
            return button;
        }
        
        @Override
        public Object getCellEditorValue() {
            if (isPushed) {
                // Get room ID from the first column
                int row = view.roomTable.getSelectedRow();
                String roomId = view.tableModel.getValueAt(row, 0).toString();
                view.openUpdateView(roomId);
            }
            isPushed = false;
            return label;
        }
        
        @Override
        public boolean stopCellEditing() {
            isPushed = false;
            return super.stopCellEditing();
        }
    }
}
