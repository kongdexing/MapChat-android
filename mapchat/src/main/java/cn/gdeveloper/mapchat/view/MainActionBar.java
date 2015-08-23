package cn.gdeveloper.mapchat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.gdeveloper.mapchat.R;

/**
 * Created by Dexing on 2015/8/22.
 */
public class MainActionBar extends LinearLayout {

    private ImageView img_user;
    private ImageView img_map;
    private TextView txt_main_title;

    public MainActionBar(Context context) {
        this(context, null);
    }

    public MainActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.layout_mainactionbar, this);
        txt_main_title = (TextView) view.findViewById(R.id.txt_main_title);
        img_user = (ImageView) view.findViewById(R.id.img_user);
        img_map = (ImageView) view.findViewById(R.id.img_map);
        img_map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    public void setMainTitle(int title) {
        setMainTitle(this.getContext().getString(title));
    }

    public void setMainTitle(String title) {
        txt_main_title.setText(title);
    }

    public void setMapIconEnable(boolean enable) {
        img_map.setVisibility(enable ? View.VISIBLE : View.GONE);
    }

}
