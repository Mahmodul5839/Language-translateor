package com.forhad.languagestranslator;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_home);
        AlphaAnimation animation = new AlphaAnimation(0.0f, 1.0f);
        animation.setFillAfter(true);
        animation.setDuration(1200);
        ((LinearLayout) findViewById(R.id.home_container)).startAnimation(animation);
        ((Button) findViewById(R.id.start_new_conversation)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, ConversationActivity.class));
            }
        });
        ((Button) findViewById(R.id.start_new_translation)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, TranslateActivity.class));
            }
        });
        ((Button) findViewById(R.id.about)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                HomeActivity.this.startActivity(new Intent(HomeActivity.this, AboutActivity.class));
            }
        });
    }
}
