package com.forhad.languagestranslator.SpinnerAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.forhad.languagestranslator.Models.LanguageModel;
import com.forhad.languagestranslator.R;

import java.util.List;


public class LanguagesAdapter extends RecyclerView.Adapter<LanguagesAdapter.MyAdapter> {
    Context context;
    List<LanguageModel> data;
    private LayoutInflater inflater;

    public LanguagesAdapter(Context context2, List<LanguageModel> data2) {
        this.context = context2;
        this.data = data2;
    }

    @NonNull
    public MyAdapter onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (this.inflater == null) {
            this.inflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new MyAdapter(this.inflater.inflate(R.layout.custom_spinner_cell, viewGroup, false));
    }

    public void onBindViewHolder(@NonNull MyAdapter myAdapter, int i) {
        myAdapter.countrynametxt.setText(this.data.get(i).getTitle());
        Drawable drawable = ContextCompat.getDrawable(this.context, this.data.get(i).getIcon().intValue());
        int pixelDrawableSize = (int) Math.round(((double) myAdapter.countrynametxt.getLineHeight()) * 1.0d);
        drawable.setBounds(0, 0, pixelDrawableSize, pixelDrawableSize);
        myAdapter.countrynametxt.setCompoundDrawables(drawable, (Drawable) null, (Drawable) null, (Drawable) null);
    }

    public int getItemCount() {
        return this.data.size();
    }

    public class MyAdapter extends RecyclerView.ViewHolder {
        TextView countrynametxt;

        public MyAdapter(@NonNull View itemView) {
            super(itemView);
            this.countrynametxt = (TextView) itemView.findViewById(R.id.countrynametxt);
        }
    }
}
