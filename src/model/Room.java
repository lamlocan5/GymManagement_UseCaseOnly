package model;

import java.util.Date;

public class Room {
    private String roomId;
    private String type;
    private String ptId;
    private String ptName;
    private double price;
    private Date startDate;
    private Date dueDate;
    private String note;
    
    // Constructor mặc định
    public Room() {
    }
    
    // Constructor đầy đủ
    public Room(String roomId, String type, String ptId, String ptName, 
                double price, Date startDate, Date dueDate, String note) {
        this.roomId = roomId;
        this.type = type;
        this.ptId = ptId;
        this.ptName = ptName;
        this.price = price;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.note = note;
    }
    
    // Getters và Setters
    public String getRoomId() {
        return roomId;
    }
    
    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getPtId() {
        return ptId;
    }
    
    public void setPtId(String ptId) {
        this.ptId = ptId;
    }
    
    public String getPtName() {
        return ptName;
    }
    
    public void setPtName(String ptName) {
        this.ptName = ptName;
    }
    
    public double getPrice() {
        return price;
    }
    
    public void setPrice(double price) {
        this.price = price;
    }
    
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    public Date getDueDate() {
        return dueDate;
    }
    
    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
    
    public String getNote() {
        return note;
    }
    
    public void setNote(String note) {
        this.note = note;
    }
}
