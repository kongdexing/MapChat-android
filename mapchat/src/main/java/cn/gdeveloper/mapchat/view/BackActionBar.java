package cn.gdeveloper.mapchat.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gdeveloper.mapchat.R;

/**
 * Created by Dexing on 2015/8/23.
 */
public class BackActionBar extends LinearLayout implements View.OnClickListener {

    private TextView txt_title;

    public BackActionBar(Context context) {
        this(context, null);
    }

    public BackActionBar(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.backActionBar);
    }

    public BackActionBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BackActionBar, defStyleAttr, 0);
        int strResId = typedArray.getResourceId(R.styleable.BackActionBar_action_title, 0);
        typedArray.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.layout_backactionbar, this);
        txt_title = (TextView) view.findViewById(R.id.txt_title);
        if (strResId != 0) {
            txt_title.setText(strResId);
        }
        ImageView img_back = (ImageView) view.findViewById(R.id.img_back);
        img_back.setOnClickListener(this);
        txt_title.setOnClickListener(this);
    }

    public void setTitle(String title) {
        txt_title.setText(title);
    }

    public void setTitle(int title) {
        txt_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_title:
            case R.id.img_back:
                ((Activity) this.getContext()).finish();
                break;
        }
    }
}
