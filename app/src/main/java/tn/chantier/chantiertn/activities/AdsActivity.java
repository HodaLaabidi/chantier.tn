package tn.chantier.chantiertn.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import tn.chantier.chantiertn.R;

public class AdsActivity extends AppCompatActivity {

    @BindView(R.id.web_view)
    WebView webView ;
    @BindView(R.id.progress_bar_ads)
    ProgressBar progressBarAds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ads);
        ButterKnife.bind(this);
        Intent intent = new Intent(Intent.ACTION_VIEW , Uri.parse("http://www.comaf.tn/"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }
}
