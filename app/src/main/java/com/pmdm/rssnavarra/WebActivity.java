package com.pmdm.rssnavarra;

import android.content.Intent;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Intent intent = this.getIntent();
        String link = intent.getStringExtra("LINK");

        mostrarDetalle(link);
    }

    /**
     * Mostrar web
     */
    public void mostrarDetalle(String strUrl) {
        WebView webView = (WebView)findViewById(R.id.WebViewNews);
        WebSettings webViewSettings = webView.getSettings();
        webViewSettings.setJavaScriptEnabled(true);
        webViewSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webViewSettings.setPluginState(WebSettings.PluginState.ON);
        webView.getSettings().setAllowFileAccess(true);
        webView.setSoundEffectsEnabled(true);
        webView.setWebViewClient(new SSLTolerentWebViewClient());
        webView.loadUrl(strUrl);
    }

    // SSL Error Tolerant Web View Client
    // http://stackoverflow.com/questions/7416096/android-webview-not-loading-https-url
    private class SSLTolerentWebViewClient extends WebViewClient {
        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed(); // Ignore SSL certificate errors
        }
    }
}
