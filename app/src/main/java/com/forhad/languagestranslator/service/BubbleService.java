package com.forhad.languagestranslator.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.IBinder;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;

import com.forhad.languagestranslator.R;
import com.forhad.languagestranslator.TranslateActivity;


public class BubbleService extends Service {
    boolean activity_background;
    private ImageView counterFab;
    private ImageView mButtonClose;
    /* access modifiers changed from: private */
    public View mOverlayView;
    int mWidth;
    /* access modifiers changed from: private */
    public WindowManager mWindowManager;
    private ImageView openButton;

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        final WindowManager.LayoutParams params;
        if (intent != null) {
            this.activity_background = intent.getBooleanExtra("activity_background", false);
        }
        if (this.mOverlayView == null) {
            this.mOverlayView = LayoutInflater.from(this).inflate(R.layout.overlay_layout, (ViewGroup) null);
            if (Build.VERSION.SDK_INT >= 26) {
                params = new WindowManager.LayoutParams(-2, -2, 2038, 8, -3);
            } else {
                params = new WindowManager.LayoutParams(-2, -2, 2002, 8, -3);
            }
            params.gravity = 51;
            params.x = 0;
            params.y = 100;
            this.mWindowManager = (WindowManager) getSystemService("window");
            this.mWindowManager.addView(this.mOverlayView, params);
            Display display = this.mWindowManager.getDefaultDisplay();
            final Point size = new Point();
            display.getSize(size);
            this.counterFab = (ImageView) this.mOverlayView.findViewById(R.id.fabHead);
            this.mButtonClose = (ImageView) this.mOverlayView.findViewById(R.id.closeButton);
            this.openButton = (ImageView) this.mOverlayView.findViewById(R.id.openButton);
            final RelativeLayout layout = (RelativeLayout) this.mOverlayView.findViewById(R.id.layout);
            layout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                public void onGlobalLayout() {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    int width = layout.getMeasuredWidth();
                    BubbleService.this.mWidth = size.x - width;
                }
            });
            this.openButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent myIntent = new Intent(BubbleService.this, TranslateActivity.class);
                    myIntent.setFlags(268435456);
                    BubbleService.this.startActivity(myIntent);
                }
            });
            this.mButtonClose.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    BubbleService.this.stopSelf();
                }
            });
            this.counterFab.setOnTouchListener(new View.OnTouchListener() {
                private float initialTouchX;
                private float initialTouchY;
                private int initialX;
                private int initialY;

                @SuppressLint({"ClickableViewAccessibility"})
                public boolean onTouch(View v, MotionEvent event) {
                    int action = event.getAction();
                    if (action == 0) {
                        this.initialX = params.x;
                        this.initialY = params.y;
                        this.initialTouchX = event.getRawX();
                        this.initialTouchY = event.getRawY();
                        return true;
                    } else if (action == 1) {
                        if (BubbleService.this.activity_background) {
                            float xDiff = event.getRawX() - this.initialTouchX;
                            float yDiff = event.getRawY() - this.initialTouchY;
                            if (Math.abs(xDiff) < 5.0f && Math.abs(yDiff) < 5.0f) {
                                Intent intent = new Intent(BubbleService.this.getBaseContext(), TranslateActivity.class);
                                intent.addFlags(268435456);
                                BubbleService.this.startActivity(intent);
                            }
                            BubbleService.this.stopSelf();
                        }
                        params.x = (int) (params.x >= BubbleService.this.mWidth / 2 ? (float) BubbleService.this.mWidth : 0.0f);
                        BubbleService.this.mWindowManager.updateViewLayout(BubbleService.this.mOverlayView, params);
                        return true;
                    } else if (action != 2) {
                        return false;
                    } else {
                        int xDiff2 = Math.round(event.getRawX() - this.initialTouchX);
                        int yDiff2 = Math.round(event.getRawY() - this.initialTouchY);
                        WindowManager.LayoutParams layoutParams = params;
                        layoutParams.x = this.initialX + xDiff2;
                        layoutParams.y = this.initialY + yDiff2;
                        BubbleService.this.mWindowManager.updateViewLayout(BubbleService.this.mOverlayView, params);
                        return true;
                    }
                }
            });
        }
        return super.onStartCommand(intent, flags, startId);
    }

    public void onCreate() {
        super.onCreate();
        setTheme(R.style.Theme_LanguagesTranslator);
    }

    public void onDestroy() {
        super.onDestroy();
        View view = this.mOverlayView;
        if (view != null) {
            this.mWindowManager.removeView(view);
        }
    }
}
