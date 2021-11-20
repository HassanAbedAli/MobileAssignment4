package com.example.mobileassignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class StoryDetail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_detail);
        getInfoAndLoadView();
    }


    public void getInfoAndLoadView(){

        Intent intent = getIntent();

        String name = intent.getStringExtra("name");
        String url = intent.getStringExtra("url");


        WebView view = (WebView) findViewById(R.id.webView);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new WebViewClient());
        //
        view.loadUrl(url);
        // }
}
}