# Giải pháp ngăn chặn Lỗi dây chuyền (Cascading Failure)

Trong hệ thống phân tán (Microservices), **Cascading Failure (Lỗi dây chuyền)** xảy ra khi một service bị chậm hoặc sập (ví dụ: `Product Service`), khiến các service gọi đến nó (ví dụ: `Order Service`) phải chờ đợi (block thread), cạn kiệt tài nguyên (timeout, thread pool exhaustion) và cuối cùng cũng sập theo. 

Dưới đây là 4 giải pháp phổ biến để ngăn chặn rủi ro này:

## 1. Timeout Handling (Xử lý thời gian chờ)
- **Cơ chế**: Không bao giờ chờ đợi một API call mãi mãi. Cần cấu hình thời gian chờ tối đa (Read Timeout và Connect Timeout). Nếu quá thời gian này mà service đích chưa phản hồi, cuộc gọi sẽ lập tức bị hủy bỏ (abort) và ném ra lỗi Timeout.
- **Ưu điểm**: Giải phóng luồng (thread) nhanh chóng, ngăn chặn việc service gọi bị cạn kiệt thread pool do dồn ứ request.
- **Áp dụng**: Cấu hình timeout trực tiếp trên `RestTemplate` hoặc `WebClient`.

## 2. Retry Pattern (Cơ chế thử lại)
- **Cơ chế**: Thường đi kèm với Timeout. Đôi khi lỗi chỉ mang tính tạm thời (network glitch). Hệ thống sẽ tự động thử gọi lại API (retry) một số lần nhất định trước khi thực sự báo lỗi.
- **Ưu điểm**: Tăng tỷ lệ thành công cho các lỗi chập chờn (transient faults).
- **Lưu ý**: Cần kết hợp với độ trễ theo cấp số nhân (Exponential Backoff) để tránh tạo hiệu ứng DDoS (làm service đang yếu lại càng nhận nhiều request dồn dập).

## 3. Circuit Breaker (Cầu dao điện)
- **Cơ chế**: Hoạt động giống cầu dao điện trong nhà. Nếu phát hiện một service đích liên tục bị lỗi hoặc timeout quá ngưỡng cho phép (ví dụ: lỗi 50% trong 10 giây), Circuit Breaker sẽ "Mở" (Open) chốt. Mọi request gọi đến service đó sẽ bị chặn ngay lập tức (Fast Fail) mà không cần gọi mạng thực tế, giúp service đích có thời gian "thở" để phục hồi.
- **Ưu điểm**: Bảo vệ hệ thống khỏi sự sụp đổ diện rộng. Tự động kiểm tra (Half-Open) và đóng cầu dao lại (Close) khi service đích đã phục hồi.
- **Công cụ**: Resilience4j, Netflix Hystrix.

## 4. Fallback (Dữ liệu dự phòng)
- **Cơ chế**: Khi API gọi sang service đích bị lỗi (sập, timeout, circuit breaker open), thay vì văng lỗi cho người dùng, hệ thống trả về một dữ liệu mặc định (default data) hoặc dữ liệu lấy từ Cache cũ.
- **Ưu điểm**: Đảm bảo trải nghiệm người dùng không bị gián đoạn hoàn toàn (Graceful Degradation). Ví dụ: Nếu không lấy được giá sản phẩm từ Product Service, Order Service có thể dùng giá mặc định hoặc giá cũ lưu trong bộ nhớ tạm để hoàn tất đặt hàng.
- **Nhược điểm**: Dữ liệu có thể không chính xác (stale data) tại thời điểm đó.
