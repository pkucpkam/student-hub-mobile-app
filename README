# 🧑‍💻Subject: Mobile Apps Development

email: test@a.com
pass: 123456



* Trang xuất dữ liệu
	 - Làm bộ lọc: chọn đối tượng
		- Tài khoản : Xuất theo role
		- Người dùng: Xuất theo trạng thái
		- Chứng chỉ: Xuất theo người dùng
	- Chọn loại file 
	
	
	
*Database: 
	Table: Users
		user_id INT PRIMARY KEY,
		username VARCHAR(50) NOT NULL,
		password VARCHAR(100) NOT NULL,
		role VARCHAR,
		name VARCHAR(100),
		age INT,
		phone_number VARCHAR(15),
		status ENUM('Normal', 'Locked') DEFAULT 'Normal',
		profile_picture VARCHAR(255),
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	Table: Students 
		student_id INT PRIMARY KEY,
		name VARCHAR(100) NOT NULL,
		age INT,
		phone_number VARCHAR(15),
		email VARCHAR(100),
		address VARCHAR(255),
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
	Table: Certificates
		certificate_id INT PRIMARY KEY,
		student_id INT,
		certificate_name VARCHAR(100) NOT NULL,
		issue_date DATE,
		expiry_date DATE,
		created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
		FOREIGN KEY (student_id) REFERENCES Students(student_id)
	Table: LoginHistory
		login_id INT PRIMARY KEY,
		user_id INT,
		login_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
		logout_timestamp TIMESTAMP,
		FOREIGN KEY (user_id) REFERENCES Users(user_id)
