package com.forhad.languagestranslator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mannan.translateapi.Language;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class TranslationActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    public static final String LOG_TAG = TranslationActivity.class.getName();
    private static final int REQ_CODE_SPEECH_INPUT = 1;
    volatile boolean activityRunning;
    private ImageView mImageSpeak;
    /* access modifiers changed from: private */
    public String mLanguageCodeFrom = Language.ENGLISH;
    /* access modifiers changed from: private */
    public String mLanguageCodeTo = Language.ENGLISH;
    /* access modifiers changed from: private */
    public Spinner mSpinnerLanguageFrom;
    /* access modifiers changed from: private */
    public Spinner mSpinnerLanguageTo;
    /* access modifiers changed from: private */
    public EditText mTextInput;
    private TextToSpeech mTextToSpeech;
    /* access modifiers changed from: private */
    public TextView mTextTranslated;
    HashMap<String, String> map = new HashMap<>();
    /* access modifiers changed from: private */
    public Dialog process_tts;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_translation);
        getSupportActionBar().hide();
        getWindow().addFlags(1024);
        this.activityRunning = true;
        TextView mEmptyTextView = (TextView) findViewById(R.id.empty_view_not_connected);
        this.mSpinnerLanguageFrom = (Spinner) findViewById(R.id.spinner_language_from);
        this.mSpinnerLanguageTo = (Spinner) findViewById(R.id.spinner_language_to);
        Button mButtonTranslate = (Button) findViewById(R.id.button_translate);
        ImageView mImageSwap = (ImageView) findViewById(R.id.image_swap);
        ImageView mImageListen = (ImageView) findViewById(R.id.image_listen);
        this.mImageSpeak = (ImageView) findViewById(R.id.image_speak);
        ImageView mClearText = (ImageView) findViewById(R.id.clear_text);
        this.mTextInput = (EditText) findViewById(R.id.text_input);
        this.mTextTranslated = (TextView) findViewById(R.id.text_translated);
        this.mTextTranslated.setMovementMethod(new ScrollingMovementMethod());
        this.process_tts = new Dialog(this);
        this.process_tts.setContentView(R.layout.dialog_processing_tts);
        this.process_tts.setTitle(getString(R.string.process_tts));
        this.mTextToSpeech = new TextToSpeech(this, this);
        if (!isOnline()) {
            mEmptyTextView.setVisibility(View.VISIBLE);
            return;
        }
        mEmptyTextView.setVisibility(View.GONE);
        new GetLanguages().execute(new Void[0]);
        mImageListen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                intent.putExtra("android.speech.extra.LANGUAGE", TranslationActivity.this.mLanguageCodeFrom);
                intent.putExtra("android.speech.extra.PROMPT", TranslationActivity.this.getString(R.string.speech_prompt));
                try {
                    TranslationActivity.this.startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(TranslationActivity.this.getApplicationContext(), TranslationActivity.this.getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
                }
            }
        });
        this.mImageSpeak.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TranslationActivity.this.speakOut();
            }
        });
        mButtonTranslate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input = TranslationActivity.this.mTextInput.getText().toString();
                new TranslateText().execute(new String[]{input});
            }
        });
        mImageSwap.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String temp = TranslationActivity.this.mLanguageCodeFrom;
                TranslationActivity translationActivity = TranslationActivity.this;
                String unused = translationActivity.mLanguageCodeFrom = translationActivity.mLanguageCodeTo;
                String unused2 = TranslationActivity.this.mLanguageCodeTo = temp;
                int posFrom = TranslationActivity.this.mSpinnerLanguageFrom.getSelectedItemPosition();
                TranslationActivity.this.mSpinnerLanguageFrom.setSelection(TranslationActivity.this.mSpinnerLanguageTo.getSelectedItemPosition());
                TranslationActivity.this.mSpinnerLanguageTo.setSelection(posFrom);
                String textFrom = TranslationActivity.this.mTextInput.getText().toString();
                TranslationActivity.this.mTextInput.setText(TranslationActivity.this.mTextTranslated.getText().toString());
                TranslationActivity.this.mTextTranslated.setText(textFrom);
            }
        });
        mClearText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                TranslationActivity.this.mTextInput.setText("");
                TranslationActivity.this.mTextTranslated.setText("");
            }
        });
        this.mSpinnerLanguageFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String unused = TranslationActivity.this.mLanguageCodeFrom = GlobalVars.LANGUAGE_CODES.get(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(TranslationActivity.this.getApplicationContext(), "No option selected", Toast.LENGTH_SHORT).show();
            }
        });
        this.mSpinnerLanguageTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String unused = TranslationActivity.this.mLanguageCodeTo = GlobalVars.LANGUAGE_CODES.get(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(TranslationActivity.this.getApplicationContext(), "No option selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public boolean isOnline() {
        try {
            @SuppressLint("WrongConstant") NetworkInfo networkInfo = ((ConnectivityManager) getApplicationContext().getSystemService("connectivity")).getActiveNetworkInfo();
            if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            Log.e(LOG_TAG, e.getMessage());
            return false;
        }
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
                    TranslationActivity.this.mTextInput.setText((CharSequence) matches_text.get(position));
                    match_text_dialog.dismiss();
                }
            });
            match_text_dialog.show();
        }
    }

    public void onInit(int status) {
        Log.e("Inside----->", "onInit");
        if (status == 0) {
            int result = this.mTextToSpeech.setLanguage(new Locale(Language.ENGLISH));
            if (result == -1) {
                Toast.makeText(getApplicationContext(), getString(R.string.language_pack_missing), Toast.LENGTH_SHORT).show();
            } else if (result == -2) {
                Toast.makeText(getApplicationContext(), getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
            }
            this.mImageSpeak.setEnabled(true);
            this.mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                public void onStart(String utteranceId) {
                    Log.e("Inside", "OnStart");
                    TranslationActivity.this.process_tts.hide();
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

    /* access modifiers changed from: private */
    public void speakOut() {
        int result = this.mTextToSpeech.setLanguage(new Locale(this.mLanguageCodeTo));
        Log.e("Inside", "speakOut " + this.mLanguageCodeTo + " " + result);
        if (result == -1) {
            Toast.makeText(getApplicationContext(), getString(R.string.language_pack_missing), Toast.LENGTH_SHORT).show();
            Intent installIntent = new Intent();
            installIntent.setAction("android.speech.tts.engine.INSTALL_TTS_DATA");
            startActivity(installIntent);
        } else if (result == -2) {
            Toast.makeText(getApplicationContext(), getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
        } else {
            String textMessage = this.mTextTranslated.getText().toString();
            this.process_tts.show();
            this.map.put("utteranceId", "UniqueID");
            this.mTextToSpeech.speak(textMessage, 0, this.map);
        }
    }

    /* access modifiers changed from: protected */
    public void onPause() {
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            textToSpeech.stop();
        }
        super.onPause();
    }

    public void onDestroy() {
        TextToSpeech textToSpeech = this.mTextToSpeech;
        if (textToSpeech != null) {
            textToSpeech.stop();
            this.mTextToSpeech.shutdown();
        }
        this.activityRunning = false;
        this.process_tts.dismiss();
        super.onDestroy();
    }

    private class TranslateText extends AsyncTask<String, Void, String> {
        private TranslateText() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... input) {
            Uri.Builder uriBuilder = Uri.parse(GlobalVars.BASE_REQ_URL).buildUpon();
            Uri.Builder appendQueryParameter = uriBuilder.appendPath("translate").appendQueryParameter("key", TranslationActivity.this.getString(R.string.API_KEY));
            appendQueryParameter.appendQueryParameter("lang", TranslationActivity.this.mLanguageCodeFrom + "-" + TranslationActivity.this.mLanguageCodeTo).appendQueryParameter("text", input[0]);
            Log.e("String Url ---->", uriBuilder.toString());
            return QueryUtils.fetchTranslation(uriBuilder.toString());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            if (TranslationActivity.this.activityRunning) {
                TranslationActivity.this.mTextTranslated.setText(result);
            }
        }
    }

    private class GetLanguages extends AsyncTask<Void, Void, ArrayList<String>> {
        private GetLanguages() {
        }

        /* access modifiers changed from: protected */
        public ArrayList<String> doInBackground(Void... params) {
            Uri.Builder uriBuilder = Uri.parse(GlobalVars.BASE_REQ_URL).buildUpon();
            uriBuilder.appendPath("getLangs").appendQueryParameter("key", TranslationActivity.this.getString(R.string.API_KEY)).appendQueryParameter("ui", Language.ENGLISH);
            Log.e("String Url ---->", uriBuilder.toString());
            return QueryUtils.fetchLanguages(uriBuilder.toString());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ArrayList<String> result) {
            if (TranslationActivity.this.activityRunning) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TranslationActivity.this, 17367048, result);
                adapter.setDropDownViewResource(17367049);
                TranslationActivity.this.mSpinnerLanguageTo.setAdapter(adapter);
                TranslationActivity.this.mSpinnerLanguageFrom.setSelection(GlobalVars.DEFAULT_LANG_POS);
                TranslationActivity.this.mSpinnerLanguageTo.setSelection(GlobalVars.DEFAULT_LANG_POS);
            }
        }
    }
}
