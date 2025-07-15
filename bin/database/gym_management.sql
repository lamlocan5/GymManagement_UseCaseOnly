CREATE DATABASE IF NOT EXISTS gym_management DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
USE gym_management;

-- Bảng Personal Trainers
CREATE TABLE IF NOT EXISTS personal_trainers (
    pt_id VARCHAR(50) PRIMARY KEY,
    pt_name VARCHAR(100) NOT NULL,
    phone_num VARCHAR(20),
    dob DATE,
    address TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Bảng Rooms
CREATE TABLE IF NOT EXISTS rooms (
    room_id VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50),
    pt_id VARCHAR(50),
    pt_name VARCHAR(100),
    price DOUBLE NOT NULL,
    start_date DATE,
    due_date DATE,
    note TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Insert dữ liệu mẫu vào bảng Personal Trainers
INSERT INTO personal_trainers (pt_id, pt_name, phone_num, dob, address) VALUES
('001', 'Do Ngoc Lam', '0912345678', '1990-05-15', 'Ha Noi'),
('002', 'Jane Doe', '0923456789', '1988-03-20', 'Ho Chi Minh City'),
('003', 'Mike Johnson', '0934567890', '1992-07-10', 'Da Nang'),
('004', 'Sarah Williams', '0945678901', '1995-11-22', 'Can Tho'),
('005', 'David Lee', '0956789012', '1987-09-18', 'Hai Phong');

-- Insert dữ liệu mẫu vào bảng Rooms với 12 phòng
INSERT INTO rooms (room_id, type, pt_id, pt_name, price, start_date, due_date, note) VALUES
-- Tầng 1
('101', 'standard', '001', 'Do Ngoc Lam', 800, '2024-01-01', '2024-12-31', 'Regular maintenance every 3 months'),
('102', 'premium', '001', 'Do Ngoc Lam', 1000, '2024-03-17', '2025-03-17', 'đã trả 1 nửa = 6000'),
('103', 'deluxe', '002', 'Jane Doe', 1500, '2024-02-15', '2024-08-15', 'VIP room with all equipment'),
('104', 'standard', NULL, NULL, 800, NULL, NULL, 'Available for rent'),

-- Tầng 2
('201', 'premium', '003', 'Mike Johnson', 1200, '2024-01-10', '2024-07-10', 'Specialized for yoga sessions'),
('202', 'standard', '003', 'Mike Johnson', 850, '2024-02-22', '2024-08-22', 'đã trả 3000'),
('203', 'deluxe', NULL, NULL, 1600, NULL, NULL, 'Recently renovated'),
('204', 'premium', '004', 'Sarah Williams', 1100, '2024-03-05', '2024-06-05', 'Short-term contract'),

-- Tầng 3
('301', 'standard', '005', 'David Lee', 820, '2024-01-15', '2024-07-15', 'Needs maintenance next month'),
('302', 'premium', '002', 'Jane Doe', 1050, '2024-02-01', '2025-02-01', 'Annual contract with discount'),
('303', 'standard', NULL, NULL, 830, NULL, NULL, 'Available from next week'),
('304', 'deluxe', '005', 'David Lee', 1700, '2024-03-10', '2024-09-10', 'Fully equipped for professional training');
