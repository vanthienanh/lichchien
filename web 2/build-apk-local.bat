@echo off
chcp 65001 > nul
echo ========================================================
echo   CỬU TRÙNG THIÊN FILM - TỰ ĐỘNG BIÊN DỊCH TỆP APK
echo ========================================================
echo.

cd /d "%~dp0android"

if exist gradlew.bat (
    echo Đang biên dịch tệp APK bằng Gradle...
    call gradlew.bat assembleDebug
) else (
    echo Đang kiểm tra gradle trên hệ thống...
    gradle assembleDebug
)

if %ERRORLEVEL% EQU 0 (
    echo.
    echo ========================================================
    echo   [THÀNH CÔNG] Tệp APK đã được tạo tại:
    echo   android\app\build\outputs\apk\debug\app-debug.apk
    echo ========================================================
    pause
) else (
    echo.
    echo [LƯU Ý] Nếu máy tính chưa cài Android SDK hoặc Java JDK:
    echo Bạn có thể dùng GitHub Actions miễn phí (xem hướng dẫn trong HD_SU_DUNG.md)
    echo để tải file APK về mà không cần cài đặt gì trên máy tính!
    echo.
    pause
)
