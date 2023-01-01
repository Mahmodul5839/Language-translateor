package com.forhad.languagestranslator;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {
    private ArrayList<ChatMessage> mListChatMessages = new ArrayList<>();

    public void add(ChatMessage object) {
        super.add(object);
        this.mListChatMessages.add(object);
    }

    public void clear() {
        super.clear();
        this.mListChatMessages.clear();
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public int getCount() {
        return this.mListChatMessages.size();
    }

    public ChatMessage getItem(int index) {
        return this.mListChatMessages.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = getItem(position);
        View row = convertView;
        @SuppressLint("WrongConstant") LayoutInflater inflater = (LayoutInflater) getContext().getSystemService("layout_inflater");
        if (chatMessage != null) {
            if (chatMessage.getmTranslate()) {
                if (chatMessage.getmLeft()) {
                    row = inflater.inflate(R.layout.chat_translate_left, parent, false);
                } else {
                    row = inflater.inflate(R.layout.chat_translate_right, parent, false);
                }
            } else if (chatMessage.getmLeft()) {
                row = inflater.inflate(R.layout.chat_left, parent, false);
            } else {
                row = inflater.inflate(R.layout.chat_right, parent, false);
            }
            ((TextView) row.findViewById(R.id.message)).setText(chatMessage.getmMessage());
        }
        return row;
    }
}
