package com.forhad.languagestranslator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.autofit.et.lib.AutoFitEditText;
import com.forhad.languagestranslator.Customization.AutoResizeTextView;
import com.forhad.languagestranslator.Models.LanguageModel;
import com.forhad.languagestranslator.SpinnerAdapter.LanguagesAdapter;
import com.forhad.languagestranslator.SpinnerAdapter.SpinnerCustomAdapter;
import com.forhad.languagestranslator.service.BubbleService;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.android.material.navigation.NavigationView;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.mannan.translateapi.Language;
import com.mannan.translateapi.TranslateAPI;
import com.mindorks.paracamera.Camera;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;

public class TranslateActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    private static final int CAMERA_PERMISSION_CODE = 1;
    public static final String LOG_TAG = TranslateActivity.class.getName();
    private static final int REQ_CODE_SPEECH_INPUT = 1;
    private static final String TAG = "OCR";
    volatile boolean activityRunning;
    //AdView adView1;
   // AdView adView2;
    Bitmap bitmap;
    Button button_translate;
    Camera camera;
    ImageButton clearbtn;
    ImageButton copyBtn;
    DrawerLayout drawerLayout;
    int drawerpos = 0;
    List<LanguageModel> filterData = new ArrayList();
    int firstspinnerpos;
    String firsttxt = "How are you and what are you doing";
    SwitchCompat floatSwitch;
    Integer[] icons = {Integer.valueOf(R.drawable.africaans), Integer.valueOf(R.drawable.albania), Integer.valueOf(R.drawable.arabic), Integer.valueOf(R.drawable.armenia), Integer.valueOf(R.drawable.az), Integer.valueOf(R.drawable.eu), Integer.valueOf(R.drawable.belarius), Integer.valueOf(R.drawable.bangal), Integer.valueOf(R.drawable.bulgaria), Integer.valueOf(R.drawable.ca), Integer.valueOf(R.drawable.cn), Integer.valueOf(R.drawable.hr), Integer.valueOf(R.drawable.cz), Integer.valueOf(R.drawable.danish), Integer.valueOf(R.drawable.dutch), Integer.valueOf(R.drawable.f_eng), Integer.valueOf(R.drawable.et), Integer.valueOf(R.drawable.filipino), Integer.valueOf(R.drawable.finish), Integer.valueOf(R.drawable.fr), Integer.valueOf(R.drawable.gl), Integer.valueOf(R.drawable.georgian), Integer.valueOf(R.drawable.german), Integer.valueOf(R.drawable.gr), Integer.valueOf(R.drawable.gu), Integer.valueOf(R.drawable.ht), Integer.valueOf(R.drawable.il), Integer.valueOf(R.drawable.in), Integer.valueOf(R.drawable.hu), Integer.valueOf(R.drawable.is), Integer.valueOf(R.drawable.id), Integer.valueOf(R.drawable.ga), Integer.valueOf(R.drawable.it), Integer.valueOf(R.drawable.jp), Integer.valueOf(R.drawable.kn), Integer.valueOf(R.drawable.kr), Integer.valueOf(R.drawable.la), Integer.valueOf(R.drawable.lv), Integer.valueOf(R.drawable.lt), Integer.valueOf(R.drawable.mk), Integer.valueOf(R.drawable.ms), Integer.valueOf(R.drawable.mt), Integer.valueOf(R.drawable.no), Integer.valueOf(R.drawable.ir), Integer.valueOf(R.drawable.pl), Integer.valueOf(R.drawable.pt), Integer.valueOf(R.drawable.ro), Integer.valueOf(R.drawable.ru), Integer.valueOf(R.drawable.sr), Integer.valueOf(R.drawable.sk), Integer.valueOf(R.drawable.slovenua), Integer.valueOf(R.drawable.spanish), Integer.valueOf(R.drawable.sv), Integer.valueOf(R.drawable.swedish), Integer.valueOf(R.drawable.tg), Integer.valueOf(R.drawable.th), Integer.valueOf(R.drawable.tr), Integer.valueOf(R.drawable.turkish), Integer.valueOf(R.drawable.ukranian), Integer.valueOf(R.drawable.pk), Integer.valueOf(R.drawable.vn), Integer.valueOf(R.drawable.gb_wls), Integer.valueOf(R.drawable.pg)};
    ImageView image_swap;
    String langcode = Language.URDU;
    List<LanguageModel> languageData = new ArrayList();
    LanguagesAdapter languagesAdapter;
    Locale loc;
   //AdView mAdView;
    ImageButton mImageSpeak;
    private boolean noTranslate;
    String[] opions = {"All Languages", "Rate Me", "About This App", "Try More Apps", "Share App"};
    Integer[] opions_icons_white = {Integer.valueOf(R.drawable.language_translator), Integer.valueOf(R.drawable.star_filled), Integer.valueOf(R.drawable.about_us), Integer.valueOf(R.drawable.app_filled), Integer.valueOf(R.drawable.share_filled)};
    OptionsAdapter optionsAdapter;
    ListView optionsLV;
    ImageView picture;
    SharedPreferences preferences;
    /* access modifiers changed from: private */
    public Dialog process_tts;
    int secondspinnerpos = 0;
    String secondtxt = "آپ کیسی ہیں اور کیا کر رہی ہیں۔";
    TextView setting;
    ImageButton sharebtn;
    ImageButton speakInputBtn;
    ImageButton speakTranslatedBtn;
    Spinner spinner_language_from;
    Spinner spinner_language_to;
    String[] split_language_arr;
    int switchcheck;
    ImageButton takepicBtn;
    /* access modifiers changed from: private */
    public TextToSpeech textToSpeech;
    AutoFitEditText text_input;
    AutoResizeTextView text_translated;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_translate);
        getWindow().addFlags(1024);
        setupLayout();
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        checkPermission("android.permission.CAMERA", 1);
        /*MobileAds.initialize(this, getResources().getString(R.string.app_id));
        this.mAdView = (AdView) findViewById(R.id.adView);
        this.mAdView.loadAd(new AdRequest.Builder().build());
        this.adView1 = (AdView) findViewById(R.id.adView1);
        this.adView1.loadAd(new AdRequest.Builder().build());
        this.adView2 = (AdView) findViewById(R.id.adView2);
        this.adView2.loadAd(new AdRequest.Builder().build());

       */
        this.process_tts = new Dialog(this);
        this.process_tts.setContentView(R.layout.dialog_processing_tts);
        this.process_tts.setTitle(getString(R.string.process_tts));
        this.floatSwitch = (SwitchCompat) findViewById(R.id.floatSwitch);
        SwitchCompat floatSwitch2 = (SwitchCompat) findViewById(R.id.floatSwitch);
        this.takepicBtn = (ImageButton) findViewById(R.id.takepicBtn);
        this.copyBtn = (ImageButton) findViewById(R.id.copyBtn);
        this.picture = (ImageView) findViewById(R.id.picture);
        this.mImageSpeak = (ImageButton) findViewById(R.id.mImageSpeak);
        this.speakInputBtn = (ImageButton) findViewById(R.id.speakInputBtn);
        this.speakTranslatedBtn = (ImageButton) findViewById(R.id.speakTranslatedBtn);
        this.sharebtn = (ImageButton) findViewById(R.id.sharebtn);
        this.clearbtn = (ImageButton) findViewById(R.id.clearbtn);
        this.optionsLV = (ListView) findViewById(R.id.optionsLV);
        this.button_translate = (Button) findViewById(R.id.button_translate);
        this.image_swap = (ImageView) findViewById(R.id.image_swap);
        this.text_input = (AutoFitEditText) findViewById(R.id.text_input);
        this.text_translated = (AutoResizeTextView) findViewById(R.id.text_translated);
        this.spinner_language_from = (Spinner) findViewById(R.id.spinner_language_from);
        this.spinner_language_to = (Spinner) findViewById(R.id.spinner_language_to);
        this.setting = (TextView) findViewById(R.id.setting);
        getSwitchState();
        setSpinners();
        this.spinner_language_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    TranslateActivity.this.langcode = Languages.langCodesEN[i];
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        this.textToSpeech = new TextToSpeech(this, this);
        this.text_input.setText(this.firsttxt);
        this.text_translated.setText(this.secondtxt);
        if (this.switchcheck == 1) {
            floatSwitch2.setChecked(true);
        } else {
            floatSwitch2.setChecked(false);
        }
        floatSwitch2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    TranslateActivity translateActivity = TranslateActivity.this;
                    translateActivity.switchcheck = 1;
                    translateActivity.storeSwicthState();
                    return;
                }
                TranslateActivity translateActivity2 = TranslateActivity.this;
                translateActivity2.switchcheck = 0;
                translateActivity2.storeSwicthState();
            }
        });
        try {
            Camera.Builder directory = new Camera.Builder().resetToCorrectOrientation(true).setTakePhotoRequestCode(1).setDirectory("pics");
            this.camera = directory.setName("ali_" + System.currentTimeMillis()).setImageFormat(Camera.IMAGE_JPEG).setCompression(75).setImageHeight(1000).build((Activity) this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.takepicBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    TranslateActivity.this.camera.takePicture();
                } catch (Exception e) {
                    TranslateActivity.this.checkPermission("android.permission.CAMERA", 1);
                    e.printStackTrace();
                }
            }
        });
        this.copyBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                if (TranslateActivity.this.text_translated.getText().toString().equals("")) {
                    TranslateActivity.this.text_translated.setError("Nothing to copy");
                } else {
                    ((ClipboardManager) TranslateActivity.this.getSystemService("clipboard")).setPrimaryClip(ClipData.newPlainText("chat", TranslateActivity.this.text_translated.getText().toString()));
                    Toast.makeText(TranslateActivity.this, "copied to clipboard", Toast.LENGTH_SHORT).show();
                }
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.mImageSpeak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int ttspos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                intent.putExtra("android.speech.extra.LANGUAGE", Languages.getLangCodesTTS()[ttspos]);
                intent.putExtra("android.speech.extra.PROMPT", TranslateActivity.this.getString(R.string.speech_prompt));
                try {
                    TranslateActivity.this.startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Log.d(NotificationCompat.CATEGORY_MESSAGE, TranslateActivity.this.getString(R.string.language_not_supported));
                }
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        this.speakInputBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int ttspos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                if (Languages.getLangCodesTTS()[ttspos].length() == 5) {
                    TranslateActivity.this.split_language_arr = Languages.getLangCodesTTS()[ttspos].split("-");
                    TranslateActivity translateActivity = TranslateActivity.this;
                    translateActivity.loc = new Locale(translateActivity.split_language_arr[0], TranslateActivity.this.split_language_arr[1]);
                }
                if (TranslateActivity.this.text_input.getText().toString().equals("")) {
                    TranslateActivity.this.text_input.setError("Nothing to speak");
                } else {
                    TranslateActivity.this.textToSpeech.setLanguage(TranslateActivity.this.loc);
                    TextToSpeech access$000 = TranslateActivity.this.textToSpeech;
                    String obj = TranslateActivity.this.text_input.getText().toString();
                    TextToSpeech unused = TranslateActivity.this.textToSpeech;
                    access$000.speak(obj, 0, (HashMap) null);
                }
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.speakTranslatedBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                int ttspos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                if (Languages.getLangCodesTTS()[ttspos].length() == 5) {
                    TranslateActivity.this.split_language_arr = Languages.getLangCodesTTS()[ttspos].split("-");
                    TranslateActivity translateActivity = TranslateActivity.this;
                    translateActivity.loc = new Locale(translateActivity.split_language_arr[0], TranslateActivity.this.split_language_arr[1]);
                }
                if (TranslateActivity.this.text_translated.getText().toString().equals("")) {
                    TranslateActivity.this.text_translated.setError("Nothing to speak");
                } else {
                    TranslateActivity.this.textToSpeech.setLanguage(TranslateActivity.this.loc);
                    TextToSpeech access$000 = TranslateActivity.this.textToSpeech;
                    String charSequence = TranslateActivity.this.text_translated.getText().toString();
                    TextToSpeech unused = TranslateActivity.this.textToSpeech;
                    access$000.speak(charSequence, 0, (HashMap) null);
                }
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.clearbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TranslateActivity.this.text_input.getText().toString().equals("")) {
                    TranslateActivity.this.text_input.setError("Nothing to clear");
                } else {
                    TranslateActivity.this.text_input.setText("");
                }
            }
        });
        this.sharebtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (TranslateActivity.this.text_translated.getText().toString().equals("")) {
                    TranslateActivity.this.text_translated.setError("Nothing to share");
                } else {
                    TranslateActivity translateActivity = TranslateActivity.this;
                    translateActivity.shareText(translateActivity.text_translated.getText().toString());
                }
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.optionsAdapter = new OptionsAdapter();
        this.optionsLV.setAdapter(this.optionsAdapter);
        this.optionsLV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TranslateActivity translateActivity = TranslateActivity.this;
                translateActivity.drawerpos = i;
                translateActivity.optionsAdapter.notifyDataSetChanged();
                if (i == 0) {
                    TranslateActivity.this.showDialog();
                    TranslateActivity.this.drawerLayout.closeDrawer((int) GravityCompat.START);
                }
                if (i == 1) {
                    TranslateActivity.this.rateapp();
                    TranslateActivity.this.drawerLayout.closeDrawer((int) GravityCompat.START);
                }
                if (i == 2) {
                    try {
                        TranslateActivity.this.startActivity(new Intent(TranslateActivity.this.getApplicationContext(), AboutUs.class));
                        TranslateActivity.this.finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    TranslateActivity.this.drawerLayout.closeDrawer((int) GravityCompat.START);
                }
                if (i == 3) {
                    TranslateActivity.this.moreapps();
                    TranslateActivity.this.drawerLayout.closeDrawer((int) GravityCompat.START);
                }
                if (i == 4) {
                    TranslateActivity.this.shareApp();
                    TranslateActivity.this.drawerLayout.closeDrawer((int) GravityCompat.START);
                }
            }
        });
        this.image_swap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                int sourceLng = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                TranslateActivity.this.spinner_language_from.setSelection(TranslateActivity.this.spinner_language_to.getSelectedItemPosition());
                TranslateActivity.this.spinner_language_to.setSelection(sourceLng);
                TranslateActivity.this.text_input.setText(TranslateActivity.this.text_translated.getText().toString());
                TranslateActivity translateActivity = TranslateActivity.this;
                translateActivity.translate(translateActivity.text_input.getText().toString().trim());
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        this.button_translate.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            public void onClick(View view) {
                try {
                    ((InputMethodManager) TranslateActivity.this.getSystemService("input_method")).hideSoftInputFromWindow(TranslateActivity.this.getCurrentFocus().getWindowToken(), 0);
                } catch (Exception e) {
                }
                TranslateActivity translateActivity = TranslateActivity.this;
                translateActivity.translate(translateActivity.text_input.getText().toString().trim());
                try {
                    TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                    TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                    TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                    TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                    TranslateActivity.this.storeSpinnerState();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public void shareText(String text) {
        try {
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType("text/plain");
            shareIntent.putExtra("android.intent.extra.SUBJECT", "Languages Translator v-2020");
            shareIntent.putExtra("android.intent.extra.TEXT", text);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setupLayout() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ((NavigationView) findViewById(R.id.navigation)).setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                TranslateActivity.this.drawerLayout.closeDrawers();
                return true;
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.content_description_open_drawer, R.string.content_description_close_drawer) {
            public void onDrawerOpened(View drawerView) {
                TranslateActivity.this.getSupportActionBar().setTitle((CharSequence) "");
                super.onDrawerOpened(drawerView);
            }

            public void onDrawerClosed(View drawerView) {
                TranslateActivity.this.getSupportActionBar().setTitle((CharSequence) "");
                super.onDrawerClosed(drawerView);
            }
        };
        this.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toggle.setDrawerIndicatorEnabled(false);
        toggle.setHomeAsUpIndicator((int) R.drawable.menu);
        if (Build.VERSION.SDK_INT >= 23) {
            toggle.getDrawerArrowDrawable().setColor(getColor(R.color.colorPrimaryDark));
        } else {
            toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                TranslateActivity.this.drawerLayout.openDrawer((int) GravityCompat.START);
            }
        });
    }

    public void setSpinners() {
        List<String> categories = new ArrayList<>();
        Collections.addAll(categories, Languages.getLangsEN());
        new ArrayAdapter<>(this, 17367048, categories).setDropDownViewResource(17367049);
        SpinnerCustomAdapter spinnerCustomAdapter = new SpinnerCustomAdapter(this, categories, this.icons);
        this.spinner_language_from.setAdapter(spinnerCustomAdapter);
        this.spinner_language_from.setSelection(this.firstspinnerpos);
        this.spinner_language_to.setAdapter(spinnerCustomAdapter);
        this.spinner_language_to.setSelection(this.secondspinnerpos);
    }

    public void textChangedListener() {
        RxTextView.textChanges(this.text_input).filter($$Lambda$TranslateActivity$62tX3KEziZeSDbleQV4O65XZwvQ.INSTANCE).debounce(500, TimeUnit.MILLISECONDS).subscribe(new Action1<CharSequence>() {
            public void call(CharSequence charSequence) {
                TranslateActivity.this.translate(charSequence.toString().trim());
            }
        });
        RxTextView.textChanges(this.text_translated).filter($$Lambda$TranslateActivity$mwVGQys_cqYJPSYmYHwtFMi_Vek.INSTANCE).subscribe(new Action1<CharSequence>() {
            public void call(CharSequence charSequence) {
                TranslateActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                    }
                });
            }
        });
    }

    static /* synthetic */ Boolean lambda$textChangedListener$0(CharSequence charSequence) {
        return Boolean.valueOf(charSequence.length() > 0);
    }

    static /* synthetic */ Boolean lambda$textChangedListener$1(CharSequence charSequence) {
        return Boolean.valueOf(charSequence.length() == 0);
    }

    public String langCode(String selectedLang) {
        String code = null;
        for (int i = 0; i < Languages.getLangsEN().length; i++) {
            if (selectedLang.equals(Languages.getLangsEN()[i])) {
                code = Languages.getLangCodeEN(i);
            }
        }
        return code;
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == -1 && data != null) {
            final Dialog match_text_dialog = new Dialog(this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle(getString(R.string.select_matching_text));
            ListView textlist = (ListView) match_text_dialog.findViewById(R.id.list);
            final ArrayList<String> matches_text = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            textlist.setAdapter(new ArrayAdapter<>(this, 17367043, matches_text));
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    TranslateActivity.this.text_input.setText((CharSequence) matches_text.get(position));
                    TranslateActivity.this.translate(((String) matches_text.get(position)).trim());
                    match_text_dialog.dismiss();
                }
            });
            match_text_dialog.show();
        }
        if (requestCode == Camera.REQUEST_TAKE_PHOTO) {
            this.bitmap = this.camera.getCameraBitmap();
            Bitmap bitmap2 = this.bitmap;
            if (bitmap2 != null) {
                this.picture.setImageBitmap(bitmap2);
                processImage();
                return;
            }
            Toast.makeText(this, "Picture not taken!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onInit(int status) {
        int ttspos = this.spinner_language_from.getSelectedItemPosition();
        Log.e("Inside----->", "onInit");
        TextToSpeech textToSpeech2 = this.textToSpeech;
        if (status == 0) {
            int result = textToSpeech2.setLanguage(new Locale(Languages.getLangCodesTTS()[ttspos]));
            TextToSpeech textToSpeech3 = this.textToSpeech;
            if (result == -1) {
                Log.d(NotificationCompat.CATEGORY_MESSAGE, getString(R.string.language_pack_missing));
            } else if (result == -2) {
                Log.d(NotificationCompat.CATEGORY_MESSAGE, getString(R.string.language_not_supported));
            }
            this.mImageSpeak.setEnabled(true);
            this.textToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                public void onStart(String utteranceId) {
                    Log.e("Inside", "OnStart");
                    TranslateActivity.this.process_tts.hide();
                }

                public void onDone(String utteranceId) {
                }

                public void onError(String utteranceId) {
                }
            });
            return;
        }
        Log.e(LOG_TAG, "TTS Initilization Failed");
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        TextToSpeech textToSpeech2 = this.textToSpeech;
        if (textToSpeech2 != null) {
            textToSpeech2.stop();
        }
        if (this.switchcheck == 1) {
            try {
                startService(new Intent(this, BubbleService.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        super.onPause();
    }

    public void onDestroy() {
        TextToSpeech textToSpeech2 = this.textToSpeech;
        if (textToSpeech2 != null) {
            textToSpeech2.stop();
            this.textToSpeech.shutdown();
        }
        this.activityRunning = false;
        this.process_tts.dismiss();
        this.camera.deleteImage();
        super.onDestroy();
    }

    /* access modifiers changed from: private */
    public void translate(String text) {
        if (this.noTranslate) {
            this.noTranslate = false;
        } else {
            new TranslateAPI(Language.AUTO_DETECT, this.langcode, text).setTranslateListener(new TranslateAPI.TranslateListener() {
                public void onSuccess(String translatedText) {
                    Log.d("TAGRAZA", "onSuccess: " + translatedText);
                    TranslateActivity.this.text_translated.setText(translatedText);
                    try {
                        TranslateActivity.this.firstspinnerpos = TranslateActivity.this.spinner_language_from.getSelectedItemPosition();
                        TranslateActivity.this.secondspinnerpos = TranslateActivity.this.spinner_language_to.getSelectedItemPosition();
                        TranslateActivity.this.firsttxt = TranslateActivity.this.text_input.getText().toString();
                        TranslateActivity.this.secondtxt = TranslateActivity.this.text_translated.getText().toString();
                        TranslateActivity.this.storeSpinnerState();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                public void onFailure(String ErrorText) {
                    Log.d("TAGRAZA", "onFailure: " + ErrorText);
                }
            });
        }
    }

    public class OptionsAdapter extends BaseAdapter {
        public OptionsAdapter() {
        }

        public int getCount() {
            return TranslateActivity.this.opions.length;
        }

        public Object getItem(int i) {
            return TranslateActivity.this.opions[i];
        }

        public long getItemId(int i) {
            return (long) i;
        }

        public View getView(int i, View view, ViewGroup viewGroup) {
            View rowView = View.inflate(TranslateActivity.this, R.layout.drawer_categories_cell, (ViewGroup) null);
            LinearLayout cell = (LinearLayout) rowView.findViewById(R.id.cell);
            TextView optionname = (TextView) rowView.findViewById(R.id.optionname);
            optionname.setText(TranslateActivity.this.opions[i]);
            optionname.setCompoundDrawablesWithIntrinsicBounds(TranslateActivity.this.opions_icons_white[i].intValue(), 0, 0, 0);
            if (TranslateActivity.this.drawerpos == i) {
                cell.setBackgroundResource(R.drawable.buttonshapeagain);
            }
            return rowView;
        }
    }

    /* access modifiers changed from: private */
    public void rateapp() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + getPackageName())));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_SHORT).show();
        }
    }

    /* access modifiers changed from: private */
    public void shareApp() {
        try {
            Intent shareIntent = new Intent("android.intent.action.SEND");
            shareIntent.setType("text/plain");
            shareIntent.putExtra("android.intent.extra.SUBJECT", "Resume Builder v-2019");
            shareIntent.putExtra("android.intent.extra.TEXT", "\nEasiest way to create your resume\n\n" + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n");
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void moreapps() {
        try {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://search?q=pub:technicdude")));
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/developer?id=technicdude")));
        }
    }

    public void showDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_rv);
        dialog.setCancelable(true);
        dialog.getWindow().setLayout((getWidth(this) / 100) * 99, -2);
        dialog.getWindow().setGravity(17);
        dialog.getWindow().setSoftInputMode(4);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        RecyclerView languagesRV = (RecyclerView) dialog.findViewById(R.id.langyagesRV);
        languagesRV.setLayoutManager(new LinearLayoutManager(this));
        for (int i = 0; i < this.icons.length; i++) {
            LanguageModel languageModel = new LanguageModel();
            languageModel.setIcon(this.icons[i]);
            languageModel.setTitle(Languages.getLangsEN()[i]);
            this.languageData.add(languageModel);
            this.filterData.add(languageModel);
        }
        this.languagesAdapter = new LanguagesAdapter(this, this.languageData);
        languagesRV.setAdapter(this.languagesAdapter);
        final EditText searchEdt = (EditText) dialog.findViewById(R.id.searchEdt);
        searchEdt.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (TranslateActivity.this.languageData.size() > 0) {
                    TranslateActivity.this.languageData.clear();
                }
                TranslateActivity.this.filter(searchEdt.getText().toString());
            }

            public void afterTextChanged(Editable editable) {
            }
        });
        dialog.show();
    }

    public void filter(String text) {
        this.languageData.clear();
        if (text.length() == 0) {
            this.languageData.clear();
        }
        for (int i = 0; i < this.filterData.size(); i++) {
            if (this.filterData.get(i).getTitle().toLowerCase().contains(text)) {
                LanguageModel languageModel = new LanguageModel();
                languageModel.setIcon(this.filterData.get(i).getIcon());
                languageModel.setTitle(this.filterData.get(i).getTitle());
                if (!this.languageData.contains(languageModel.getTitle())) {
                    this.languageData.add(languageModel);
                }
            }
            this.languagesAdapter.notifyDataSetChanged();
        }
    }

    @SuppressLint("WrongConstant")
    public static int getWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private void processImage() {
        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();
        if (textRecognizer.isOperational()) {
            Log.d(TAG, "processImage: started");
            SparseArray<TextBlock> items = textRecognizer.detect(new Frame.Builder().setBitmap(this.bitmap).build());
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < items.size(); i++) {
                stringBuilder.append(items.valueAt(i).getValue());
                stringBuilder.append("\n");
            }
            this.text_input.setText(stringBuilder.toString());
            translate(stringBuilder.toString().trim());
            try {
                this.firstspinnerpos = this.spinner_language_from.getSelectedItemPosition();
                this.secondspinnerpos = this.spinner_language_to.getSelectedItemPosition();
                this.firsttxt = this.text_input.getText().toString();
                this.secondtxt = this.text_translated.getText().toString();
                storeSpinnerState();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Log.d(TAG, "processImage: not operational");
        }
    }

    public void storeSwicthState() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt("flag", this.switchcheck);
        editor.apply();
        editor.commit();
    }

    public void storeSpinnerState() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = this.preferences.edit();
        editor.putInt("firstspinnerpos", this.firstspinnerpos);
        editor.putInt("secondspinnerpos", this.secondspinnerpos);
        editor.putString("firsttxt", this.firsttxt);
        editor.putString("secondtxt", this.secondtxt);
        editor.apply();
        editor.commit();
    }

    public void getSwitchState() {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.switchcheck = this.preferences.getInt("flag", 1);
        this.firstspinnerpos = this.preferences.getInt("firstspinnerpos", 19);
        this.secondspinnerpos = this.preferences.getInt("secondspinnerpos", 85);
        this.firsttxt = this.preferences.getString("firsttxt", this.firsttxt);
        this.secondtxt = this.preferences.getString("secondtxt", this.secondtxt);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode != 1) {
            return;
        }
        if (grantResults.length <= 0 || grantResults[0] != 0) {
            Toast.makeText(this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
        }
    }

    public void checkPermission(String permission, int requestCode) {
        if (ContextCompat.checkSelfPermission(this, permission) == -1) {
            ActivityCompat.requestPermissions(this, new String[]{permission}, requestCode);
            return;
        }
        Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
    }
}
