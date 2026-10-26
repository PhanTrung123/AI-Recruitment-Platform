# 🚀 AI Recruitment Platform

Hệ thống nền tảng tuyển dụng thông minh tích hợp AI, kết nối trực tiếp ứng viên và nhà tuyển dụng. Dự án được xây dựng bằng **Spring Boot**, **Spring Security** và **Thymeleaf**, hỗ trợ phân quyền chặt chẽ và tự động hóa quy trình sàng lọc hồ sơ.

---

## 🌟 Tính năng nổi bật

### 🤖 Tính năng AI (Trí tuệ nhân tạo)
* **Phân tích & Đánh giá CV tự động:** Trích xuất thông tin kỹ năng, kinh nghiệm từ CV của ứng viên.
* **Gợi ý việc làm thông minh (Job Matching):** Đề xuất công việc phù hợp dựa trên hồ sơ ứng viên và yêu cầu của nhà tuyển dụng.

### 👥 Phân quyền người dùng (Role-based Access Control)
Hệ thống được bảo mật ở tầng Backend với **Spring Security**, chia làm 3 phân quyền chính:

1. **👤 Ứng viên (`USER` / Candidate):**
   * Đăng ký, đăng nhập và quản lý hồ sơ cá nhân (Profile & CV).
   * Tìm kiếm, lọc công việc theo ngành nghề, mức lương, địa điểm.
   * Ứng tuyển công việc và theo dõi trạng thái hồ sơ.
   * Nhận gợi ý việc làm phù hợp từ AI.

2. **🏢 Nhà tuyển dụng (`RECRUITER`):**
   * Quản lý thông tin công ty.
   * Đăng tin tuyển dụng, cập nhật và đóng/mở các chiến dịch tuyển dụng.
   * Quản lý danh sách ứng viên ứng tuyển, xem điểm đánh giá mức độ phù hợp từ AI.

3. **🛡️ Quản trị viên (`ADMIN`):**
   * Truy cập trang quản trị hệ thống chuyên biệt (`/admin`).
   * Quản lý toàn bộ người dùng (`USER`, `RECRUITER`), khóa/mở khóa tài khoản.
   * Kiểm duyệt bài đăng tuyển dụng và theo dõi thống kê hệ thống.
   * *Bảo mật:* Mọi truy cập trái phép từ `USER` hoặc `RECRUITER` vào đường dẫn `/admin/**` đều bị chặn ở tầng Backend và điều hướng về trang cảnh báo `403 Forbidden`.

---

## 🛠️ Công nghệ sử dụng (Tech Stack)

* **Backend:** Java `[17 / 21]`, Spring Boot `[3.x]`
* **Bảo mật:** Spring Security 6, Thymeleaf Extras Spring Security
* **Frontend / Template Engine:** Thymeleaf, HTML5, CSS3, JavaScript, `[Bootstrap / Tailwind CSS]`
* **Cơ sở dữ liệu:** `[MySQL / PostgreSQL / SQL Server]`, Spring Data JPA (Hibernate)
* **Tích hợp AI:** `[Điền API AI bạn dùng: ví dụ OpenAI API / Gemini API / Spring AI]`
* **Công cụ quản lý & Build:** Maven, Git

---

## 📂 Cấu trúc thư mục chính

```text
AI-Recruitment-Platform/
├── src/
│   ├── main/
│   │   ├── java/com/quangtrung/HireAI/
│   │   │   ├── annotation/     # Chứa các custom annotation tùy chỉnh
│   │   │   ├── config/         # Cấu hình bảo mật, bean, web MVC...
│   │   │   ├── controller/     # Các Controller điều hướng request (Admin, Recruiter, User, Auth...)
│   │   │   ├── dto/            # Các Data Transfer Object truyền tải dữ liệu
│   │   │   ├── entity/         # Các class Entity ánh xạ bảng Database (JPA)
│   │   │   ├── enums/          # Các kiểu dữ liệu liệt kê (Role, Status...)
│   │   │   ├── exception/      # Xử lý ngoại lệ tập trung của ứng dụng
│   │   │   ├── repository/     # Giao tiếp Database với Spring Data JPA
│   │   │   ├── security/       # Cấu hình Spring Security, Authentication/Authorization
│   │   │   └── service/        # Giao diện tầng nghiệp vụ (Service) và gói impl (thực thi)
│   │   └── resources/
│   │       ├── static/          # Chứa tài nguyên tĩnh (CSS, JS, Images)
│   │       ├── templates/       # Giao diện Thymeleaf (.html), bao gồm trang lỗi 403.html
│   │       └── application.properties # Cấu hình kết nối Database, cổng port và tham số hệ thống
└── pom.xml                      # Quản lý thư viện và dependencies Maven

```
---

## ⚙️ Hướng dẫn cài đặt và chạy dự án

### 1. Yêu cầu hệ thống
* **JDK:** Phiên bản `[17]` trở lên.
* **Maven:** 3.8+
* **Database:** `[MySQL / PostgreSQL]`

### 2. Các bước cài đặt

**Bước 1: Clone dự án về máy**
```bash
git clone [https://github.com/PhanTrung123/AI-Recruitment-Platform.git](https://github.com/PhanTrung123/AI-Recruitment-Platform.git)
cd AI-Recruitment-Platform
```

**Bước 2: Cấu hình Cơ sở dữ liệu & API Key**
Mở file `src/main/resources/application.properties` (hoặc `application.yml`) và cập nhật thông tin kết nối của bạn:

```properties
# Cấu hình Database
spring.datasource.url=jdbc:mysql://localhost:3306/ai_recruitment_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update

# Cấu hình AI API Key (nếu có)
ai.api.key=your_ai_api_key_here
```

**Bước 3: Chạy ứng dụng**
Sử dụng Maven Wrapper có sẵn trong dự án:
```bash
./mvnw spring-boot:run
```
Hoặc chạy trực tiếp file class chính chứa `@SpringBootApplication` trên IDE (IntelliJ IDEA, Eclipse, VS Code).

**Bước 4: Truy cập ứng dụng**
* Trang chủ: `http://localhost:8080/`
* Trang đăng nhập: `http://localhost:8080/login`
* Trang quản trị (Dành riêng cho `ADMIN`): `http://localhost:8080/admin`

---

## 🔐 Tài khoản thử nghiệm (Demo Accounts)

| Vai trò (Role) | Tài khoản (Username/Email) | Mật khẩu | Quyền hạn truy cập |
| :--- | :--- | :--- | :--- |
| **ADMIN** | `admin@gmail.com` | `123456` | Toàn quyền hệ thống, truy cập `/admin/**` |
| **RECRUITER** | `recruiter@gmail.com` | `123456` | Đăng tin, quản lý CV (Bị chặn ở `/admin`) |
| **USER** | `user@gmail.com` | `123456` | Tìm việc, nộp CV (Bị chặn ở `/admin`) |

---

## 👨‍💻 Tác giả

* **Phan Trung** - [GitHub Profile](https://github.com/PhanTrung123)

Nếu bạn thấy dự án hữu ích, hãy cho dự án một ⭐ trên GitHub nhé!
