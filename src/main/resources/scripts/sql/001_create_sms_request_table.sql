CREATE TABLE sms_requests_v1 (
     id INT PRIMARY KEY AUTO_INCREMENT,
     phone_number VARCHAR(20) NOT NULL,
     message TEXT NOT NULL,
     status VARCHAR(50),
     failure_code VARCHAR(50),
     failure_comments TEXT,
     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);