# Thiết kế Kiến trúc Phân rã (Decomposition) Hệ thống E-commerce

## 1. Mục đích và Phạm vi
Hệ thống E-commerce đã được tách từ kiến trúc Monolithic (tất cả trong một project `order-service`) thành 3 Microservices độc lập để đảm bảo Single Responsibility Principle (SRP) và dễ dàng scale (mở rộng) trong tương lai:
1. **customer-service (Port 8081)**: Quản lý thông tin khách hàng.
2. **product-service (Port 8082)**: Quản lý thông tin sản phẩm, giá cả và tồn kho.
3. **order-service (Port 8083)**: Quản lý giao dịch mua hàng.

---

## 2. Ranh giới thực thể (Bounded Contexts)

### 2.1 Customer Service
- **Entity**: `Customer`
- **Thuộc tính**:
  - `id` (Long, PK)
  - `fullName` (String)
  - `email` (String)
  - `password` (String)
  - `address` (String)
- **Database**: `customer_db`

### 2.2 Product Service
- **Entity**: `Product`
- **Thuộc tính**:
  - `id` (Long, PK)
  - `name` (String)
  - `price` (Double)
  - `stockQuantity` (Integer)
  - `description` (String)
- **Database**: `product_db`

### 2.3 Order Service
- **Entity**: `Order`
- **Thuộc tính**:
  - `id` (Long, PK)
  - `customerId` (Long) - **Foreign Key (Logical)**
  - `productId` (Long) - **Foreign Key (Logical)**
  - `orderDate` (LocalDateTime)
  - `totalAmount` (Double)
- **Database**: `order_db`
- **Lưu ý**: Order Service sử dụng `ExternalCustomerDTO` và `ExternalProductDTO` để hứng dữ liệu giao tiếp thông qua HTTP API (sử dụng `RestTemplate`) chứ không chứa entity domain của các service khác.

---

## 3. Tại sao không sử dụng liên kết @JoinColumn (khóa ngoại vật lý) giữa Order và Customer/Product?

Trong kiến trúc Microservices, việc thiết kế dữ liệu giữa các domain phải thỏa mãn tính **Lỏng lẻo (Loosely Coupled)** và **Độc lập (Independent)**. Lý do chính bao gồm:

1. **Tránh Data Duplication & Synchronization (Trùng lặp và đồng bộ dữ liệu)**:
   Nếu `Order` lưu nguyên object `Customer` (gồm name, email, address...), mỗi khi khách hàng đổi địa chỉ ở `Customer Service`, hệ thống sẽ phải tìm kiếm và update lại tất cả các Order của khách hàng đó bên `Order Service`. Bằng cách chỉ lưu `customerId` (tham chiếu), `Order Service` luôn có thể fetch dữ liệu khách hàng chính xác nhất thông qua API khi cần.

2. **Giới hạn trách nhiệm (Single Source of Truth)**:
   `Customer Service` là nơi duy nhất sở hữu và quản lý dữ liệu khách hàng. `Order Service` không nên có khả năng (hoặc có dữ liệu gốc) để sửa đổi hay quản lý khách hàng.

3. **Giảm thiểu coupling (Sự phụ thuộc vòng)**:
   Việc lưu các Entity ngoại lai trực tiếp sẽ biến hệ thống thành một "Distributed Monolith" – các service bị dính chặt vào cấu trúc bảng của nhau. Lưu `customerId` giúp `order-service` chỉ cần quan tâm "ID người mua là ai" thay vì "Người mua có thông tin cấu trúc như thế nào".

4. **Tối ưu hóa Database và Memory**:
   Bảng `orders` sẽ rất nặng nếu phải lưu dư thừa dữ liệu `Customer` và `Product` bên trong từng bản ghi.

## 4. Biểu đồ giao tiếp (Communication Flow)
Khi tạo mới một Order:
1. Client gửi `OrderRequestDTO` tới `Order Service`.
2. `Order Service` gọi HTTP GET tới `Customer Service` để xác thực `customerId`.
3. `Order Service` gọi HTTP GET tới `Product Service` để lấy giá và kiểm tra tồn kho cho `productId`.
4. `Order Service` tính `totalAmount` = `price * quantity` và lưu `Order` vào DB.
