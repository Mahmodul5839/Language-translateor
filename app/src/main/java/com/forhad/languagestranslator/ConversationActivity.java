package com.forhad.languagestranslator;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.mannan.translateapi.Language;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

public class ConversationActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
    public static final String LOG_TAG = ConversationActivity.class.getName();
    private static final int REQ_CODE_SPEECH_INPUT_FROM = 1;
    private static final int REQ_CODE_SPEECH_INPUT_TO = 2;
    volatile boolean activityRunning;
    /* access modifiers changed from: private */
    public ChatArrayAdapter chatArrayAdapter;
    /* access modifiers changed from: private */
    public String mChatInput;
    /* access modifiers changed from: private */
    public EditText mEditTextChatKeyboardInput;
    /* access modifiers changed from: private */
    public String mLanguageCodeFrom = Language.ENGLISH;
    /* access modifiers changed from: private */
    public String mLanguageCodeTo = Language.ENGLISH;
    /* access modifiers changed from: private */
    public boolean mLeftSide;
    /* access modifiers changed from: private */
    public LinearLayout mLinearLayoutKeyboardPopup;
    /* access modifiers changed from: private */
    public ListView mListView;
    /* access modifiers changed from: private */
    public Spinner mSpinnerLanguageFrom;
    /* access modifiers changed from: private */
    public Spinner mSpinnerLanguageTo;
    private TextToSpeech mTextToSpeech;
    HashMap<String, String> map = new HashMap<>();
    /* access modifiers changed from: private */
    public Dialog process_tts;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((int) R.layout.activity_conversation);
        this.activityRunning = true;
        this.mSpinnerLanguageFrom = (Spinner) findViewById(R.id.spinner_language_from);
        this.mSpinnerLanguageTo = (Spinner) findViewById(R.id.spinner_language_to);
        ImageView mImageKeyboardFrom = (ImageView) findViewById(R.id.image_keyboard_from);
        ImageView mImageKeyboardTo = (ImageView) findViewById(R.id.image_keyboard_to);
        ImageView mImageMicFrom = (ImageView) findViewById(R.id.image_mic_from);
        ImageView mImageMicTo = (ImageView) findViewById(R.id.image_mic_to);
        TextView mEmptyTextView = (TextView) findViewById(R.id.empty_view_not_connected);
        this.mListView = (ListView) findViewById(R.id.list_chat_view);
        this.chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.chat_left);
        this.mLinearLayoutKeyboardPopup = (LinearLayout) findViewById(R.id.popup_keyboard);
        this.mEditTextChatKeyboardInput = (EditText) findViewById(R.id.text_keyboard_input);
        this.process_tts = new Dialog(this);
        this.process_tts.setContentView(R.layout.dialog_processing_tts);
        this.process_tts.setTitle(getString(R.string.process_tts));
        this.mTextToSpeech = new TextToSpeech(this, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (!isOnline()) {
            mEmptyTextView.setVisibility(View.GONE);
            return;
        }
        mEmptyTextView.setVisibility(View.GONE);
        this.mLinearLayoutKeyboardPopup.setVisibility(View.VISIBLE);
        this.mListView.setAdapter(this.chatArrayAdapter);
        new GetLanguages().execute(new Void[0]);
        mImageMicFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                intent.putExtra("android.speech.extra.LANGUAGE", ConversationActivity.this.mLanguageCodeFrom);
                intent.putExtra("android.speech.extra.PROMPT", ConversationActivity.this.getString(R.string.speech_prompt));
                try {
                    ConversationActivity.this.startActivityForResult(intent, 1);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ConversationActivity.this.getApplicationContext(), ConversationActivity.this.getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mImageMicTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent("android.speech.action.RECOGNIZE_SPEECH");
                intent.putExtra("android.speech.extra.LANGUAGE_MODEL", "free_form");
                intent.putExtra("android.speech.extra.LANGUAGE", ConversationActivity.this.mLanguageCodeTo);
                intent.putExtra("android.speech.extra.PROMPT", ConversationActivity.this.getString(R.string.speech_prompt));
                try {
                    ConversationActivity.this.startActivityForResult(intent, 2);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(ConversationActivity.this.getApplicationContext(), ConversationActivity.this.getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
                }
            }
        });
        mImageKeyboardFrom.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConversationActivity.this.mLinearLayoutKeyboardPopup.setVisibility(View.GONE);
                ConversationActivity.this.mEditTextChatKeyboardInput.setText("");
                ConversationActivity.this.mEditTextChatKeyboardInput.requestFocus();
                @SuppressLint("WrongConstant") final InputMethodManager imm = (InputMethodManager) ConversationActivity.this.getSystemService("input_method");
                imm.showSoftInput(ConversationActivity.this.mEditTextChatKeyboardInput, 1);
                ((ImageView) ConversationActivity.this.findViewById(R.id.image_send)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(ConversationActivity.this.mEditTextChatKeyboardInput.getWindowToken(), 0);
                        ConversationActivity.this.mLinearLayoutKeyboardPopup.setVisibility(View.GONE);
                        String unused = ConversationActivity.this.mChatInput = ConversationActivity.this.mEditTextChatKeyboardInput.getText().toString();
                        boolean unused2 = ConversationActivity.this.mLeftSide = true;
                        new TranslateText().execute(new String[]{ConversationActivity.this.mChatInput});
                    }
                });
                ((ImageView) ConversationActivity.this.findViewById(R.id.image_back)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(ConversationActivity.this.mEditTextChatKeyboardInput.getWindowToken(), 0);
                        ConversationActivity.this.mLinearLayoutKeyboardPopup.setVisibility(View.GONE);
                    }
                });
            }
        });
        mImageKeyboardTo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                ConversationActivity.this.mLinearLayoutKeyboardPopup.setVisibility(View.VISIBLE);
                ConversationActivity.this.mEditTextChatKeyboardInput.setText("");
                ConversationActivity.this.mEditTextChatKeyboardInput.requestFocus();
                @SuppressLint("WrongConstant") final InputMethodManager imm = (InputMethodManager) ConversationActivity.this.getSystemService("input_method");
                imm.showSoftInput(ConversationActivity.this.mEditTextChatKeyboardInput, 1);
                ((ImageView) ConversationActivity.this.findViewById(R.id.image_send)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(ConversationActivity.this.mEditTextChatKeyboardInput.getWindowToken(), 0);
                        ConversationActivity.this.mLinearLayoutKeyboardPopup.setVisibility(View.VISIBLE);
                        boolean unused = ConversationActivity.this.mLeftSide = false;
                        String unused2 = ConversationActivity.this.mChatInput = ConversationActivity.this.mEditTextChatKeyboardInput.getText().toString();
                        new TranslateText().execute(new String[]{ConversationActivity.this.mChatInput});
                    }
                });
                ((ImageView) ConversationActivity.this.findViewById(R.id.image_back)).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        imm.hideSoftInputFromWindow(ConversationActivity.this.mEditTextChatKeyboardInput.getWindowToken(), 0);
                        ConversationActivity.this.mLinearLayoutKeyboardPopup.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
        this.mSpinnerLanguageFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String unused = ConversationActivity.this.mLanguageCodeFrom = GlobalVars.LANGUAGE_CODES.get(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ConversationActivity.this.getApplicationContext(), "No option selected", Toast.LENGTH_SHORT).show();
            }
        });
        this.mSpinnerLanguageTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String unused = ConversationActivity.this.mLanguageCodeTo = GlobalVars.LANGUAGE_CODES.get(position);
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(ConversationActivity.this.getApplicationContext(), "No option selected", Toast.LENGTH_SHORT).show();
            }
        });
        this.mListView.setTranscriptMode(2);
        this.mListView.setAdapter(this.chatArrayAdapter);
        this.chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            public void onChanged() {
                super.onChanged();
                ConversationActivity.this.mListView.setSelection(ConversationActivity.this.chatArrayAdapter.getCount() - 1);
            }
        });
        this.mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                ChatMessage chatMessage = ConversationActivity.this.chatArrayAdapter.getItem(position);
                if (chatMessage != null) {
                    ConversationActivity.this.speakOut(chatMessage.getmMessage(), chatMessage.getmLanguageCode());
                }
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
        if (requestCode != 1) {
            if (requestCode != 2) {
                return;
            }
        } else if (resultCode == -1 && data != null) {
            final Dialog match_text_dialog = new Dialog(this);
            match_text_dialog.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog.setTitle(getString(R.string.select_matching_text));
            ListView textlist = (ListView) match_text_dialog.findViewById(R.id.list);
            final ArrayList<String> matches_text = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            textlist.setAdapter(new ArrayAdapter(this, 17367043, matches_text));
            textlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String unused = ConversationActivity.this.mChatInput = (String) matches_text.get(position);
                    boolean unused2 = ConversationActivity.this.mLeftSide = true;
                    match_text_dialog.dismiss();
                    new TranslateText().execute(new String[]{ConversationActivity.this.mChatInput});
                }
            });
            match_text_dialog.show();
            return;
        }
        if (resultCode == -1 && data != null) {
            final Dialog match_text_dialog2 = new Dialog(this);
            match_text_dialog2.setContentView(R.layout.dialog_matches_frag);
            match_text_dialog2.setTitle(getString(R.string.select_matching_text));
            ListView textlist2 = (ListView) match_text_dialog2.findViewById(R.id.list);
            final ArrayList<String> matches_text2 = data.getStringArrayListExtra("android.speech.extra.RESULTS");
            textlist2.setAdapter(new ArrayAdapter(this, 17367043, matches_text2));
            textlist2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                    String unused = ConversationActivity.this.mChatInput = (String) matches_text2.get(position);
                    boolean unused2 = ConversationActivity.this.mLeftSide = false;
                    match_text_dialog2.dismiss();
                    new TranslateText().execute(new String[]{ConversationActivity.this.mChatInput});
                }
            });
            match_text_dialog2.show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.conversation_menu_clear, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId != 16908332) {
            if (itemId == R.id.action_clear) {
                this.chatArrayAdapter.clear();
                this.chatArrayAdapter.notifyDataSetChanged();
            }
            return true;
        }
        finish();
        return true;
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
            this.mTextToSpeech.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                public void onStart(String utteranceId) {
                    Log.e("Inside", "OnStart");
                    ConversationActivity.this.process_tts.hide();
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
    public void speakOut(String textMessage, String languageCode) {
        int result = this.mTextToSpeech.setLanguage(new Locale(languageCode));
        Log.e("Inside", "speakOut " + languageCode + " " + result);
        if (result == -1) {
            Toast.makeText(getApplicationContext(), getString(R.string.language_pack_missing), Toast.LENGTH_SHORT).show();
            Intent installIntent = new Intent();
            installIntent.setAction("android.speech.tts.engine.INSTALL_TTS_DATA");
            startActivity(installIntent);
        } else if (result == -2) {
            Toast.makeText(getApplicationContext(), getString(R.string.language_not_supported), Toast.LENGTH_SHORT).show();
        } else {
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

    /* access modifiers changed from: private */
    public boolean sendChatMessage(String textTranslated) {
        String to;
        String from;
        String str = LOG_TAG;
        Log.e(str, "New chat ---------> " + this.mChatInput + " " + textTranslated);
        if (this.mLeftSide) {
            from = this.mLanguageCodeFrom;
            to = this.mLanguageCodeTo;
        } else {
            from = this.mLanguageCodeTo;
            to = this.mLanguageCodeFrom;
        }
        this.chatArrayAdapter.add(new ChatMessage(this.mLeftSide, false, this.mChatInput, from));
        this.chatArrayAdapter.add(new ChatMessage(this.mLeftSide, true, textTranslated, to));
        return true;
    }

    private class TranslateText extends AsyncTask<String, Void, String> {
        private TranslateText() {
        }

        /* access modifiers changed from: protected */
        public String doInBackground(String... input) {
            String to;
            String from;
            if (input[0].isEmpty()) {
                return "";
            }
            if (ConversationActivity.this.mLeftSide) {
                from = ConversationActivity.this.mLanguageCodeFrom;
                to = ConversationActivity.this.mLanguageCodeTo;
            } else {
                from = ConversationActivity.this.mLanguageCodeTo;
                to = ConversationActivity.this.mLanguageCodeFrom;
            }
            Uri.Builder uriBuilder = Uri.parse(GlobalVars.BASE_REQ_URL).buildUpon();
            Uri.Builder appendQueryParameter = uriBuilder.appendPath("translate").appendQueryParameter("key", ConversationActivity.this.getString(R.string.API_KEY));
            appendQueryParameter.appendQueryParameter("lang", from + "-" + to).appendQueryParameter("text", input[0]);
            Log.e("String Url ---->", uriBuilder.toString());
            return QueryUtils.fetchTranslation(uriBuilder.toString());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(String result) {
            if (ConversationActivity.this.activityRunning) {
                boolean unused = ConversationActivity.this.sendChatMessage(result);
            }
        }
    }

    private class GetLanguages extends AsyncTask<Void, Void, ArrayList<String>> {
        private GetLanguages() {
        }

        /* access modifiers changed from: protected */
        public ArrayList<String> doInBackground(Void... params) {
            Uri.Builder uriBuilder = Uri.parse(GlobalVars.BASE_REQ_URL).buildUpon();
            uriBuilder.appendPath("getLangs").appendQueryParameter("key", ConversationActivity.this.getString(R.string.API_KEY)).appendQueryParameter("ui", Language.ENGLISH);
            Log.e("String Url ---->", uriBuilder.toString());
            return QueryUtils.fetchLanguages(uriBuilder.toString());
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(ArrayList<String> result) {
            if (ConversationActivity.this.activityRunning) {
                ArrayAdapter<String> adapter = new ArrayAdapter<>(ConversationActivity.this, 17367048, result);
                adapter.setDropDownViewResource(17367049);
                ConversationActivity.this.mSpinnerLanguageFrom.setAdapter(adapter);
                ConversationActivity.this.mSpinnerLanguageTo.setAdapter(adapter);
                ConversationActivity.this.mSpinnerLanguageFrom.setSelection(GlobalVars.DEFAULT_LANG_POS);
                ConversationActivity.this.mSpinnerLanguageTo.setSelection(GlobalVars.DEFAULT_LANG_POS);
            }
        }
    }
}
