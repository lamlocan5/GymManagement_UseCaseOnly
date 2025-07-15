package main;

import javax.swing.*;
import view.ManageHomeView;
import view.SearchRoomView;

public class GymManagementMain {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        SwingUtilities.invokeLater(() -> {
            ManageHomeView homeView = new ManageHomeView();
            
            // Thiết lập action listener cho nút Update Room
            homeView.setManageRoomButtonListener(e -> {
                homeView.setVisible(false);
                SearchRoomView searchView = new SearchRoomView(homeView);
                searchView.setVisible(true);
            });
            
            homeView.setVisible(true);
        });
    }
}
