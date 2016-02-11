package com.antoniocappiello.cloudapp.ui.customwidget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;

import com.antoniocappiello.cloudapp.R;

public class EditTextWithClearIcon extends EditText implements OnFocusChangeListener {

    private Drawable clearDrawable;
    private boolean hasFocus;
    private Drawable transparentDrawable;

    public EditTextWithClearIcon(Context context) {
        this(context, null);
        init(context);
    }

    public EditTextWithClearIcon(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.editTextStyle);
        init(context);
    }

    public EditTextWithClearIcon(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        clearDrawable = getCompoundDrawables()[2];
        clearDrawable = ContextCompat.getDrawable(context, R.drawable.ic_clear);
        clearDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());

        transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
        transparentDrawable.setBounds(0, 0, clearDrawable.getIntrinsicWidth(), clearDrawable.getIntrinsicHeight());

        setClearIconVisible(false);
        setOnFocusChangeListener(this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (getCompoundDrawables()[2] != null) {

                boolean isDrawableTouched = event.getX() > (getWidth() - getTotalPaddingRight()) &&
                        (event.getX() < ((getWidth() - getPaddingRight())));

                if (isDrawableTouched) {
                    this.setText("");
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        this.hasFocus = hasFocus;
        if (hasFocus) {
            setClearIconVisible(getText().toString().length() > 0);
        }
        else {
            setClearIconVisible(false);
        }

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int count, int after) {
        if(hasFocus){
            setClearIconVisible(getText().toString().length() > 0);
        }
        setTypefaceToNormal(getText().toString().length() > 0);
    }

    private void setTypefaceToNormal(boolean hasText) {
        setTypeface(null, hasText ? Typeface.NORMAL : Typeface.ITALIC);
    }

    private void setClearIconVisible(boolean visible) {
        setCompoundDrawables(getCompoundDrawables()[0], getCompoundDrawables()[1], visible ? clearDrawable : transparentDrawable, getCompoundDrawables()[3]);

    }

}
