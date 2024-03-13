# Leave Management System - Hệ thống quản lý lịch nghỉ của Nhân viên 
Chúng tôi mong muốn có một công cụ giúp nhân viên quản lý ngày nghỉ phép của mình ngày. Mỗi thành viên sẽ được mặc định có `12 ngày nghỉ phép` theo quy định của pháp luật. Hệ thống cho phép họ gửi yêu cầu nghỉ phép (có ngày, lý do...) cho người quản lý của họ. Người quản lý có thể từ chối hoặc phê duyệt yêu cầu nghỉ phép. Cuối năm, số đơn xin nghỉ phép còn lại sẽ được cộng dồn sang năm sau.

Main function:
- Thêm request (employee, admin) 
- Check status request (employee: seen, admin: modified)
- Login/logout (employee, admin)
- Accept/reject request (admin)

Schedule:

Technology:
- FE: ReactJs
- BE: Java Spring Boot build REST API, Spring security, Spring data (JPA)
- DB: MySQL
- Deployment: Render, Clever Cloud
