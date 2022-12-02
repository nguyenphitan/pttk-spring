# Website bán hàng và quản lí cho cửa hàng bán pizza

# Build Web Service Using

- Front-end: 
	* HTML/CSS
	* Javascrip ES6
	* Bootstrap 5
	* Jquery
- Back-end: 
	* Java Spring boot
	* Spring data jpa
	* Spring security
	* Lombok
	* Mysql database
	* Thymeleaf
	* Spring boot devtools

# Version

* JDK 8
* Spring 2.6.6

# IDE

* Eclipse 
* MYSQL Workbench

# 1. Mô tả dự án.
- Hệ thống quản lý bán hàng cho một cửa hàng bán bánh Pizza.
(*) Các chức năng:
- Khách hàng: 
	+ Đăng ký tài khoản, đăng nhập, đăng xuất.
	+ Chỉnh sửa thông tin cá nhân.
	+ Xem menu.
	+ Tìm kiếm món ăn theo tên.
	+ Lọc sản phẩm theo danh mục.
	+ Thêm giỏ hàng, thanh toán giỏ hàng.
	+ Thanh toán qua ví điện từ hoặc thanh toán khi nhận hàng.
	+ Xem lịch sử mua hàng, hủy đơn hàng.
	+ Chat với admin qua Zalo.

- Nhân viên:
	+ Gồm có các chức năng như khách hàng.
	+ Quản lý danh mục sản phẩm.
	+ Quản lý sản phẩm.
	+ Quản lý banner.
	+ Quản lý hóa đơn.
	
- Admin:
	+ Gồm có các chức năng của nhân viên.
	+ Quản lý mã giảm giá.
	+ Quản lý tài khoản.
	+ Quản lý nhân viên.
	+ Chấm công nhân viên.
	+ Tính lương nhân viên.
	+ Thống kê doanh thu theo tháng.

# 2. Nhiệm vụ - đóng góp của các thành viên trong team.
- Nguyễn Phi Tân: 
	+ Quản lý tài khoản.
	+ Quản lý nhân viên.
	+ Quản lý mã giảm giá.
	+ Thống kê doanh thu.
	+ Chấm công nhân viên.
	+ Tính lương nhân viên.
	+ Phân quyền: ADMIN, Nhân viên, Khách hàng.
	+ Sử dụng token định danh người dùng.
	+ Mã hóa mật khẩu bằng sercret key.
	+ Liên kết ví điện tử.
	+ Quản lý code của team, merge code.
	+ Test code.
	+ Fix bug.
	+ Viết báo cáo.

- Đỗ Công Ban:
	+ Dựng giao diện.
	+ Đăng nhập, đăng xuất.
	+ Đăng ký.
	+ Lọc sản phẩm.
	+ Tìm kiếm sản phẩm.
	+ Quản lý hóa đơn
	+ Quản lý banner.
	+ Quản lý giỏ hàng.
	+ Chỉnh sửa thông tin cá nhân
	+ Xem lịch sử mua hàng, hủy đơn hàng.
	+ Tích hợp chat qua Zalo cho hệ thống.
	+ Viết báo cáo.

- Phan Hoàng Nguyên:
	+ Quản lý danh mục sản phẩm.
	+ Quản lý sản phẩm.


# 3. Thiết kế hệ thống.
![image](https://user-images.githubusercontent.com/62367845/171392898-da2d0a1e-586a-4e46-887e-267e0bdef4a1.png)


# 4. Các màn hình của Khách Hàng.
### 4.1. Đăng ký tài khoản.
- Người dùng vào trang chủ -> đăng ký tài khoản -> nhập thông tin tài khoản -> nhấn đăng ký.
![image](https://user-images.githubusercontent.com/62367845/171393569-92ebe2e4-244e-4531-88c7-c9cce93b86f9.png)

### 4.2. Đăng nhập.
- Người dùng vào trang chủ -> đăng nhập -> nhập tài khoản, mật khẩu -> nhấn đăng nhập.
![image](https://user-images.githubusercontent.com/62367845/171393923-dbf03a5d-26c5-4590-b334-5f82d654edf6.png)

### 4.3. Lọc sản phẩm theo danh mục (theo loại).
- Chọn các danh mục được liệt kê trên thanh menu.
![image](https://user-images.githubusercontent.com/62367845/171395204-87a9e124-aa3d-4cef-9af9-6d6a17aea3bb.png)

### 4.4. Tìm kiếm sản phẩm theo tên.
- Nhấn nút tìm kiếm -> nhập tên sản phẩm muốn tìm.
![image](https://user-images.githubusercontent.com/62367845/171395517-576f85c4-d37c-4c26-96bc-02004ca70fbc.png)

### 4.5. Kiểm tra giỏ hàng.
- Xem nhanh giỏ hàng -> Di chuột vào giỏ hàng để xem nhanh giỏ hàng.
![image](https://user-images.githubusercontent.com/62367845/171396004-a1f774d2-18da-4bc8-8ac0-cfbddda58118.png)

- Nhấn vào mục Giỏ Hàng -> để xem chi tiết giỏ hàng.
![image](https://user-images.githubusercontent.com/62367845/171395822-7f792b51-4a4c-4227-9e7e-604ca87b173d.png)

### 4.6. Thanh toán.
#### 4.6.1. Thanh toán khi nhận hàng.
- Chọn phương thức thanh toán khi nhận hàng -> Nhấn thanh toán -> reset lại giỏ hàng.
![image](https://user-images.githubusercontent.com/62367845/171396310-4f80960b-2945-491a-8e4d-17f8c0c87617.png)

#### 4.6.2. Thanh toán online (qua ví điện tử VNPay).
- Chọn phương thức thanh toán qua ví điện tử -> Nhấn thanh toán -> Nhập thông tin vào đơn hàng -> Nhấn thanh toán 
 -> Nhập số thẻ -> Xác thực -> Nhập mã OTP -> Thanh toán -> Đơn hàng đã được thanh toán thành công.
![image](https://user-images.githubusercontent.com/62367845/171397311-e5331d1d-f7fa-4d6d-accc-65b05d15b34f.png)
![image](https://user-images.githubusercontent.com/62367845/171396783-ce8b8ef3-38b6-4855-849d-91508a83c4cf.png)
![image](https://user-images.githubusercontent.com/62367845/171397022-f3d5a551-f2ae-40e7-8731-83c880fdc286.png)
![image](https://user-images.githubusercontent.com/62367845/171397048-dde962c5-5e08-4936-9ce0-6799a1f30d93.png)
![image](https://user-images.githubusercontent.com/62367845/171397148-3da81a48-bdd8-4a6b-b41e-f41ecf821cc1.png)


### 4.7. Quản lý thông tin cá nhân.
- Xem thông tin cá nhân: Nhấn vào tên của mình hiển thị ở trên góc phải của trình duyệt để xem và cập nhật thông tin cá nhân.
![image](https://user-images.githubusercontent.com/62367845/171397696-e1a285a5-e49b-4170-be6d-addab35f0719.png)
![image](https://user-images.githubusercontent.com/62367845/171397818-cd32e785-70e6-4158-9a03-ed83faab2c7c.png)

### 4.8. Xem lịch sử mua hàng.
- Tại trang thông tin cá nhân -> Xem lịch sử mua hàng: Xem các đơn hàng đã mua - Hủy các đơn hàng chưa được giao.
![image](https://user-images.githubusercontent.com/62367845/171398178-79607d76-929d-4fe9-b343-acdcfe10dadf.png)

### 4.9. Chat qua Zalo với ADMIN.
- Khách hàng nhấn vào box chat bên phải màn hình -> chat qua Zalo với của hàng.
![image](https://user-images.githubusercontent.com/62367845/171402536-36b9b799-ffa9-4969-b3fc-c262eb16b4e4.png)


# 5. Các màn hình của Nhân Viên.
### 5.1. Trang chủ của nhân viên. (có thêm mục quản lý gian hàng)
![image](https://user-images.githubusercontent.com/62367845/171398566-d23d8358-83db-4bf0-868a-9feafaf5254c.png)

### 5.2. Quản lý gian hàng.
#### 5.2.1. Quản lý hóa đơn.
- Nhân viên nhấn sang mục quản lý hóa đơn để xem tình trạng của các đơn hàng.
![image](https://user-images.githubusercontent.com/62367845/171399135-e3370200-958f-40e7-8858-e2a6d74e0268.png)

#### 5.2.2. Quản lý danh mục sản phẩm.
- Xem các danh mục sản phẩm đang có.
![image](https://user-images.githubusercontent.com/62367845/171399272-0a74d323-b2d4-47e1-8468-c5cc56ecc8ab.png)

- Thêm mới danh mục sản phẩm.
![image](https://user-images.githubusercontent.com/62367845/171399508-27f6d8c9-8b75-48c4-bf74-363fd479b2a8.png)

- Sửa danh mục sản phẩm.
![image](https://user-images.githubusercontent.com/62367845/171400084-3b07994b-ec67-4b3d-bacd-8e66ed1eb697.png)

#### 5.2.3. Quản lý sản phẩm.
- Xem danh sách các sản phẩm đang có.
![image](https://user-images.githubusercontent.com/62367845/171400590-e8940a12-1680-4bfd-ae4a-fddc173dc980.png)

- Thêm mới sản phẩm.
![image](https://user-images.githubusercontent.com/62367845/171400911-5b0af52a-a708-453d-80bf-176bb3d14a7c.png)

- Sửa thông tin sản phẩm.
![image](https://user-images.githubusercontent.com/62367845/171401191-14ad1879-b133-4e5a-91b1-3b41a61e310e.png)

#### 5.2.4. Quản lý banner.
- Xem các banner nào đang được sử dụng, banner nào không được sử dụng.
![image](https://user-images.githubusercontent.com/62367845/171401579-f232a353-c3c8-4e2c-bc34-8b5270fec3af.png)

- Thêm mới banner.
![image](https://user-images.githubusercontent.com/62367845/171401734-5e1d72a9-af0d-4beb-92f0-4f6f2b31e9dc.png)


# 6. Các màn hình của ADMIN.
### 6.1. Trang quản lí của ADMIN.
![image](https://user-images.githubusercontent.com/62367845/171402858-8bcf0598-3e52-41cf-9b39-2d9e9a8e45f8.png)

### 6.2. Quản lý mã giảm giá.
![image](https://user-images.githubusercontent.com/62367845/171402988-050cedde-9de3-4d71-9797-71b485b49b7f.png)

- Thêm mới mã giảm giá.
![image](https://user-images.githubusercontent.com/62367845/171403134-de3f3591-bd7a-4af2-8179-9be22afb6013.png)

- Sửa mã giảm giá.
![image](https://user-images.githubusercontent.com/62367845/171403382-bd0baefe-b647-4d16-9df7-864ae47da93a.png)

- Xóa mã giảm giá.
![image](https://user-images.githubusercontent.com/62367845/171403273-732c98fb-b849-4f20-a7e1-c27d94353b6a.png)

### 6.3. Quản lý tài khoản.
- Admin xem danh sách các tài khoản: khách hàng, nhân viên.
- Admin cấp tài khoản nhân viên -> Nhấn nút thêm nhân viên.
![image](https://user-images.githubusercontent.com/62367845/171404146-e7716e35-3c59-4186-92c5-309ba3c64daf.png)

### 6.4. Quản lý nhân viên.
- Xem danh sách các nhân viên của cửa hàng.
- Admin có thể xóa nhân viên bằng cách nhấn nút: Xóa nhân viên.
![image](https://user-images.githubusercontent.com/62367845/171404505-8742b57c-0fe4-4e42-b951-5511970f5e9d.png)

### 6.5. Thống kê doanh thu.
- Admin xem thống kê doanh thu theo tháng.
- Chọn tháng muốn xem thống kê.
![image](https://user-images.githubusercontent.com/62367845/171404837-540e2f00-6e8e-43a2-8777-5fbb902ddd63.png)

### 6.6. Chấm công nhân viên.
- Admin xem bảng chấm công -> chấm công nhân viên.
![image](https://user-images.githubusercontent.com/62367845/171405060-63a23390-622b-4134-9df4-ece37df7a1d8.png)

### 6.7. Tính lương.
- Admin tính lương cho nhân viên.
- Đánh dấu trả lương cho nhân viên.
![image](https://user-images.githubusercontent.com/62367845/171405417-c9371057-f18f-4a0a-9d0b-e3026490a488.png)
![image](https://user-images.githubusercontent.com/62367845/171405485-52272de1-3887-41db-b8ad-8ea48af70a21.png)

