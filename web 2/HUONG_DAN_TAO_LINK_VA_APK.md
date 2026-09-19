# HƯỚNG DẪN: CÁCH TẠO LINK WEB VÀ XUẤT TỆP APK TỰ ĐỘNG CẬP NHẬT

---

## 💡 NGUYÊN LÝ HOẠT ĐỘNG
Để tệp APK **tự động thay đổi ngay khi bạn sửa code web**:
* Tệp APK là một ứng dụng Android thông minh (WebView) trỏ trực tiếp đến đường link Web Online của bạn.
* **Lợi ích**: Bạn chỉ cần cài tệp APK vào điện thoại **1 lần duy nhất**. Bất kỳ khi nào bạn chỉnh sửa file `index.html` trên máy chủ, người dùng chỉ cần mở app APK lên (hoặc vuốt nhẹ màn hình xuống) là **nội dung mới sẽ xuất hiện ngay lập tức**, không cần gửi file APK mới hay cài đặt lại!

---

## BƯỚC 1: ĐƯA WEB LÊN ONLINE MIỄN PHÍ (CHỈ MẤT 1 PHÚT)

Bạn có thể chọn **1 trong 2 cách** sau:

### Cách A: Dùng Vercel (Nhanh nhất - Không cần biết code)
1. Truy cập [vercel.com](https://vercel.com) và đăng nhập (bằng tài khoản Google hoặc GitHub).
2. Kéo thả cả thư mục chứa file `index.html` vào màn hình Vercel (hoặc chọn "Add New Project" -> Import).
3. Bấm **Deploy**.
4. Bạn sẽ nhận được ngay 1 đường link miễn phí dạng: `https://cuutrungthien.vercel.app`.

---

### Cách B: Dùng GitHub Pages (Khuyên dùng - Tự động 100%)
1. Đăng nhập vào [github.com](https://github.com), bấm **New repository** (Tạo kho lưu trữ mới), đặt tên ví dụ: `web-cuu-trung-thien`.
2. Đẩy toàn bộ thư mục này lên repository đó.
3. Vào mục **Settings** của repository trên GitHub -> Chọn thẻ **Pages** ở cột trái -> Tại mục **Build and deployment**, chọn **GitHub Actions**.
4. GitHub sẽ tự động:
   - Triển khai website thành link online: `https://<ten_tai_khoan>.github.io/<ten_repo>/`.
   - Đồng thời tự động build luôn **tệp APK** sẵn sàng cho bạn tải về!

---

## BƯỚC 2: CẤU HÌNH LINK VÀO DỰ ÁN ANDROID

1. Mở tệp sau:
   👉 [`android/app/src/main/java/com/cuutrungthien/film/AppConfig.java`](file:///e:/OneDrive/web/web%202/android/app/src/main/java/com/cuutrungthien/film/AppConfig.java)
2. Thay đường link web của bạn vào dòng `WEB_URL`:
   ```java
   public static final String WEB_URL = "https://cuutrungthien.vercel.app"; // Dán link web của bạn vào đây
   ```
3. Lưu lại.

---

## BƯỚC 3: XUẤT TỆP APK ĐỂ CÀI VÀO ĐIỆN THOẠI

Bạn có thể chọn 1 trong các cách sau:

### Cách 1: Tải file APK trực tiếp từ GitHub (Không cần cài Android Studio)
* Nếu bạn dùng GitHub ở Bước 1:
* Vào repository trên GitHub -> Bấm thẻ **Actions**.
* Bấm vào lần chạy mới nhất -> Ở mục **Artifacts** phía dưới, bạn sẽ thấy tệp **`CuuTrungThien-Film-APK`** -> Bấm vào để tải tệp `.apk` về điện thoại và cài đặt ngay!

### Cách 2: Xuất tệp APK bằng Android Studio (Nếu máy bạn có cài)
1. Mở **Android Studio**.
2. Chọn **Open** -> Trỏ tới thư mục: `e:\OneDrive\web\web 2\android`.
3. Chờ Android Studio đồng bộ xong (khoảng 1-2 phút).
4. Vào menu: **Build** -> **Build Bundle(s) / APK(s)** -> **Build APK(s)**.
5. Sau khi build xong, bấm vào nút **locate** để lấy tệp `app-debug.apk` chép vào điện thoại.

---

## 📱 CÁC TÍNH NĂNG NỔI BẬT TRÊN TỆP APK NÀY:
1. **Xem phim toàn màn hình (Fullscreen Video)**: Khi bấm xem phim và phóng to, app tự động xoay ngang màn hình và ẩn thanh trạng thái.
2. **Kéo xuống để cập nhật (Pull to refresh)**: Khi bạn vừa sửa code web, chỉ cần dùng ngón tay kéo từ đỉnh màn hình xuống để tải lại bản mới nhất.
3. **Nút Back thông minh**: Bấm nút Back trên điện thoại để lùi lại trang trước; nhấn 2 lần liên tiếp để xác nhận thoát app.
4. **Offline Fallback**: Nếu lỡ mất mạng, app có cơ chế tự mở trang thông báo dự phòng mượt mà.
