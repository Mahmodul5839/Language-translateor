package com.forhad.languagestranslator;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    /* access modifiers changed from: private */
    public static String YANDEX_URL = "https://translate.yandex.com/";

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_about);
        ((ImageView) findViewById(R.id.about_yandex)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AboutActivity.this.startActivity(new Intent("android.intent.action.VIEW", Uri.parse(AboutActivity.YANDEX_URL)));
            }
        });
    }
}
