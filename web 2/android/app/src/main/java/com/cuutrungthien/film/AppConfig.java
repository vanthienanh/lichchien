package com.cuutrungthien.film;

public class AppConfig {
    /**
     * ĐIỀN LINK WEB ONLINE CỦA BẠN TẠI ĐÂY:
     * Ví dụ: "https://cuutrungthien.vercel.app" hoặc "https://username.github.io/web2/"
     * 
     * - Nếu để giá trị "", ứng dụng sẽ tự động tải file index.html offline có sẵn trong app.
     * - Khi bạn đã đưa web lên host, chỉ cần dán link vào biến này: mọi thay đổi trên web
     *   sẽ lập tức hiển thị trên ứng dụng APK mỗi khi mở app mà không cần cài lại!
     */
    public static final String WEB_URL = "https://vanthienanh.github.io/web-cuutrungthien/";

    /**
     * Đường dẫn file offline dự phòng
     */
    public static final String FALLBACK_OFFLINE_URL = "file:///android_asset/index.html";

    /**
     * Bật/Tắt tính năng kéo xuống để làm mới (Pull-to-refresh)
     */
    public static final boolean ENABLE_PULL_TO_REFRESH = true;
}
