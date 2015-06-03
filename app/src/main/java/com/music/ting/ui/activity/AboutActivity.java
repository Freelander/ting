package com.music.ting.ui.activity;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.music.ting.R;

/**
 * Created by Jun on 2015/6/1.
 */
public class AboutActivity extends BaseActivity {

    private static final String url = "https://github.com/Freelander";
    private Toolbar toolbar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        initToolbar();
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        getSupportActionBar().setTitle("About Me");

        final WebView webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);//设置能够使用执行JS脚本
        webView.getSettings().setBuiltInZoomControls(true);//支持使用缩放

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description,
                                        String failingUrl) {
                Toast.makeText(AboutActivity.this, "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });

        //点击返回键
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(webView.canGoBack()) //回退在WebView内发生
                webView.goBack();
                finish();
            }
        });

    }
}
