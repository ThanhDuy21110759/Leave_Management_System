# Leave Management System - Hệ thống quản lý lịch nghỉ của Nhân viên 
Chúng tôi mong muốn có một công cụ giúp nhân viên quản lý ngày nghỉ phép của mình ngày. Mỗi thành viên sẽ được mặc định có `12 ngày nghỉ phép` theo quy định của pháp luật. Hệ thống cho phép họ gửi yêu cầu nghỉ phép (có ngày, lý do...) cho người quản lý của họ. Người quản lý có thể từ chối hoặc phê duyệt yêu cầu nghỉ phép. Cuối năm, số đơn xin nghỉ phép còn lại sẽ được cộng dồn sang năm sau.

<strong>Main function</strong>
- Thêm request (employee, admin) 
- Check status request (employee: seen, admin: modified)
- Login/logout (employee, admin)
- Accept/reject request (admin)

<strong>Technology</strong>
- FE: ReactJs
- BE: Java Spring Boot build REST API, Spring security, Spring data (JPA)
- DB: MySQL

<strong>Set up database</strong>

`Step 1:` create database employee_leave;

`Step 2:` 

- INSERT INTO roles(name) VALUES('ROLE_EMPLOYEE');
- INSERT INTO roles(name) VALUES('ROLE_ADMIN');


<strong>Details</strong>

`/api/auth/signin`: login với token {ROLE_ADMIN, ROLE_EMPLOYEE}

    {
      "username": "admin",
    
      "password": "12345678"
    }

`/api/auth/signup`: Đăng ký user {ROLE_ADMIN, ROLE_EMPLOYEE}

    {
      "username": "admin",
    
      "password": "12345678",
    
      "email": "chutich.dev@gmail.com",
    
      "role": ["admin"]
    }

`/api/admin/request`: {POST} accept/ reject request

`/api/admin/requests`: {GET} lấy ds các request của employee

`/api/admin/requests/process`: {GET} lấy ds request request đang trong quá trình xử lý

`/api/admin/requests/accept`: {GET} lấy ds request request đã được xử lý

`/api/admin/requests/reject`: {GET} lấy ds request request đã bị tữ chối

`/api/employee/request`: {POST, PUT, DELETE} thêm, sửa, xóa request

    /api/employee/request?id={value}: format xóa request (trạng thái Process)

`/api/employee/requests`: {GET} lấy ds request cá nhân


<strong>Note API</strong>

1. `/api/admin/request`: thực hiện update remaining time sau khi accept

2. `/api/employee/request`: thực hiện kiểm tra (ngày trùng với old request, remaing time >= diff)

3. `/api/employee/request`: thực hiện kiểm tra request đang được xử lý
