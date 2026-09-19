package com.cuutrungthien.film;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressBar;
    private FrameLayout customViewContainer;
    private View customView;
    private WebChromeClient.CustomViewCallback customViewCallback;

    private long lastBackPressTime = 0;
    private static final int BACK_PRESS_INTERVAL = 2000; // 2 giây để xác nhận thoát

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        webView = findViewById(R.id.webView);
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        progressBar = findViewById(R.id.progressBar);
        customViewContainer = findViewById(R.id.customViewContainer);

        setupWebView();
        setupSwipeRefresh();
        setupBackNavigation();

        loadContent();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setMediaPlaybackRequiresUserGesture(false);
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        settings.setSupportZoom(false);
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);

        // Tối ưu user agent cho mobile webview
        String userAgent = settings.getUserAgentString();
        settings.setUserAgentString(userAgent + " CuuTrungThienApp/1.0");

        webView.setWebViewClient(new CustomWebViewClient());
        webView.setWebChromeClient(new CustomWebChromeClient());
    }

    private void setupSwipeRefresh() {
        swipeRefreshLayout.setColorSchemeResources(R.color.primary);
        swipeRefreshLayout.setProgressBackgroundColorSchemeResource(R.color.surface);

        if (!AppConfig.ENABLE_PULL_TO_REFRESH) {
            swipeRefreshLayout.setEnabled(false);
            return;
        }

        swipeRefreshLayout.setOnRefreshListener(() -> {
            webView.reload();
        });

        // Chỉ cho phép kéo refresh khi webview đang ở đầu trang
        webView.getViewTreeObserver().addOnScrollChangedListener(() -> {
            if (webView.getScrollY() == 0) {
                swipeRefreshLayout.setEnabled(true);
            } else {
                swipeRefreshLayout.setEnabled(false);
            }
        });
    }

    private void setupBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // 1. Nếu đang xem video toàn màn hình thì thoát toàn màn hình trước
                if (customView != null) {
                    hideCustomView();
                    return;
                }

                // 2. Nếu webview có thể quay lại trang trước
                if (webView.canGoBack()) {
                    webView.goBack();
                    return;
                }

                // 3. Nhấn 2 lần để thoát ứng dụng
                if (System.currentTimeMillis() - lastBackPressTime < BACK_PRESS_INTERVAL) {
                    finish();
                } else {
                    lastBackPressTime = System.currentTimeMillis();
                    Toast.makeText(MainActivity.this, getString(R.string.exit_confirm), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void loadContent() {
        String targetUrl = AppConfig.WEB_URL;
        if (targetUrl != null && !targetUrl.trim().isEmpty()) {
            webView.loadUrl(targetUrl.trim());
        } else {
            webView.loadUrl(AppConfig.FALLBACK_OFFLINE_URL);
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            progressBar.setVisibility(View.GONE);
            swipeRefreshLayout.setRefreshing(false);
            super.onPageFinished(view, url);
        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            // Nếu load URL online bị mất mạng và đang ở trang chính, mở bản offline
            if (request.isForMainFrame() && !view.getUrl().startsWith("file:///android_asset/")) {
                Toast.makeText(MainActivity.this, "Không có kết nối mạng. Đang mở bản lưu offline...", Toast.LENGTH_LONG).show();
                view.loadUrl(AppConfig.FALLBACK_OFFLINE_URL);
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            Uri uri = request.getUrl();
            String scheme = uri.getScheme();

            if (scheme != null && (scheme.equals("http") || scheme.equals("https") || scheme.equals("file"))) {
                return false; // Mở trực tiếp trong WebView
            }

            // Xử lý các scheme khác như tel, mailto, zalo,...
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                return true;
            } catch (Exception ignored) {
                return true;
            }
        }
    }

    private class CustomWebChromeClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onShowCustomView(View view, CustomViewCallback callback) {
            // Khi xem video HTML5 toàn màn hình
            if (customView != null) {
                onHideCustomView();
                return;
            }

            customView = view;
            customViewCallback = callback;

            swipeRefreshLayout.setVisibility(View.GONE);
            customViewContainer.setVisibility(View.VISIBLE);
            customViewContainer.addView(view, new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));

            // Chuyển sang chế độ toàn màn hình & xoay ngang
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
        }

        @Override
        public void onHideCustomView() {
            hideCustomView();
        }

        @Override
        public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
            return super.onConsoleMessage(consoleMessage);
        }
    }

    private void hideCustomView() {
        if (customView == null) return;

        customViewContainer.removeView(customView);
        customView = null;
        customViewContainer.setVisibility(View.GONE);
        swipeRefreshLayout.setVisibility(View.VISIBLE);

        if (customViewCallback != null) {
            customViewCallback.onCustomViewHidden();
            customViewCallback = null;
        }

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (webView != null) webView.onResume();
    }

    @Override
    protected void onPause() {
        if (webView != null) webView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}
