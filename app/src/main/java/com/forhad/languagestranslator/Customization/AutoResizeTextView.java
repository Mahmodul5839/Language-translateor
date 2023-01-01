package com.forhad.languagestranslator.Customization;

import android.content.Context;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatTextView;

public class AutoResizeTextView extends AppCompatTextView {
    public static final float MIN_TEXT_SIZE = 20.0f;
    private static final String mEllipsis = "...";
    private boolean mAddEllipsis;
    private float mMaxTextSize;
    private float mMinTextSize;
    private boolean mNeedsResize;
    private float mSpacingAdd;
    private float mSpacingMult;
    private OnTextResizeListener mTextResizeListener;
    private float mTextSize;

    public interface OnTextResizeListener {
        void onTextResize(TextView textView, float f, float f2);
    }

    public AutoResizeTextView(Context context) {
        this(context, (AttributeSet) null);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoResizeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mNeedsResize = false;
        this.mMaxTextSize = 0.0f;
        this.mMinTextSize = 20.0f;
        this.mSpacingMult = 1.0f;
        this.mSpacingAdd = 0.0f;
        this.mAddEllipsis = true;
        this.mTextSize = getTextSize();
    }

    /* access modifiers changed from: protected */
    public void onTextChanged(CharSequence text, int start, int before, int after) {
        this.mNeedsResize = true;
        resetTextSize();
    }

    /* access modifiers changed from: protected */
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w != oldw || h != oldh) {
            this.mNeedsResize = true;
        }
    }

    public void setTextSize(float size) {
        super.setTextSize(size);
        this.mTextSize = getTextSize();
    }

    public void setTextSize(int unit, float size) {
        super.setTextSize(unit, size);
        this.mTextSize = getTextSize();
    }

    public void setLineSpacing(float add, float mult) {
        super.setLineSpacing(add, mult);
        this.mSpacingMult = mult;
        this.mSpacingAdd = add;
    }

    public void resetTextSize() {
        float f = this.mTextSize;
        if (f > 0.0f) {
            super.setTextSize(0, f);
            this.mMaxTextSize = this.mTextSize;
        }
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (changed || this.mNeedsResize) {
            resizeText(((right - left) - getCompoundPaddingLeft()) - getCompoundPaddingRight(), ((bottom - top) - getCompoundPaddingBottom()) - getCompoundPaddingTop());
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    public void resizeText(int width, int height) {
        CharSequence text;
        int i = width;
        int i2 = height;
        CharSequence text2 = getText();
        if (text2 != null && text2.length() != 0 && i2 > 0 && i > 0 && this.mTextSize != 0.0f) {
            if (getTransformationMethod() != null) {
                text = getTransformationMethod().getTransformation(text2, this);
            } else {
                text = text2;
            }
            TextPaint textPaint = getPaint();
            float oldTextSize = textPaint.getTextSize();
            float f = this.mMaxTextSize;
            float targetTextSize = f > 0.0f ? Math.min(this.mTextSize, f) : this.mTextSize;
            float targetTextSize2 = targetTextSize;
            int textHeight = getTextHeight(text, textPaint, i, targetTextSize);
            while (textHeight > i2) {
                float f2 = this.mMinTextSize;
                if (targetTextSize2 <= f2) {
                    break;
                }
                targetTextSize2 = Math.max(targetTextSize2 - 2.0f, f2);
                textHeight = getTextHeight(text, textPaint, i, targetTextSize2);
            }
            if (!this.mAddEllipsis || targetTextSize2 != this.mMinTextSize || textHeight <= i2) {
            } else {
                int i3 = textHeight;
                StaticLayout layout = new StaticLayout(text, new TextPaint(textPaint), width, Layout.Alignment.ALIGN_NORMAL, this.mSpacingMult, this.mSpacingAdd, false);
                if (layout.getLineCount() > 0) {
                    int lastLine = layout.getLineForVertical(i2) - 1;
                    if (lastLine < 0) {
                        setText("");
                    } else {
                        int start = layout.getLineStart(lastLine);
                        int end = layout.getLineEnd(lastLine);
                        float lineWidth = layout.getLineWidth(lastLine);
                        float ellipseWidth = textPaint.measureText(mEllipsis);
                        while (((float) i) < lineWidth + ellipseWidth) {
                            end--;
                            lineWidth = textPaint.measureText(text.subSequence(start, end + 1).toString());
                        }
                        StringBuilder sb = new StringBuilder();
                        StaticLayout staticLayout = layout;
                        int i4 = lastLine;
                        sb.append(text.subSequence(0, end));
                        sb.append(mEllipsis);
                        setText(sb.toString());
                    }
                }
            }
            setTextSize(0, targetTextSize2);
            setLineSpacing(this.mSpacingAdd, this.mSpacingMult);
            OnTextResizeListener onTextResizeListener = this.mTextResizeListener;
            if (onTextResizeListener != null) {
                onTextResizeListener.onTextResize(this, oldTextSize, targetTextSize2);
            }
            this.mNeedsResize = false;
        }
    }

    private int getTextHeight(CharSequence source, TextPaint paint, int width, float textSize) {
        TextPaint paintCopy = new TextPaint(paint);
        paintCopy.setTextSize(textSize);
        return new StaticLayout(source, paintCopy, width, Layout.Alignment.ALIGN_NORMAL, this.mSpacingMult, this.mSpacingAdd, true).getHeight();
    }
}
