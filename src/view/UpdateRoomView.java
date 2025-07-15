package view;

import controller.RoomController;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import model.PT;
import model.Room;

public class UpdateRoomView extends JFrame {
    private final RoomController roomController;
    private final SearchRoomView parentView;
    private final Room originalRoom;
    
    private JTextField roomIdField;
    private JTextField roomTypeField;
    private JTextField ptIdField;
    private JTextField ptNameField;
    private JTextField priceField;
    private JTextField startDateField;
    private JTextField dueDateField;
    private JTextArea noteArea;
    
    private JButton updateButton;
    private JButton cancelButton;
    
    private JLabel ptIdErrorLabel;
    private JLabel ptNameErrorLabel;
    private JLabel priceErrorLabel;
    private JLabel startDateErrorLabel;
    private JLabel dueDateErrorLabel;
    
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private boolean suppressPtErrors = false;
    private boolean isValidatingPt = false;
    private Timer validationTimer;
    
    public UpdateRoomView(SearchRoomView parentView, Room room) {
        this.roomController = new RoomController();
        this.parentView = parentView;
        this.originalRoom = room;
        
        initializeUI();
        fillRoomInfo(room);
    }
    
    private void initializeUI() {
        setTitle("Update Room Information");
        setSize(600, 550);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("Update Room Information");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        headerPanel.add(titleLabel, BorderLayout.WEST);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // Room ID field (read-only)
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(new JLabel("Room ID:"), gbc);
        
        gbc.gridx = 1;
        roomIdField = new JTextField(20);
        roomIdField.setEditable(false);
        formPanel.add(roomIdField, gbc);
        
        // Room Type field
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(new JLabel("Room Type:"), gbc);
        
        gbc.gridx = 1;
        roomTypeField = new JTextField(20);
        formPanel.add(roomTypeField, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(new JLabel("PT ID:"), gbc);
        
        gbc.gridx = 1;
        JPanel ptIdPanel = new JPanel(new BorderLayout());
        ptIdField = new JTextField(20);
        ptIdErrorLabel = new JLabel("");
        ptIdErrorLabel.setForeground(Color.RED);
        ptIdErrorLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        
        ptIdField.getDocument().addDocumentListener(new DelayedDocumentListener());
        ptIdField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!ptNameField.isFocusOwner()) {
                    validatePtFields();
                }
            }
        });
        
        ptIdPanel.add(ptIdField, BorderLayout.CENTER);
        ptIdPanel.add(ptIdErrorLabel, BorderLayout.SOUTH);
        formPanel.add(ptIdPanel, gbc);
        
        // PT Name field
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(new JLabel("PT Name:"), gbc);
        
        gbc.gridx = 1;
        JPanel ptNamePanel = new JPanel(new BorderLayout());
        ptNameField = new JTextField(20);
        ptNameErrorLabel = new JLabel("");
        ptNameErrorLabel.setForeground(Color.RED);
        ptNameErrorLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        ptNamePanel.add(ptNameField, BorderLayout.CENTER);
        ptNamePanel.add(ptNameErrorLabel, BorderLayout.SOUTH);
        formPanel.add(ptNamePanel, gbc);
        
        ptNameField.getDocument().addDocumentListener(new DelayedDocumentListener());
        ptNameField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (!ptIdField.isFocusOwner()) {
                    validatePtFields();
                }
            }
        });
        
        // Price field
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(new JLabel("Price:"), gbc);
        
        gbc.gridx = 1;
        JPanel pricePanel = new JPanel(new BorderLayout());
        priceField = new JTextField(20);
        priceErrorLabel = new JLabel("");
        priceErrorLabel.setForeground(Color.RED);
        priceErrorLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        pricePanel.add(priceField, BorderLayout.CENTER);
        pricePanel.add(priceErrorLabel, BorderLayout.SOUTH);
        formPanel.add(pricePanel, gbc);
        
        priceField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validatePriceField();
            }
        });
        
        // Start Date field
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Start Date (dd/MM/yyyy):"), gbc);
        
        gbc.gridx = 1;
        JPanel startDatePanel = new JPanel(new BorderLayout());
        startDateField = new JTextField(20);
        startDateErrorLabel = new JLabel("");
        startDateErrorLabel.setForeground(Color.RED);
        startDateErrorLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        startDatePanel.add(startDateField, BorderLayout.CENTER);
        startDatePanel.add(startDateErrorLabel, BorderLayout.SOUTH);
        formPanel.add(startDatePanel, gbc);
        
        startDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateDateField(startDateField, startDateErrorLabel, "Start Date");
                validateDateRange();
            }
        });
        
        // Due Date field
        gbc.gridx = 0;
        gbc.gridy = 6;
        formPanel.add(new JLabel("Due Date (dd/MM/yyyy):"), gbc);
        
        gbc.gridx = 1;
        JPanel dueDatePanel = new JPanel(new BorderLayout());
        dueDateField = new JTextField(20);
        dueDateErrorLabel = new JLabel("");
        dueDateErrorLabel.setForeground(Color.RED);
        dueDateErrorLabel.setFont(new Font("Arial", Font.ITALIC, 10));
        dueDatePanel.add(dueDateField, BorderLayout.CENTER);
        dueDatePanel.add(dueDateErrorLabel, BorderLayout.SOUTH);
        formPanel.add(dueDatePanel, gbc);
        
        dueDateField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                validateDateField(dueDateField, dueDateErrorLabel, "Due Date");
                validateDateRange();
            }
        });
        
        // Note field
        gbc.gridx = 0;
        gbc.gridy = 7;
        formPanel.add(new JLabel("Note:"), gbc);
        
        gbc.gridx = 1;
        noteArea = new JTextArea(4, 20);
        noteArea.setLineWrap(true);
        JScrollPane noteScrollPane = new JScrollPane(noteArea);
        formPanel.add(noteScrollPane, gbc);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        updateButton = new JButton("Update");
        cancelButton = new JButton("Cancel");
        
        buttonPanel.add(updateButton);
        buttonPanel.add(cancelButton);
        
        // Add components to main panel
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(mainPanel);
        
        // Set tooltips
        ptIdField.setToolTipText("Enter PT ID from database");
        ptNameField.setToolTipText("Enter PT Name matching with PT ID");
        startDateField.setToolTipText("Enter date in format: dd/MM/yyyy");
        dueDateField.setToolTipText("Enter date in format: dd/MM/yyyy");
        
        // Set up validation timer
        validationTimer = new Timer(500, e -> validatePtFieldsQuietly());
        validationTimer.setRepeats(false);
        
        // Set action listeners
        setUpdateButtonListener(e -> handleUpdate());
        setCancelButtonListener(e -> handleCancel());
    }
    
    private class DelayedDocumentListener implements DocumentListener {
        @Override
        public void insertUpdate(DocumentEvent e) {
            restartValidationTimer();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            restartValidationTimer();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            restartValidationTimer();
        }
    }
    
    private void restartValidationTimer() {
        validationTimer.restart();
    }
    
    private boolean validatePtFields() {
        if (isValidatingPt) return true;
        isValidatingPt = true;
        
        try {
            return doValidatePtFields(true);
        } finally {
            isValidatingPt = false;
        }
    }
    
    private boolean validatePtFieldsQuietly() {
        if (isValidatingPt) return true;
        isValidatingPt = true;
        
        try {
            // Tự động điền PT Name nếu tìm thấy PT ID và PT Name đang trống
            String ptId = ptIdField.getText().trim();
            if (!ptId.isEmpty() && ptNameField.getText().trim().isEmpty()) {
                PT pt = roomController.getPTById(ptId);
                if (pt != null) {
                    ptNameField.setText(pt.getPtName());
                    ptNameField.setBackground(Color.WHITE);
                    ptNameErrorLabel.setText("");
                }
            }
            
            return doValidatePtFields(false);
        } finally {
            isValidatingPt = false;
        }
    }
    
    private boolean doValidatePtFields(boolean showPopup) {
        String ptId = ptIdField.getText().trim();
        String ptName = ptNameField.getText().trim();
        
        // Reset error messages
        ptIdErrorLabel.setText("");
        ptNameErrorLabel.setText("");
        ptIdField.setBackground(Color.WHITE);
        ptNameField.setBackground(Color.WHITE);
        
        // Trường hợp 1: Cả hai đều trống - hợp lệ
        if (ptId.isEmpty() && ptName.isEmpty()) {
            return true;
        }
        
        // Trường hợp 2: Chỉ một trong hai trống - không hợp lệ
        if (ptId.isEmpty() || ptName.isEmpty()) {
            if (ptId.isEmpty()) {
                ptIdField.setBackground(new Color(255, 200, 200));
                ptIdErrorLabel.setText("Required when PT Name is provided");
                if (showPopup) {
                    displayErrorMessage("PT ID is required when PT Name is provided");
                }
            } else {
                ptNameField.setBackground(new Color(255, 200, 200));
                ptNameErrorLabel.setText("Required when PT ID is provided");
                if (showPopup) {
                    displayErrorMessage("PT Name is required when PT ID is provided");
                }
            }
            return false;
        }
        
        // Trường hợp 3: Cả hai đều có giá trị - kiểm tra khớp
        PT pt = roomController.getPTById(ptId);
        if (pt == null) {
            ptIdField.setBackground(new Color(255, 200, 200));
            ptIdErrorLabel.setText("PT ID not found in database");
            if (showPopup) {
                displayErrorMessage("PT with ID " + ptId + " not found in database");
            }
            return false;
        }
        
        if (!ptName.equals(pt.getPtName())) {
            ptNameField.setBackground(new Color(255, 200, 200));
            ptNameErrorLabel.setText("Expected: " + pt.getPtName());
            if (showPopup) {
                displayErrorMessage("PT Name does not match with PT ID. For PT ID '" + 
                                  ptId + "', expected name: " + pt.getPtName());
            }
            return false;
        }
        
        // Tất cả đều hợp lệ
        return true;
    }
    
    private boolean validatePriceField() {
        String priceText = priceField.getText().trim();
        if (priceText.isEmpty()) {
            priceField.setBackground(new Color(255, 200, 200));
            priceErrorLabel.setText("Price cannot be empty");
            return false;
        }
        
        try {
            double price = Double.parseDouble(priceText);
            if (price < 0) {
                priceField.setBackground(new Color(255, 200, 200));
                priceErrorLabel.setText("Price cannot be negative");
                return false;
            }
            priceField.setBackground(Color.WHITE);
            priceErrorLabel.setText("");
            return true;
        } catch (NumberFormatException e) {
            priceField.setBackground(new Color(255, 200, 200));
            priceErrorLabel.setText("Invalid price format");
            return false;
        }
    }
    
    private boolean validateDateField(JTextField field, JLabel errorLabel, String fieldName) {
        String dateText = field.getText().trim();
        if (dateText.isEmpty()) {
            field.setBackground(Color.WHITE);
            errorLabel.setText("");
            return true; // Empty is allowed
        }
        
        if (!dateText.matches("\\d{2}/\\d{2}/\\d{4}")) {
            field.setBackground(new Color(255, 200, 200));
            errorLabel.setText("Use format: dd/MM/yyyy");
            return false;
        }
        
        try {
            dateFormat.setLenient(false); // This will enforce strict date validation
            Date date = dateFormat.parse(dateText);
            field.setBackground(Color.WHITE);
            errorLabel.setText("");
            return true;
        } catch (ParseException e) {
            field.setBackground(new Color(255, 200, 200));
            errorLabel.setText("Invalid date");
            return false;
        }
    }
    
    private boolean validateDateRange() {
        String startDateText = startDateField.getText().trim();
        String dueDateText = dueDateField.getText().trim();
        
        if (startDateText.isEmpty() || dueDateText.isEmpty()) {
            return true; // Can't compare if one is empty
        }
        
        try {
            Date startDate = dateFormat.parse(startDateText);
            Date dueDate = dateFormat.parse(dueDateText);
            
            if (dueDate.before(startDate)) {
                dueDateField.setBackground(new Color(255, 200, 200));
                dueDateErrorLabel.setText("Due Date must be after Start Date");
                return false;
            } else {
                dueDateField.setBackground(Color.WHITE);
                dueDateErrorLabel.setText("");
                return true;
            }
        } catch (ParseException e) {
            // This will likely be caught by validateDateField first
            return false;
        }
    }
    
    private void fillRoomInfo(Room room) {
        roomIdField.setText(room.getRoomId());
        roomTypeField.setText(room.getType());
        ptIdField.setText(room.getPtId() != null ? room.getPtId() : "");
        ptNameField.setText(room.getPtName() != null ? room.getPtName() : "");
        priceField.setText(String.valueOf(room.getPrice()));
        
        if (room.getStartDate() != null) {
            startDateField.setText(dateFormat.format(room.getStartDate()));
        } else {
            startDateField.setText("");
        }
        
        if (room.getDueDate() != null) {
            dueDateField.setText(dateFormat.format(room.getDueDate()));
        } else {
            dueDateField.setText("");
        }
        
        noteArea.setText(room.getNote() != null ? room.getNote() : "");
        
        // Thêm tooltip và placeholder text cho các trường
        ptIdField.setToolTipText("Enter PT ID from database");
        ptNameField.setToolTipText("Enter PT Name matching with PT ID");
        
        // Nếu không có PT, thêm gợi ý
        if (room.getPtId() == null || room.getPtId().isEmpty()) {
            ptIdErrorLabel.setText("Leave empty if no PT");
            ptIdErrorLabel.setForeground(new Color(0, 100, 0));  // Dark green
            ptNameErrorLabel.setText("Leave empty if no PT");
            ptNameErrorLabel.setForeground(new Color(0, 100, 0));  // Dark green
        }
    }
    
    private Room getUpdatedRoomInfo() {
        // Validate all fields first
        boolean isValid = true;
        
        isValid = validatePriceField() && isValid;
        isValid = validateDateField(startDateField, startDateErrorLabel, "Start Date") && isValid;
        isValid = validateDateField(dueDateField, dueDateErrorLabel, "Due Date") && isValid;
        isValid = validateDateRange() && isValid;
        isValid = validatePtFields() && isValid;  // Kiểm tra PT ID và PT Name
        
        if (!isValid) {
            return null;
        }
        
        Room updatedRoom = new Room();
        updatedRoom.setRoomId(roomIdField.getText().trim());
        updatedRoom.setType(roomTypeField.getText().trim());
        
        String ptId = ptIdField.getText().trim();
        String ptName = ptNameField.getText().trim();
        
        // Nếu cả hai đều trống, đặt null cho cả hai
        if (ptId.isEmpty() && ptName.isEmpty()) {
            updatedRoom.setPtId(null);
            updatedRoom.setPtName(null);
        } else {
            updatedRoom.setPtId(ptId);
            updatedRoom.setPtName(ptName);
        }
        
        try {
            updatedRoom.setPrice(Double.parseDouble(priceField.getText().trim()));
        } catch (NumberFormatException e) {
            displayErrorMessage("Invalid price format");
            return null;
        }
        
        try {
            String startDateText = startDateField.getText().trim();
            if (!startDateText.isEmpty()) {
                updatedRoom.setStartDate(dateFormat.parse(startDateText));
            } else {
                updatedRoom.setStartDate(null);
            }
            
            String dueDateText = dueDateField.getText().trim();
            if (!dueDateText.isEmpty()) {
                updatedRoom.setDueDate(dateFormat.parse(dueDateText));
            } else {
                updatedRoom.setDueDate(null);
            }
        } catch (ParseException e) {
            displayErrorMessage("Date parsing error: " + e.getMessage());
            return null;
        }
        
        updatedRoom.setNote(noteArea.getText().trim());
        
        return updatedRoom;
    }
    
    private void handleUpdate() {
        // Lấy thông tin đã cập nhật và đã được kiểm tra
        Room updatedRoom = getUpdatedRoomInfo();
        
        // Nếu thông tin không hợp lệ, dừng lại
        if (updatedRoom == null) {
            return;
        }
        
        // Double-check PT information trước khi lưu (thêm kiểm tra này để chắc chắn)
        String ptId = updatedRoom.getPtId();
        String ptName = updatedRoom.getPtName();
        
        if (ptId != null && !ptId.isEmpty()) {
            // Nếu có PT ID, kiểm tra lại với database
            PT pt = roomController.getPTById(ptId);
            if (pt == null) {
                displayErrorMessage("PT ID is not valid. Cannot update room information.");
                ptIdField.setBackground(new Color(255, 200, 200));
                return;
            }
            
            // Kiểm tra tên PT có khớp không
            if (ptName == null || !ptName.equals(pt.getPtName())) {
                displayErrorMessage("PT Name does not match with PT ID in database. Cannot update room information.");
                ptNameField.setBackground(new Color(255, 200, 200));
                return;
            }
        }
        
        // Check if room has been modified
        if (!roomController.isRoomModified(originalRoom, updatedRoom)) {
            displayErrorMessage("No changes made to room information.");
            return;
        }
        
        // Validate input (bước này vẫn cần để kiểm tra các ràng buộc khác)
        if (!roomController.validateInput(updatedRoom)) {
            displayErrorMessage("Invalid input. Please check your data.");
            return;
        }
        
        // Update room
        boolean updated = roomController.updateRoom(updatedRoom);
        
        if (updated) {
            displaySuccessMessage("Room information updated successfully.");
            parentView.refreshRoomTable(updatedRoom.getRoomId());
            dispose();
            parentView.setVisible(true);
        } else {
            displayErrorMessage("Failed to update room information.");
        }
    }
    
    private void handleCancel() {
        int response = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to cancel? Any changes will be lost.",
            "Confirm Cancel",
            JOptionPane.YES_NO_OPTION
        );
        
        if (response == JOptionPane.YES_OPTION) {
            dispose();
            parentView.setVisible(true);
        }
    }
    
    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }
    
    public void setCancelButtonListener(ActionListener listener) {
        cancelButton.addActionListener(listener);
    }
    
    public void displayErrorMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
    
    public void displaySuccessMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }
}
