package cn.gdeveloper.mapchat.ui;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cn.gdeveloper.mapchat.R;

public class WinToast {

    private static Toast toast;
    private static Toast toastWithIcon;

    public static void toast(Context context, int textRes) {
        CharSequence text = context.getResources().getText(textRes);
        makeText(context, text);
    }

    public static void toast(Context context, CharSequence sequence) {
        makeText(context, sequence);
    }

    public static void toastWithCat(Context context, int textRes, boolean isHappy) {
        CharSequence text = context.getResources().getText(textRes);
        toastWithCat(context, text, isHappy);
    }

    public static void toastWithCat(Context context, CharSequence text, boolean isHappy) {
        if (toastWithIcon == null) {
            toastWithIcon = new Toast(context);
        }
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.de_ui_toast, null);
        toastWithIcon.setView(v);
        ImageView iv = (ImageView) v.findViewById(android.R.id.icon);
        TextView tv = (TextView) v.findViewById(android.R.id.message);
        tv.setText(text);
        toastWithIcon.setGravity(Gravity.CENTER, 0, 0);
        toastWithIcon.setDuration(Toast.LENGTH_SHORT);
        toastWithIcon.show();
    }

    public static void makeText(Context context, CharSequence text) {
        if (toast == null) {
            toast = new Toast(context);
        }
        LayoutInflater inflate = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.de_ui_toast, null);
        toast.setView(v);
        TextView tv = (TextView) v.findViewById(R.id.message);
        tv.setText(text);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
    }
}
