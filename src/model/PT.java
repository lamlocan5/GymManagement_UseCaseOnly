package model;

import java.util.Date;

public class PT {
    private String ptId;
    private String ptName;
    private String phoneNum;
    private Date dob;
    private String address;
    
    // Constructor mặc định
    public PT() {
    }
    
    // Constructor đầy đủ
    public PT(String ptId, String ptName, String phoneNum, Date dob, String address) {
        this.ptId = ptId;
        this.ptName = ptName;
        this.phoneNum = phoneNum;
        this.dob = dob;
        this.address = address;
    }
    
    // Getters và Setters
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
    
    public String getPhoneNum() {
        return phoneNum;
    }
    
    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }
    
    public Date getDob() {
        return dob;
    }
    
    public void setDob(Date dob) {
        this.dob = dob;
    }
    
    public String getAddress() {
        return address;
    }
    
    public void setAddress(String address) {
        this.address = address;
    }
}
