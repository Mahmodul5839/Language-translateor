package com.forhad.languagestranslator;

public class ChatMessage {
    private String mLanguageCode;
    private boolean mLeft;
    private String mMessage;
    private boolean mTranslate;

    ChatMessage(boolean left, boolean translate, String message, String code) {
        this.mLeft = left;
        this.mTranslate = translate;
        this.mMessage = message;
        this.mLanguageCode = code;
    }

    public String getmMessage() {
        return this.mMessage;
    }

    public boolean getmLeft() {
        return this.mLeft;
    }

    public String getmLanguageCode() {
        return this.mLanguageCode;
    }

    public boolean getmTranslate() {
        return this.mTranslate;
    }
}
