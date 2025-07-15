package dao;

import model.PT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PTDAO {
    
    // Lấy PT theo ID
    public PT getPTById(String ptId) throws SQLException {
        PT pt = null;
        String query = "SELECT * FROM personal_trainers WHERE pt_id = ?";
        
        try (Connection conn = Connector.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            
            stmt.setString(1, ptId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                pt = new PT();
                pt.setPtId(rs.getString("pt_id"));
                pt.setPtName(rs.getString("pt_name"));
                pt.setPhoneNum(rs.getString("phone_num"));
                pt.setDob(rs.getDate("dob"));
                pt.setAddress(rs.getString("address"));
            }
        }
        
        return pt;
    }
    
    // Lấy tất cả PT
    public List<PT> getAllPTs() throws SQLException {
        List<PT> pts = new ArrayList<>();
        String query = "SELECT * FROM personal_trainers";
        
        try (Connection conn = Connector.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            while (rs.next()) {
                PT pt = new PT();
                pt.setPtId(rs.getString("pt_id"));
                pt.setPtName(rs.getString("pt_name"));
                pt.setPhoneNum(rs.getString("phone_num"));
                pt.setDob(rs.getDate("dob"));
                pt.setAddress(rs.getString("address"));
                
                pts.add(pt);
            }
        }
        
        return pts;
    }
}
