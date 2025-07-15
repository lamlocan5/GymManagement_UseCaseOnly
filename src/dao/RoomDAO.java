package dao;

import model.Room;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RoomDAO {
    
    // Tìm phòng theo ID chính xác
    public Room searchById(String roomId) throws SQLException {
        Room room = null;
        String query = "SELECT * FROM rooms WHERE room_id = ?";
        
        try (Connection conn = Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, roomId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                room = new Room();
                room.setRoomId(rs.getString("room_id"));
                room.setType(rs.getString("type"));
                room.setPtId(rs.getString("pt_id"));
                room.setPtName(rs.getString("pt_name"));
                room.setPrice(rs.getDouble("price"));
                room.setStartDate(rs.getDate("start_date"));
                room.setDueDate(rs.getDate("due_date"));
                room.setNote(rs.getString("note"));
            }
        }
        
        return room;
    }
    
    // Cập nhật thông tin phòng
    public boolean updateRoom(Room room) throws SQLException {
        String query = "UPDATE rooms SET type = ?, pt_id = ?, pt_name = ?, " +
                "price = ?, start_date = ?, due_date = ?, note = ? " +
                "WHERE room_id = ?";
        
        try (Connection conn = Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, room.getType());
            stmt.setString(2, room.getPtId());
            stmt.setString(3, room.getPtName());
            stmt.setDouble(4, room.getPrice());
            
            if (room.getStartDate() != null) {
                stmt.setDate(5, new java.sql.Date(room.getStartDate().getTime()));
            } else {
                stmt.setNull(5, java.sql.Types.DATE);
            }
            
            if (room.getDueDate() != null) {
                stmt.setDate(6, new java.sql.Date(room.getDueDate().getTime()));
            } else {
                stmt.setNull(6, java.sql.Types.DATE);
            }
            
            stmt.setString(7, room.getNote());
            stmt.setString(8, room.getRoomId());
            
            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        }
    }
    
    // Kiểm tra ràng buộc dữ liệu phòng
    public boolean validateRoom(Room room) {
        // Kiểm tra ID không được null
        if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra giá phải >= 0
        if (room.getPrice() < 0) {
            return false;
        }
        
        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (room.getStartDate() != null && room.getDueDate() != null) {
            if (room.getDueDate().before(room.getStartDate())) {
                return false;
            }
        }
        
        return true;
    }
    
    // Tìm danh sách phòng theo một phần ID
    public List<Room> searchByPartialId(String partialId) throws SQLException {
        List<Room> rooms = new ArrayList<>();
        String query = "SELECT * FROM rooms WHERE room_id LIKE ?";
        
        try (Connection conn = Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, "%" + partialId + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getString("room_id"));
                room.setType(rs.getString("type"));
                room.setPtId(rs.getString("pt_id"));
                room.setPtName(rs.getString("pt_name"));
                room.setPrice(rs.getDouble("price"));
                room.setStartDate(rs.getDate("start_date"));
                room.setDueDate(rs.getDate("due_date"));
                room.setNote(rs.getString("note"));
                
                rooms.add(room);
            }
        }
        
        return rooms;
    }
}
