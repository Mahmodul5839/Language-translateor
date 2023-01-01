package com.forhad.languagestranslator.SpinnerAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.forhad.languagestranslator.Languages;
import com.forhad.languagestranslator.R;


import java.util.List;

public class SpinnerCustomAdapter extends BaseAdapter {
    Context context;
    Integer[] icons;
    int imageResource;
    private LayoutInflater inflater;
    List<String> result;

    public SpinnerCustomAdapter(Context context2, List<String> result2, Integer[] icons2) {
        this.context = context2;
        this.result = result2;
        this.icons = icons2;
    }

    public int getCount() {
        return Languages.langsEN.length;
    }

    public Object getItem(int i) {
        return Languages.langsEN[i];
    }

    public long getItemId(int i) {
        return (long) i;
    }

    public View getView(int i, View view, ViewGroup viewGroup) {
        View rowView = View.inflate(this.context, R.layout.custom_spinner_cell, (ViewGroup) null);
        TextView textView = (TextView) rowView.findViewById(R.id.countryflag);
        TextView countrynametxt = (TextView) rowView.findViewById(R.id.countrynametxt);
        try {
            countrynametxt.setText(this.result.get(i));
            this.imageResource = this.icons[i].intValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Drawable drawable = ContextCompat.getDrawable(this.context, this.imageResource);
        int pixelDrawableSize = (int) Math.round(((double) countrynametxt.getLineHeight()) * 1.0d);
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize);
        countrynametxt.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
        return rowView;
    }
}
