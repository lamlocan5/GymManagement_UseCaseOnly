package controller;

import dao.PTDAO;
import dao.RoomDAO;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import model.PT;
import model.Room;

public class RoomController {
    private final RoomDAO roomDAO;
    private final PTDAO ptDAO;
    
    public RoomController() {
        this.roomDAO = new RoomDAO();
        this.ptDAO = new PTDAO();
    }
    
    // Tìm kiếm phòng theo ID
    public Room searchRoomById(String roomId) {
        try {
            return roomDAO.searchById(roomId);
        } catch (SQLException e) {
            System.err.println("Error searching room: " + e.getMessage());
            return null;
        }
    }
    
    // Tìm kiếm phòng theo một phần ID
    public List<Room> searchRoom(String partialId) {
        try {
            return roomDAO.searchByPartialId(partialId);
        } catch (SQLException e) {
            System.err.println("Error searching rooms: " + e.getMessage());
            return null;
        }
    }
    
    // Cập nhật phòng
    public boolean updateRoom(Room room) {
        try {
            // Kiểm tra ràng buộc dữ liệu trước khi cập nhật
            if (validateInput(room)) {
                return roomDAO.updateRoom(room);
            }
            return false;
        } catch (SQLException e) {
            System.err.println("Error updating room: " + e.getMessage());
            return false;
        }
    }
    
    // Kiểm tra ràng buộc dữ liệu đầu vào
    public boolean validateInput(Room room) {
        // Kiểm tra room id
        if (room.getRoomId() == null || room.getRoomId().trim().isEmpty()) {
            return false;
        }
        
        // Kiểm tra giá phải là số dương
        if (room.getPrice() < 0) {
            return false;
        }
        
        // Kiểm tra ngày kết thúc phải sau ngày bắt đầu
        if (room.getStartDate() != null && room.getDueDate() != null) {
            if (room.getDueDate().before(room.getStartDate())) {
                return false;
            }
        }
        
        // Kiểm tra PT ID và PT Name
        String ptId = room.getPtId();
        String ptName = room.getPtName();
        
        // Nếu một trong hai trống và cái còn lại không trống -> không hợp lệ
        if ((ptId == null || ptId.isEmpty()) && ptName != null && !ptName.isEmpty()) {
            return false;
        }
        if (ptId != null && !ptId.isEmpty() && (ptName == null || ptName.isEmpty())) {
            return false;
        }
        
        // Nếu cả hai có giá trị, kiểm tra tính hợp lệ
        if (ptId != null && !ptId.isEmpty()) {
            // Kiểm tra PT ID có tồn tại không
            PT pt = getPTById(ptId);
            if (pt == null) {
                return false;
            }
            
            // Kiểm tra PT Name có khớp với PT ID không
            if (!pt.getPtName().equals(ptName)) {
                return false;
            }
        }
        
        return true;
    }
        
    // Kiểm tra xem phòng có được thay đổi không
    public boolean isRoomModified(Room originalRoom, Room updatedRoom) {
        if (!Objects.equals(originalRoom.getType(), updatedRoom.getType())) {
            return true;
        }
        
        if (!Objects.equals(originalRoom.getPtId(), updatedRoom.getPtId())) {
            return true;
        }
        
        if (!Objects.equals(originalRoom.getPtName(), updatedRoom.getPtName())) {
            return true;
        }
        
        if (originalRoom.getPrice() != updatedRoom.getPrice()) {
            return true;
        }
        
        // Kiểm tra ngày bắt đầu
        if (originalRoom.getStartDate() != null && updatedRoom.getStartDate() != null) {
            if (!originalRoom.getStartDate().equals(updatedRoom.getStartDate())) {
                return true;
            }
        } else if (originalRoom.getStartDate() != updatedRoom.getStartDate()) {
            return true;
        }
        
        // Kiểm tra ngày kết thúc
        if (originalRoom.getDueDate() != null && updatedRoom.getDueDate() != null) {
            if (!originalRoom.getDueDate().equals(updatedRoom.getDueDate())) {
                return true;
            }
        } else if (originalRoom.getDueDate() != updatedRoom.getDueDate()) {
            return true;
        }
        
        if (!Objects.equals(originalRoom.getNote(), updatedRoom.getNote())) {
            return true;
        }
        
        return false;
    }
    
    // Lấy PT theo ID
    public PT getPTById(String ptId) {
        try {
            return ptDAO.getPTById(ptId);
        } catch (SQLException e) {
            System.err.println("Error retrieving PT: " + e.getMessage());
            return null;
        }
    }
    
    // Lấy tất cả PT
    public List<PT> getAllPTs() {
        try {
            return ptDAO.getAllPTs();
        } catch (SQLException e) {
            System.err.println("Error retrieving PTs: " + e.getMessage());
            return null;
        }
    }
    
    // Cập nhật tên PT khi ID PT thay đổi
    public String getPTNameById(String ptId) {
        if (ptId == null || ptId.isEmpty()) {
            return "";
        }
        
        PT pt = getPTById(ptId);
        return pt != null ? pt.getPtName() : "";
    }
}
