package com.forhad.languagestranslator;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private static final int DRAW_OVER_OTHER_APP_PERMISSION = 1;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView((int) R.layout.activity_splash);
        getWindow().addFlags(1024);
        askForSystemOverlayPermission();
        startActivityMainDelay();
    }

    private void startActivityMainDelay() {
        new Timer().schedule(new TimerTask() {
            public void run() {
                SplashActivity.this.startActivity(new Intent(SplashActivity.this, TranslateActivity.class));
                SplashActivity.this.finish();
            }
        }, 2500);
    }

    private void askForSystemOverlayPermission() {
        if (Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            startActivityForResult(new Intent("android.settings.action.MANAGE_OVERLAY_PERMISSION", Uri.parse("package:" + getPackageName())), 1);
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && Build.VERSION.SDK_INT >= 23 && !Settings.canDrawOverlays(this)) {
            finish();
        }
    }
}
