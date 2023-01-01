package com.forhad.languagestranslator;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import java.util.Calendar;
import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUs extends AppCompatActivity {
    SharedPreferences preferences;
    int switchcheck = 0;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        simulateDayNight(0);
        Element adsElement = new Element();
        adsElement.setTitle("Advertise with us");
        getSwitchState();
        setContentView(new AboutPage(this).isRTL(false).setImage(R.drawable.logo).setDescription(getResources().getString(R.string.description)).addItem(new Element().setTitle("Version 1.0")).addItem(adsElement).addGroup("Connect with us").addEmail("yourmail@gmail.com").addWebsite("").addYoutube("UCMC3qmkbshWceBJbvCLXuuw").addPlayStore(BuildConfig.APPLICATION_ID).addGitHub("LogicalGates").addItem(getCopyRightsElement()).create());
    }

    /* access modifiers changed from: package-private */
    public Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format(getString(R.string.copy_right), new Object[]{Integer.valueOf(Calendar.getInstance().get(1))});
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(Integer.valueOf(R.drawable.about_icon_copy_right));
        copyRightsElement.setIconTint(Integer.valueOf(R.color.about_item_icon_color));
        //copyRightsElement.setIconNightTint(17170443);
        copyRightsElement.setGravity(17);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(AboutUs.this, copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }

    /* access modifiers changed from: package-private */
    public void simulateDayNight(int currentSetting) {
        int currentNightMode = getResources().getConfiguration().uiMode & 48;
        if (currentSetting == 0 && currentNightMode != 16) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else if (currentSetting == 1 && currentNightMode != 32) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else if (currentSetting == 3) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(), TranslateActivity.class));
        finish();
    }

    public void getSwitchState() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.switchcheck = this.preferences.getInt("flag", 1);
    }
}
